package com.ysh.dlt2811bean.transport.report;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsTriggerConditions;
import com.ysh.dlt2811bean.scl.model.control.SclReportControl;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ReportControlBlock {

    private static final Logger log = LoggerFactory.getLogger(ReportControlBlock.class);

    private final String ref;
    private final SclReportControl sclReportControl;
    private final boolean buffered;
    private final CopyOnWriteArrayList<CmsServerSession> subscribers = new CopyOnWriteArrayList<>();
    private final AtomicInteger sqNum = new AtomicInteger(0);
    private final Queue<CmsReport> pendingReports = new ConcurrentLinkedQueue<>();
    private final ReentrantLock bufTmLock = new ReentrantLock();

    private volatile CmsRcbOptFlds optFlds;
    private volatile CmsTriggerConditions trgOps;
    private volatile long bufTm;
    private volatile long intgPd;
    private volatile long confRev;
    private volatile boolean enabled;
    private ScheduledExecutorService scheduler;

    private ScheduledFuture<?> integrityFuture;
    private ScheduledFuture<?> bufTmFuture;

    public ReportControlBlock(String ref, SclReportControl sclReportControl,
                               CmsRcbOptFlds optFlds, CmsTriggerConditions trgOps,
                               long bufTm, long intgPd, long confRev) {
        this.ref = ref;
        this.sclReportControl = sclReportControl;
        this.buffered = sclReportControl != null && sclReportControl.isBuffered();
        this.optFlds = optFlds;
        this.trgOps = trgOps;
        this.bufTm = bufTm;
        this.intgPd = intgPd;
        this.confRev = confRev;
        this.enabled = true;
    }

    public void addSubscriber(CmsServerSession session) {
        if (!subscribers.contains(session)) {
            subscribers.add(session);
        }
    }

    public void removeSubscriber(CmsServerSession session) {
        subscribers.remove(session);
    }

    public List<CmsServerSession> getSubscribers() {
        return subscribers;
    }

    public boolean isBuffered() {
        return buffered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRef() {
        return ref;
    }

    public SclReportControl getSclReportControl() {
        return sclReportControl;
    }

    public CmsRcbOptFlds getOptFlds() {
        return optFlds;
    }

    public CmsTriggerConditions getTrgOps() {
        return trgOps;
    }

    public long getConfRev() {
        return confRev;
    }

    public int nextSqNum() {
        int next = sqNum.incrementAndGet();
        return buffered ? (next & 0xFFFF) : (next & 0xFF);
    }

    public int getCurrentSqNum() {
        int cur = sqNum.get();
        return buffered ? (cur & 0xFFFF) : (cur & 0xFF);
    }

    public void updateConfig(CmsRcbOptFlds optFlds, CmsTriggerConditions trgOps, long bufTm, long intgPd, long confRev) {
        this.optFlds = optFlds;
        this.trgOps = trgOps;
        this.bufTm = bufTm;
        this.intgPd = intgPd;
        this.confRev = confRev;
    }

    public void bufferReport(CmsReport report) {
        pendingReports.add(report);
        if (bufTm > 0 && bufTmFuture == null) {
            bufTmLock.lock();
            try {
                if (bufTmFuture == null) {
                    bufTmFuture = scheduler.schedule(this::flushBufferedReports, bufTm, TimeUnit.MILLISECONDS);
                }
            } finally {
                bufTmLock.unlock();
            }
        } else if (bufTm <= 0) {
            flushBufferedReports();
        }
    }

    private void flushBufferedReports() {
        bufTmLock.lock();
        try {
            bufTmFuture = null;
            if (pendingReports.isEmpty()) return;

            CmsReport merged = pendingReports.poll();
            while (!pendingReports.isEmpty()) {
                CmsReport next = pendingReports.poll();
                for (var entryData : next.entry.entryData()) {
                    merged.entry.entryData().add(entryData);
                }
            }

            for (CmsServerSession session : subscribers) {
                if (session.isConnected() && session.isAssociated()) {
                    try {
                        session.send(new com.ysh.dlt2811bean.service.protocol.types.CmsApdu(merged));
                    } catch (Exception e) {
                        log.warn("Failed to send buffered report to {}: {}", session.getSessionId(), e.getMessage());
                    }
                }
            }
        } finally {
            bufTmLock.unlock();
        }
    }

    public void startIntegrityTimer(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        cancelIntegrityTimer();
        if (intgPd > 0 && trgOps.testBit(CmsTriggerConditions.INTEGRITY)) {
            integrityFuture = scheduler.scheduleAtFixedRate(
                    () -> {
                        try {
                            if (enabled && !subscribers.isEmpty()) {
                                triggerIntegrityReport();
                            }
                        } catch (Exception e) {
                            log.error("Integrity report error for {}: {}", ref, e.getMessage(), e);
                        }
                    },
                    intgPd, intgPd, TimeUnit.MILLISECONDS);
            log.debug("Integrity timer started for {}: {}ms", ref, intgPd);
        }
    }

    private void triggerIntegrityReport() {
        if (scheduler != null) {
            scheduler.execute(() -> {
                try {
                    if (enabled && !subscribers.isEmpty()) {
                        CmsReport report = new CmsReport();
                        report.rptID(sclReportControl != null && sclReportControl.getRptID() != null ?
                                sclReportControl.getRptID() : ref);
                        report.optFlds.set(optFlds.get());
                        if (optFlds.testBit(CmsRcbOptFlds.SEQUENCE_NUMBER)) {
                            report.sqNum(nextSqNum());
                        }
                        for (CmsServerSession session : subscribers) {
                            if (session.isConnected() && session.isAssociated()) {
                                try {
                                    session.send(new com.ysh.dlt2811bean.service.protocol.types.CmsApdu(report));
                                } catch (Exception e) {
                                    log.warn("Failed to send integrity report to {}: {}", session.getSessionId(), e.getMessage());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Integrity report error for {}: {}", ref, e.getMessage(), e);
                }
            });
        }
    }

    public void cancelTimers() {
        cancelIntegrityTimer();
        cancelBufTmTimer();
    }

    private void cancelIntegrityTimer() {
        if (integrityFuture != null && !integrityFuture.isCancelled()) {
            integrityFuture.cancel(false);
            integrityFuture = null;
        }
    }

    private void cancelBufTmTimer() {
        if (bufTmFuture != null && !bufTmFuture.isCancelled()) {
            bufTmFuture.cancel(false);
            bufTmFuture = null;
        }
    }

    @Override
    public String toString() {
        return "RCB{" + ref + ", buffered=" + buffered + ", enabled=" + enabled + ", subs=" + subscribers.size() + "}";
    }
}
