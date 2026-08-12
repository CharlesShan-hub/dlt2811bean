package com.ysh.jcms.app.node;

import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.config.CmsConfigLoader;
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
 * <p>
 * Monitors connected sessions for inactivity. If a session has been idle beyond
 * the configured timeout, sends a TEST probe. Disconnects the session if the
 * client fails to respond within the retry limit.
 *
 * <p>
 * Configuration lives under {@code server.keepalive} in application.yaml.
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
        if (running)
            return;
        com.ysh.jcms.utils.config.CmsConfig.Server.KeepAlive cfg = CmsConfigLoader.load().server().keepalive();
        if (cfg.idleTimeoutMs() <= 0)
            return;

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "keepalive");
            t.setDaemon(true);
            return t;
        });

        running = true;
        executor.scheduleWithFixedDelay(this::check, 1, 1, TimeUnit.SECONDS);
        log.info("Keepalive started: idleTimeout={}ms, retryInterval={}ms, maxRetries={}", cfg.idleTimeoutMs(), cfg.retryIntervalMs(),
                cfg.maxRetries());
    }

    /** Stop the keepalive checker. */
    public void stop() {
        running = false;
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    public boolean isRunning() {
        return running;
    }

    // ── internal ──

    private void check() {
        com.ysh.jcms.utils.config.CmsConfig.Server.KeepAlive cfg = CmsConfigLoader.load().server().keepalive();
        long now = System.currentTimeMillis();

        for (InnerServer.ServerSession ss : sessions) {
            // DL/T 2811 6.9.2: communication state checks apply after association
            if (ss.state() != SessionState.ASSOCIATED)
                continue;

            long idle = now - ss.lastActivityTime();
            int retries = ss.keepaliveRetries();

            if (idle > cfg.idleTimeoutMs() + (long) cfg.retryIntervalMs() * (retries + 1)) {
                if (retries >= cfg.maxRetries()) {
                    log.warn("Keepalive: session {} max retries exceeded, disconnecting", ss.sessionId());
                    ss.close();
                } else {
                    ss.incrementKeepaliveRetries();
                    try {
                        ss.connection()
                                // DL/T 2811 6.9.2: probe an idle link with a Test frame
                                .send(new Frame(new FrameHeader().serviceCode(CmsServiceInfo.TEST).resp(false).err(false), new byte[0], 0));
                        log.debug("Keepalive: sent TEST probe to session {} (retry={})", ss.sessionId(), retries + 1);
                    } catch (IOException e) {
                        log.warn("Keepalive: TEST probe failed for session {}", ss.sessionId());
                        ss.close();
                    }
                }
            }
        }
    }
}
