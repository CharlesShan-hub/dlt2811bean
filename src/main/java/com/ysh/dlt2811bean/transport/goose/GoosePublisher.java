package com.ysh.dlt2811bean.transport.goose;

import com.ysh.dlt2811bean.datatypes.data.CmsData;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GoosePublisher {

    private static final Logger log = LoggerFactory.getLogger(GoosePublisher.class);

    private final GooseFrameBuilder frameBuilder = new GooseFrameBuilder();
    private final Map<String, PublisherTask> tasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "goose-publisher");
        t.setDaemon(true);
        return t;
    });

    private PcapHandle sendHandle;
    private GooseDataProvider dataProvider;
    private volatile boolean initialized = false;

    public GoosePublisher dataProvider(GooseDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        return this;
    }

    public synchronized void init() throws Exception {
        if (initialized) return;

        PcapNetworkInterface nif = findSendInterface();
        if (nif == null) {
            throw new IllegalStateException("No suitable network interface found for GOOSE publishing");
        }

        sendHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        sendHandle.setFilter("", BpfProgram.BpfCompileMode.OPTIMIZE);
        initialized = true;
        log.info("GoosePublisher initialized on interface: {}", nif.getName());
    }

    public synchronized void destroy() {
        stopAll();
        if (sendHandle != null) {
            sendHandle.close();
            sendHandle = null;
        }
        scheduler.shutdown();
        initialized = false;
        log.info("GoosePublisher destroyed");
    }

    public void start(GooseConfig config) {
        if (!initialized) {
            throw new IllegalStateException("GoosePublisher not initialized. Call init() first.");
        }
        String ref = config.getGoCBRef();
        tasks.computeIfAbsent(ref, k -> {
            PublisherTask task = new PublisherTask(config);
            task.start();
            log.info("GOOSE publishing started for: {}", ref);
            return task;
        });
    }

    public void stop(String goCBRef) {
        PublisherTask task = tasks.remove(goCBRef);
        if (task != null) {
            task.stop();
            log.info("GOOSE publishing stopped for: {}", goCBRef);
        }
    }

    public void stopAll() {
        for (String ref : tasks.keySet()) {
            stop(ref);
        }
    }

    public void triggerEvent(String goCBRef) {
        PublisherTask task = tasks.get(goCBRef);
        if (task != null) {
            task.triggerEvent();
        }
    }

    public boolean isRunning(String goCBRef) {
        PublisherTask task = tasks.get(goCBRef);
        return task != null && task.running.get();
    }

    private PcapNetworkInterface findSendInterface() throws PcapNativeException {
        List<PcapNetworkInterface> all = Pcaps.findAllDevs();
        if (all == null || all.isEmpty()) return null;

        for (PcapNetworkInterface nif : all) {
            if (nif.isLoopBack()) continue;
            if (nif.getAddresses().isEmpty()) continue;
            for (var addr : nif.getAddresses()) {
                if (addr.getAddress() instanceof java.net.Inet4Address) {
                    return nif;
                }
            }
        }
        return all.get(0);
    }

    private class PublisherTask {
        final GooseConfig config;
        final GooseStateMachine state = new GooseStateMachine();
        final AtomicBoolean running = new AtomicBoolean(false);
        final Object eventLock = new Object();

        ScheduledFuture<?> steadyFuture;
        volatile boolean eventPending = false;

        PublisherTask(GooseConfig config) {
            this.config = config;
        }

        void start() {
            running.set(true);
            state.onEvent();
            sendFrame();
            scheduleFastRetransmit(0);
        }

        void stop() {
            running.set(false);
            if (steadyFuture != null) {
                steadyFuture.cancel(false);
            }
        }

        void triggerEvent() {
            if (!running.get()) return;
            synchronized (eventLock) {
                state.onEvent();
                eventPending = true;
            }
            if (steadyFuture != null) {
                steadyFuture.cancel(false);
            }
            sendFrame();
            scheduleFastRetransmit(0);
        }

        private void scheduleFastRetransmit(int index) {
            if (!running.get()) return;
            if (index >= GooseStateMachine.FAST_RETRANSMIT_DELAYS_MS.length) {
                scheduleSteadyRetransmit();
                return;
            }
            long delay = GooseStateMachine.FAST_RETRANSMIT_DELAYS_MS[index];
            scheduler.schedule(() -> {
                if (!running.get()) return;
                synchronized (eventLock) {
                    if (eventPending) {
                        eventPending = false;
                        return;
                    }
                }
                sendFrame();
                scheduleFastRetransmit(index + 1);
            }, delay, TimeUnit.MILLISECONDS);
        }

        private void scheduleSteadyRetransmit() {
            if (!running.get()) return;
            steadyFuture = scheduler.scheduleAtFixedRate(() -> {
                if (!running.get()) return;
                synchronized (eventLock) {
                    if (eventPending) {
                        eventPending = false;
                        return;
                    }
                }
                sendFrame();
            }, config.getSteadyRetransmitMs(), config.getSteadyRetransmitMs(), TimeUnit.MILLISECONDS);
        }

        private void sendFrame() {
            try {
                state.onTick();
                CmsData<?> dataValue = null;
                if (dataProvider != null) {
                    dataValue = dataProvider.getDataValues(config.getGoCBRef(), config.getDataSetRefs());
                }
                byte[] frame = frameBuilder.buildEthernetFrame(config, state, dataValue);
                sendPacket(frame);
            } catch (Exception e) {
                log.error("Error sending GOOSE frame for {}: {}", config.getGoCBRef(), e.getMessage());
            }
        }

        private void sendPacket(byte[] frame) throws Exception {
            if (sendHandle == null) return;

            Packet packet = new UnknownPacket.Builder()
                    .rawData(frame)
                    .build();

            sendHandle.sendPacket(packet);
        }
    }
}
