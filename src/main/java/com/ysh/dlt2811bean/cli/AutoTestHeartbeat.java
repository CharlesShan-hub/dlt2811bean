package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.transport.app.CmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AutoTestHeartbeat {

    private static final Logger log = LoggerFactory.getLogger(AutoTestHeartbeat.class);

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> future;

    public synchronized void start(CmsClient client, long intervalSeconds) {
        stop();
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "cms-auto-test");
            t.setDaemon(true);
            return t;
        });
        future = scheduler.scheduleWithFixedDelay(() -> {
            try {
                if (!client.isConnected()) {
                    log.debug("AutoTest: not connected, stopping");
                    stop();
                    return;
                }
                client.test();
            } catch (Exception e) {
                log.debug("AutoTest: send failed: {}", e.getMessage());
            }
        }, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    public synchronized void stop() {
        if (future != null) {
            future.cancel(false);
            future = null;
        }
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
    }
}