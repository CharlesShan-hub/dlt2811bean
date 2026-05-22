package com.ysh.dlt2811bean.transport.report;

import com.ysh.dlt2811bean.datatypes.code.CmsReasonCode;
import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.scl.model.data.SclDataValue;
import com.ysh.dlt2811bean.scl.model.ied.SclLDevice;
import com.ysh.dlt2811bean.scl.model.ied.SclLN;
import com.ysh.dlt2811bean.scl.model.ied.SclServer;
import com.ysh.dlt2811bean.scl.model.input.SclDataSet;
import com.ysh.dlt2811bean.scl.model.input.SclFCDA;
import com.ysh.dlt2811bean.scl.model.template.SclDataTypeTemplates;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsReportEntryData;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;

public class ReportEngine {

    private static final Logger log = LoggerFactory.getLogger(ReportEngine.class);

    private final Map<String, ReportControlBlock> rcbs = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> dataRefToRcbRefs = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "report-engine");
        t.setDaemon(true);
        return t;
    });
    private final SclServer sclServer;
    private final SclDataTypeTemplates templates;
    private volatile boolean running;

    public ReportEngine(SclServer sclServer, SclDataTypeTemplates templates) {
        this.sclServer = sclServer;
        this.templates = templates;
    }

    public void start() {
        this.running = true;
        log.info("ReportEngine started");
    }

    public void stop() {
        this.running = false;
        for (ReportControlBlock rcb : rcbs.values()) {
            rcb.cancelTimers();
        }
        rcbs.clear();
        dataRefToRcbRefs.clear();
        scheduler.shutdown();
        log.info("ReportEngine stopped");
    }

    public void enableReport(CmsServerSession session, String rcbRef, SclReportControl rc, CmsRcbOptFlds optFlds, CmsTriggerConditions trgOps, long bufTm, long intgPd, long confRev) {
        ReportControlBlock rcb = rcbs.computeIfAbsent(rcbRef, k -> new ReportControlBlock(rcbRef, rc, optFlds, trgOps, bufTm, intgPd, confRev));
        rcb.addSubscriber(session);
        rcb.updateConfig(optFlds, trgOps, bufTm, intgPd, confRev);

        SclDataSet dataSet = resolveDataSet(rc);
        if (dataSet != null) {
            for (SclFCDA fcda : dataSet.getFcDas()) {
                String ref = fcda.buildFcdaRef();
                dataRefToRcbRefs.computeIfAbsent(ref, k -> ConcurrentHashMap.newKeySet()).add(rcbRef);
            }
        }

        if (rc.isBuffered()) {
            rcb.startIntegrityTimer(scheduler);
        } else {
            rcb.startIntegrityTimer(scheduler);
        }

        log.debug("Report enabled: {} for session {}", rcbRef, session.getSessionId());
    }

    public void disableReport(String rcbRef, CmsServerSession session) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null) return;
        rcb.removeSubscriber(session);
        if (rcb.getSubscribers().isEmpty()) {
            rcb.cancelTimers();
            rcbs.remove(rcbRef);
            dataRefToRcbRefs.values().forEach(s -> s.remove(rcbRef));
            dataRefToRcbRefs.entrySet().removeIf(e -> e.getValue().isEmpty());
            log.debug("Report fully disabled: {}", rcbRef);
        }
    }

    public void onDataChanged(String dataRef, String newValue) {
        if (!running) return;
        Set<String> rcbRefs = dataRefToRcbRefs.get(dataRef);
        if (rcbRefs == null || rcbRefs.isEmpty()) return;

        for (String rcbRef : rcbRefs) {
            ReportControlBlock rcb = rcbs.get(rcbRef);
            if (rcb == null || !rcb.isEnabled()) continue;
            if (!rcb.getTrgOps().testBit(CmsTriggerConditions.DATA_CHANGE)) continue;

            triggerReport(rcb, dataRef, CmsReasonCode.DATA_CHANGE);
        }
    }

    public void onQualityChanged(String dataRef) {
        if (!running) return;
        Set<String> rcbRefs = dataRefToRcbRefs.get(dataRef);
        if (rcbRefs == null || rcbRefs.isEmpty()) return;

        for (String rcbRef : rcbRefs) {
            ReportControlBlock rcb = rcbs.get(rcbRef);
            if (rcb == null || !rcb.isEnabled()) continue;
            if (!rcb.getTrgOps().testBit(CmsTriggerConditions.QUALITY_CHANGE)) continue;

            triggerReport(rcb, dataRef, CmsReasonCode.QUALITY_CHANGE);
        }
    }

    public void onDataUpdated(String dataRef) {
        if (!running) return;
        Set<String> rcbRefs = dataRefToRcbRefs.get(dataRef);
        if (rcbRefs == null || rcbRefs.isEmpty()) return;

        for (String rcbRef : rcbRefs) {
            ReportControlBlock rcb = rcbs.get(rcbRef);
            if (rcb == null || !rcb.isEnabled()) continue;
            if (!rcb.getTrgOps().testBit(CmsTriggerConditions.DATA_UPDATE)) continue;

            triggerReport(rcb, dataRef, CmsReasonCode.DATA_UPDATE);
        }
    }

    public void triggerGI(String rcbRef) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null || !rcb.isEnabled()) return;
        triggerReport(rcb, null, CmsReasonCode.GENERAL_INTERROGATION);
    }

    public void triggerIntegrity(String rcbRef) {
        ReportControlBlock rcb = rcbs.get(rcbRef);
        if (rcb == null || !rcb.isEnabled()) return;
        triggerReport(rcb, null, CmsReasonCode.INTEGRITY);
    }

    private void triggerReport(ReportControlBlock rcb, String changedDataRef, int reasonCode) {
        try {
            CmsReport report = buildReport(rcb, changedDataRef, reasonCode);
            if (report == null) return;

            if (rcb.isBuffered()) {
                rcb.bufferReport(report);
            } else {
                sendReportToSubscribers(rcb, report);
            }
        } catch (Exception e) {
            log.error("Error triggering report for {}: {}", rcb.getRef(), e.getMessage(), e);
        }
    }

    private CmsReport buildReport(ReportControlBlock rcb, String changedDataRef, int reasonCode) {
        SclDataSet dataSet = resolveDataSet(rcb.getSclReportControl());
        if (dataSet == null) return null;

        CmsReport report = new CmsReport();
        report.rptID(rcb.getSclReportControl().getRptID() != null ?
                rcb.getSclReportControl().getRptID() : rcb.getRef());

        CmsRcbOptFlds optFlds = rcb.getOptFlds();
        report.optFlds.set(optFlds.get());

        if (optFlds.testBit(CmsRcbOptFlds.SEQUENCE_NUMBER)) {
            report.sqNum(rcb.nextSqNum());
        }

        if (optFlds.testBit(CmsRcbOptFlds.SEGMENTATION)) {
            int sub = rcb.isBuffered() ? (rcb.getCurrentSqNum() & 0xFFFF) : (rcb.getCurrentSqNum() & 0xFF);
            report.subSqNum(sub);
            report.moreSegmentsFollow(false);
        }

        if (optFlds.testBit(CmsRcbOptFlds.REPORT_TIME_STAMP)) {
            Instant now = Instant.now();
            LocalDate date = now.atZone(ZoneId.of("UTC")).toLocalDate();
            LocalDate epoch = LocalDate.of(1984, 1, 1);
            long msOfDay = now.toEpochMilli() % 86400000L;
            int daysSince1984 = (int) java.time.temporal.ChronoUnit.DAYS.between(epoch, date);
            report.entry.timeOfEntry().msOfDay(msOfDay).daysSince1984(daysSince1984);
        }

        if (optFlds.testBit(CmsRcbOptFlds.DATA_SET_NAME)) {
            report.datSet(rcb.getRef() + "." + dataSet.getName());
        }

        if (optFlds.testBit(CmsRcbOptFlds.CONF_REVISION)) {
            report.confRev(rcb.getConfRev());
        }

        if (rcb.isBuffered() && optFlds.testBit(CmsRcbOptFlds.ENTRY_ID)) {
            byte[] entryIdBytes = new byte[8];
            long sqNum = rcb.getCurrentSqNum();
            for (int i = 7; i >= 0; i--) {
                entryIdBytes[i] = (byte) (sqNum & 0xFF);
                sqNum >>= 8;
            }
            report.entry.entryID().set(entryIdBytes);
        }

        int entryId = 1;
        for (SclFCDA fcda : dataSet.getFcDas()) {
            String ref = fcda.buildFcdaRef();
            if (reasonCode == CmsReasonCode.DATA_CHANGE && changedDataRef != null && !ref.equals(changedDataRef)) {
                continue;
            }

            CmsReportEntryData entryData = new CmsReportEntryData();

            if (optFlds.testBit(CmsRcbOptFlds.DATA_REFERENCE)) {
                entryData.reference(ref).id(0);
                if (fcda.getFc() != null && !fcda.getFc().isEmpty()) {
                    entryData.fc(fcda.getFc());
                }
            } else {
                entryData.id(entryId);
            }
            entryId++;

            SclDataValue dataValue = sclServer.resolveDataValue(ref, templates);
            if (dataValue != null && dataValue.val() != null) {
                try {
                    long longVal = Long.parseLong(dataValue.val());
                    entryData.value(new CmsInt32((int) longVal));
                } catch (NumberFormatException e) {
                    entryData.value(new com.ysh.dlt2811bean.datatypes.string.CmsVisibleString(dataValue.val()));
                }
            } else {
                entryData.value(new CmsInt32(0));
            }

            if (optFlds.testBit(CmsRcbOptFlds.REASON_FOR_INCLUSION)) {
                entryData.reason.setBit(reasonCode, true);
            }

            report.entry.entryData().add(entryData);
        }

        return report;
    }

    private void sendReportToSubscribers(ReportControlBlock rcb, CmsReport report) throws Exception {
        CmsApdu apdu = new CmsApdu(report);
        for (CmsServerSession session : rcb.getSubscribers()) {
            if (session.isConnected() && session.isAssociated()) {
                try {
                    session.send(apdu);
                    log.trace("Report sent to {}: {}", session.getSessionId(), rcb.getRef());
                } catch (Exception e) {
                    log.warn("Failed to send report to {}: {}", session.getSessionId(), e.getMessage());
                }
            }
        }
    }

    private SclDataSet resolveDataSet(SclReportControl rc) {
        if (rc == null || rc.getDatSet() == null) return null;
        for (SclLDevice ld : sclServer.getLDevices()) {
            for (SclLN ln : ld.getLns()) {
                SclDataSet ds = ln.findDataSetByName(rc.getDatSet());
                if (ds != null) return ds;
            }
        }
        return null;
    }

    public void onSessionClosed(CmsServerSession session) {
        for (ReportControlBlock rcb : rcbs.values()) {
            rcb.removeSubscriber(session);
        }
        rcbs.values().removeIf(rcb -> rcb.getSubscribers().isEmpty());
    }

    public ReportControlBlock getRcb(String ref) {
        return rcbs.get(ref);
    }

    public Collection<ReportControlBlock> getAllRcbs() {
        return rcbs.values();
    }
}
