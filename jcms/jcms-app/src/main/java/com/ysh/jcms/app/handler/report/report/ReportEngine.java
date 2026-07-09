package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.data.block.CmsReasonCode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.svc.report.CmsReport;
import com.ysh.jcms.svc.report.CmsReportDataEntry;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.convert.DataConverter;
import com.ysh.jcms.utils.scl2.convert.DataValueResolver;
import com.ysh.jcms.utils.scl2.convert.DataValueEntry;
import com.ysh.jcms.utils.scl2.model.ied.SclLN;
import com.ysh.jcms.utils.scl2.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl2.model.ied.SclServer;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl2.model.control.SclReportControl;
import com.ysh.jcms.utils.scl2.model.input.SclDataSet;
import com.ysh.jcms.utils.scl2.model.input.SclFCDA;
import com.ysh.jcms.utils.scl2.model.instance.SclDAI;
import com.ysh.jcms.utils.scl2.model.instance.SclDOI;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.app.node.InnerServer.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

/**
 * ReportEngine — 报告引擎（8.7.1）。
 *
 * <p>管理报告控制块的订阅，处理 GI（总召唤）、完整性周期定时触发，
 * 构建 CmsReport PDU 并推送给订阅的 Session。
 */
public class ReportEngine {

    private static final Logger log = LoggerFactory.getLogger(ReportEngine.class);

    private final SclDocument doc;
    private final Map<String, ReportControlBlock> rcbs = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "report-engine");
        t.setDaemon(true);
        return t;
    });

    private static ReportEngine instance;

    public static ReportEngine getInstance() { return instance; }

    /** Initialize with the SCL document. Call once at startup. */
    public ReportEngine(SclDocument doc) {
        this.doc = doc;
        instance = this;
        log.info("ReportEngine initialized");
    }

    /** Subscribe a session to an RCB. Called when rptEna=true is set. */
    public void subscribe(String rcbRef, Session session) {
        ReportControlBlock rcb = rcbs.computeIfAbsent(rcbRef, k -> {
            SclReportControl rc = resolveReportControl(rcbRef);
            return new ReportControlBlock(rcbRef, rc);
        });
        rcb.addSubscriber(session);
        log.info("Report subscription: {} added for ref={}", session.getSessionId(), rcbRef);
    }

    /** Unsubscribe a session from an RCB. Called when rptEna=false is set. */
    public void unsubscribe(String rcbRef, Session session) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null) return;
        rcb.removeSubscriber(session);
        rcb.cancelIntegrityTimer();
        log.info("Report unsubscription: {} removed for ref={}", session.getSessionId(), rcbRef);
    }

    /**
     * Trigger GI (总召唤) for an RCB.
     * Builds a report from the DataSet members and pushes to all subscribers.
     */
    public void triggerGi(String rcbRef) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null || rcb.getSubscribers().isEmpty()) {
            log.warn("GI triggered but no subscribers for ref={}", rcbRef);
            return;
        }
        log.info("GI triggered for ref={}, subscribers={}", rcbRef, rcb.getSubscribers().size());
        pushReport(rcb, CmsReasonCode.class, true); // general_interrogation
    }

    /** Start the integrity period timer for an RCB. */
    public void startIntegrityTimer(String rcbRef, long intgPdMs) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null) return;
        rcb.startIntegrityTimer(scheduler, intgPdMs, this);
        log.info("Integrity timer started for ref={}, intgPd={}ms", rcbRef, intgPdMs);
    }

    /** Stop the integrity period timer for an RCB. */
    public void stopIntegrityTimer(String rcbRef) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null) return;
        rcb.cancelIntegrityTimer();
        log.info("Integrity timer stopped for ref={}", rcbRef);
    }

    // ── Internal ──

    /** Build and push a report to all subscribers of an RCB. */
    void pushReport(ReportControlBlock rcb, boolean isIntegrity) {
        pushReport(rcb, null, isIntegrity);
    }

    void pushReport(ReportControlBlock rcb, Class<?> reasonCodeClass, boolean isGi) {
        // Run asynchronously to avoid blocking the request handler
        scheduler.submit(() -> {
            try {
                log.debug("Building report for ref={}", rcb.getRef());
                CmsReport report = buildReport(rcb, isGi);
                if (report == null) {
                    log.warn("buildReport returned null for ref={}", rcb.getRef());
                    return;
                }

                byte[] asduBytes;
                try {
                    asduBytes = report.encode();
                } catch (Exception e) {
                    log.error("Failed to encode report for ref={}", rcb.getRef(), e);
                    return;
                }
                Frame frame = new Frame(
                    new FrameHeader().serviceCode(ServiceName.REPORT).resp(true).err(false),
                    asduBytes, 0  // reqId=0 for unsolicited
                );

                int sent = 0;
                for (Session session : rcb.getSubscribers()) {
                    try {
                        if (session instanceof ServerSession) {
                            ServerSession ss = (ServerSession) session;
                            ss.getConnection().send(frame);
                            sent++;
                        }
                    } catch (Exception e) {
                        log.warn("Failed to push report to {}: {}", session.getSessionId(), e.getMessage());
                    }
                }
                log.info("Report pushed for ref={}: {} bytes, {} subscribers", rcb.getRef(), asduBytes.length, sent);
            } catch (Exception e) {
                log.error("Failed to push report for ref={}", rcb.getRef(), e);
            }
        });
    }

    /** Build a CmsReport PDU from DataSet member values. */
    CmsReport buildReport(ReportControlBlock rcb, boolean isGi) throws Exception {
        SclReportControl rc = rcb.getSclReportControl();
        if (rc == null || rc.datSet() == null) return null;

        SclDataSet dataSet = resolveDataSet(rc, rcb.getRef());
        if (dataSet == null) {
            log.warn("DataSet {} not found for ref={}", rc.datSet(), rcb.getRef());
            return null;
        }

        CmsReport report = new CmsReport();
        // rptID
        String rptId = rc.rptID() != null ? rc.rptID() : rcb.getRef();
        report.rptID(rptId);

        // optFlds (all false defaults)
        report.optFlds = new com.ysh.jcms.data.block.CmsRcbOptFlds();

        // DatSet
        report.dataSetPresent(true);
        report.dataSet(rc.datSet());

        // sqNum
        report.sqNumPresent(true);
        report.sqNum(rcb.nextSqNum());

        // entry timeOfEntry
        Instant now = Instant.now();
        LocalDate date = now.atZone(ZoneId.of("UTC")).toLocalDate();
        LocalDate epoch = LocalDate.of(1984, 1, 1);
        long msOfDay = now.toEpochMilli() % 86400000L;
        int daysSince1984 = (int) java.time.temporal.ChronoUnit.DAYS.between(epoch, date);

        report.entry.timeOfEntryPresent(true);
        report.entry.timeOfEntry.msOfDay(msOfDay).daysSince1984(daysSince1984);

        // Pre-allocate entryData array
        int totalFcdas = dataSet.fcDas().size();

        // entryData — for each FCDA in DataSet
        int entryId = 1;
        for (SclFCDA fcda : dataSet.fcDas()) {
            CmsReportDataEntry entryData = new CmsReportDataEntry();

            // reference
            String fcdaRef = fcda.buildFcdaRef();
            entryData.refPresent(true);
            entryData.reference(fcdaRef);

            // id (1-based)
            entryData.id(entryId++);

            // value — try to read from SCL model
            CmsData val = readFcdaValue(fcda);
            if (val != null) {
                entryData.value = val;
            }

            // reason
            CmsReasonCode reason = new CmsReasonCode();
            if (isGi) {
                reason.general_interrogation(true);
            } else {
                reason.integrity(true);
            }
            entryData.reasonPresent(true);
            entryData.reason = reason;

            report.entry.entryData.add(entryData);
        }

        return report;
    }

    /** Read a data value from the SCL2 model for a given FCDA. */
    private CmsData readFcdaValue(SclFCDA fcda) {
        try {
            // Build the LN reference
            String lnRef = fcda.ldInst() + "/" + fcda.buildLnName();
            SclLN ln = findLnByLdRef(fcda.ldInst(), fcda.buildLnName());
            if (ln == null) return null;

            // Find DOI for this DO
            SclDOI doi = ln.findDoiByName(fcda.doName());
            if (doi == null) return null;

            // Find DAI for this DA
            if (fcda.daName() != null && !fcda.daName().isEmpty()) {
                SclDAI dai = doi.findDaiByName(fcda.daName());
                if (dai != null && !dai.vals().isEmpty()) {
                    String v = dai.vals().get(0).value();
                    if (v != null && !v.isEmpty()) {
                        return DataConverter.autoDetect(v);
                    }
                }
            }

            // Try first DAI from the DOI
            if (!doi.dais().isEmpty()) {
                SclDAI firstDai = doi.dais().get(0);
                if (!firstDai.vals().isEmpty()) {
                    String v = firstDai.vals().get(0).value();
                    if (v != null && !v.isEmpty()) {
                        return DataConverter.autoDetect(v);
                    }
                }
            }
        } catch (Exception e) {
            log.trace("Failed to read data value for FCDA: {}", fcda.buildFcdaRef(), e);
        }
        return null;
    }

    /** Resolve the SCL ReportControl by reference. */
    private SclReportControl resolveReportControl(String ref) {
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) return null;
        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);
        SclLN ln = findLnByLdRef(ldName, lnName);
        if (ln == null) return null;
        for (SclReportControl rc : ln.reportControls()) {
            if (rc.name().equals(cbName)) return rc;
        }
        return null;
    }

    /** Resolve DataSet by name from the report control, scoped to the correct LN. */
    private SclDataSet resolveDataSet(SclReportControl rc, String rcbRef) {
        if (rc == null || rc.datSet() == null) return null;
        // Parse the RCB ref to find the owning LN: e.g. "LD0/LLN0.brcbAlarm"
        int slashIdx = rcbRef.indexOf('/');
        int dotIdx = rcbRef.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) return null;
        String ldName = rcbRef.substring(0, slashIdx);
        String lnRef = rcbRef.substring(slashIdx + 1, dotIdx);  // "LLN0"
        SclLN ln = findLnByLdRef(ldName, lnRef);
        if (ln == null) return null;
        return ln.findDataSetByName(rc.datSet());
    }

    /** 跨 IED/AccessPoint 查找指定 LD/LN 下的 LN。 */
    private SclLN findLnByLdRef(String ldName, String lnName) {
        if (doc == null) return null;
        SclIED ied = doc.findIedByLdInst(ldName);
        if (ied == null) return null;
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice ld = srv.findLDeviceByInst(ldName);
                if (ld != null) {
                    return ld.findLnByFullName(lnName);
                }
            }
        }
        return null;
    }

    // ── ReportControlBlock inner class ──

    static class ReportControlBlock {
        private final String ref;
        private final SclReportControl sclReportControl;
        private final List<Session> subscribers = new CopyOnWriteArrayList<>();
        private volatile int sqNum;
        private ScheduledFuture<?> integrityFuture;

        ReportControlBlock(String ref, SclReportControl rc) {
            this.ref = ref;
            this.sclReportControl = rc;
            this.sqNum = 0;
        }

        String getRef() { return ref; }
        SclReportControl getSclReportControl() { return sclReportControl; }
        List<Session> getSubscribers() { return subscribers; }

        void addSubscriber(Session session) {
            if (!subscribers.contains(session)) subscribers.add(session);
        }

        void removeSubscriber(Session session) {
            subscribers.remove(session);
        }

        synchronized int nextSqNum() {
            return ++sqNum;
        }

        synchronized void startIntegrityTimer(ScheduledExecutorService scheduler, long intgPdMs, ReportEngine engine) {
            cancelIntegrityTimer();
            if (intgPdMs > 0) {
                integrityFuture = scheduler.scheduleAtFixedRate(
                    () -> engine.pushReport(this, false),
                    intgPdMs, intgPdMs, TimeUnit.MILLISECONDS
                );
            }
        }

        synchronized void cancelIntegrityTimer() {
            if (integrityFuture != null) {
                integrityFuture.cancel(false);
                integrityFuture = null;
            }
        }
    }
}
