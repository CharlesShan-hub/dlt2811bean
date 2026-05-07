package com.ysh.dlt2811bean.transport.session;

import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the CMS KeepAlive (Test) mechanism per session.
 *
 * <p>DL/T 2811 §6.9:
 * <ul>
 *   <li>Idle detection: 30 seconds</li>
 *   <li>Test send interval: 5 seconds</li>
 *   <li>Max retries: 4</li>
 *   <li>Max fault detection: 50 seconds</li>
 * </ul>
 *
 * <p>State machine:
 * <pre>
 *   IDLE ──(data received)──► IDLE
 *   IDLE ──(30s idle)───────► SENDING ──(response)──► IDLE
 *   SENDING ──(no response)──► SENDING (retry up to 4x)
 *   SENDING ──(4 failures)──► TIMEOUT
 * </pre>
 */
public class KeepAliveManager {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveManager.class);

    /** Idle timeout before sending the first Test (ms). Default 30s. */
    private final long idleTimeoutMs;

    /** Interval between Test retries (ms). Default 5s. */
    private final long retryIntervalMs;

    /** Maximum number of Test retries. Default 4. */
    private final int maxRetries;

    private final CmsSession session;
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private volatile long lastDataTime = System.currentTimeMillis();
    private volatile State state = State.IDLE;
    private volatile boolean enabled = true;

    private Thread timerThread;
    private volatile boolean running;

    public enum State {
        IDLE,
        SENDING,
        TIMEOUT
    }

    /**
     * Creates a KeepAliveManager with default DL/T 2811 parameters.
     *
     * @param session the session to manage
     */
    public KeepAliveManager(CmsSession session) {
        this(session, 30_000, 5_000, 4);
    }

    /**
     * Creates a KeepAliveManager with custom parameters.
     *
     * @param session          the session to manage
     * @param idleTimeoutMs   idle timeout before first Test (ms)
     * @param retryIntervalMs interval between Test retries (ms)
     * @param maxRetries      maximum number of retries
     */
    public KeepAliveManager(CmsSession session, long idleTimeoutMs, long retryIntervalMs, int maxRetries) {
        this.session = session;
        this.idleTimeoutMs = idleTimeoutMs;
        this.retryIntervalMs = retryIntervalMs;
        this.maxRetries = maxRetries;
    }

    /**
     * Starts the KeepAlive timer.
     */
    public void start() {
        if (running) return;
        running = true;
        timerThread = new Thread(this::timerLoop, "cms-keepalive-" + session.getSessionId());
        timerThread.setDaemon(true);
        timerThread.start();
    }

    /**
     * Stops the KeepAlive timer.
     */
    public void stop() {
        running = false;
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
    }

    /**
     * Called when any APDU is sent or received. Resets the idle timer.
     */
    public void onActivity() {
        lastDataTime = System.currentTimeMillis();
        if (state == State.SENDING) {
            // Got data while sending Test — connection is alive
            state = State.IDLE;
            retryCount.set(0);
            log.debug("[{}] KeepAlive: activity detected, reset", session.getSessionId());
        }
    }

    public State getState() {
        return state;
    }

    public boolean isTimeout() {
        return state == State.TIMEOUT;
    }

    private void timerLoop() {
        while (running) {
            try {
                long elapsed = System.currentTimeMillis() - lastDataTime;
                long interval;

                if (state == State.IDLE) {
                    interval = idleTimeoutMs;
                } else if (state == State.SENDING) {
                    interval = retryIntervalMs;
                } else {
                    break;  // TIMEOUT state — stop timer
                }

                long sleep = interval - elapsed;
                if (sleep > 0) {
                    Thread.sleep(sleep);
                }

                if (!running) break;

                tick();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void tick() {
        if (!enabled || !session.isConnected()) {
            return;
        }

        if (state == State.IDLE) {
            long elapsed = System.currentTimeMillis() - lastDataTime;
            if (elapsed >= idleTimeoutMs) {
                log.debug("[{}] KeepAlive: idle timeout, sending Test", session.getSessionId());
                sendTest();
                state = State.SENDING;
                retryCount.set(0);
            }
        } else if (state == State.SENDING) {
            if (!session.isAssociated()) {
                // Don't send Test if not associated
                state = State.IDLE;
                return;
            }
            int retries = retryCount.incrementAndGet();
            if (retries > maxRetries) {
                log.warn("[{}] KeepAlive: max retries exceeded, connection dead", session.getSessionId());
                state = State.TIMEOUT;
                session.getConnection().close();
            } else {
                log.debug("[{}] KeepAlive: Test retry {}/{}", session.getSessionId(), retries, maxRetries);
                sendTest();
            }
        }
    }

    private void sendTest() {
        try {
            CmsTest test = new CmsTest();
            CmsApdu apdu = new CmsApdu(test);
            session.send(apdu);
        } catch (Exception e) {
            log.warn("[{}] KeepAlive: failed to send Test: {}", session.getSessionId(), e.getMessage());
        }
    }
}
