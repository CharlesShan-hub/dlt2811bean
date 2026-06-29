package com.ysh.jcms.app.node;

import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Server-side keepalive/heartbeat manager.
 *
 * <p>Monitors connected sessions for inactivity. If a session has been idle
 * beyond the configured timeout, sends a TEST probe. Disconnects the session
 * if the client fails to respond within the retry limit.
 *
 * <p>Configuration lives under {@code server.keepalive} in application.yaml.
 */
public class KeepAliveManager {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveManager.class);

    private final List<? extends InnerServer.ServerSession> sessions;
    private ScheduledExecutorService executor;
    private volatile boolean running;

    public KeepAliveManager(List<? extends InnerServer.ServerSession> sessions) {
        this.sessions = sessions;
    }

    /** Start the keepalive checker. Does nothing if idleTimeoutMs ≤ 0. */
    public void start() {
        if (running) return;
        com.ysh.jcms.utils.config.CmsConfig.Server.KeepAlive cfg = CmsConfigLoader.load().getServer().getKeepalive();
        if (cfg.getIdleTimeoutMs() <= 0) return;

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "keepalive");
            t.setDaemon(true);
            return t;
        });

        running = true;
        executor.scheduleWithFixedDelay(this::check, 1, 1, TimeUnit.SECONDS);
        log.info("Keepalive started: idleTimeout={}ms, retryInterval={}ms, maxRetries={}",
            cfg.getIdleTimeoutMs(), cfg.getRetryIntervalMs(), cfg.getMaxRetries());
    }

    /** Stop the keepalive checker. */
    public void stop() {
        running = false;
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    public boolean isRunning() { return running; }

    // ── internal ──

    private void check() {
        com.ysh.jcms.utils.config.CmsConfig.Server.KeepAlive cfg = CmsConfigLoader.load().getServer().getKeepalive();
        long now = System.currentTimeMillis();

        for (InnerServer.ServerSession ss : sessions) {
            if (ss.getState() != SessionState.ASSOCIATED) continue;

            long idle = now - ss.getLastActivityTime();
            int retries = ss.getKeepaliveRetries();

            if (idle > cfg.getIdleTimeoutMs() + (long) cfg.getRetryIntervalMs() * (retries + 1)) {
                if (retries >= cfg.getMaxRetries()) {
                    log.warn("Keepalive: session {} max retries exceeded, disconnecting", ss.getSessionId());
                    ss.close();
                } else {
                    ss.incrementKeepaliveRetries();
                    try {
                        ss.getConnection().send(new Frame(
                            new FrameHeader().serviceCode(ServiceName.TEST).resp(false).err(false),
                            new byte[0], 0
                        ));
                        log.debug("Keepalive: sent TEST probe to session {} (retry={})",
                            ss.getSessionId(), retries + 1);
                    } catch (IOException e) {
                        log.warn("Keepalive: TEST probe failed for session {}", ss.getSessionId());
                        ss.close();
                    }
                }
            }
        }
    }
}
