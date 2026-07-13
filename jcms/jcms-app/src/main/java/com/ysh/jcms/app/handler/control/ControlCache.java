package com.ysh.jcms.app.handler.control;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory control object selection state.
 *
 * <p>
 * Tracks which session has selected which control object.
 * Select/SelectWithValue locks an object for a session; Operate/Cancel releases
 * it.
 */
public class ControlCache {

    private static final ConcurrentMap<String, ControlLock> locks = new ConcurrentHashMap<>();

    /**
     * Try to select a control object for a session.
     *
     * @return true if the lock was acquired; false if already held by another
     *         session.
     */
    public static boolean select(String ref, String sessionId) {
        return locks.putIfAbsent(ref, new ControlLock(sessionId)) == null;
    }

    /**
     * Release a lock if owned by the given session.
     *
     * @return true if released; false if not owned by this session or not locked.
     */
    public static boolean release(String ref, String sessionId) {
        ControlLock lock = locks.get(ref);
        if (lock != null && sessionId.equals(lock.sessionId)) {
            return locks.remove(ref, lock);
        }
        return false;
    }

    /** Check if a ref is selected by the given session. */
    public static boolean isSelectedBy(String ref, String sessionId) {
        ControlLock lock = locks.get(ref);
        return lock != null && sessionId.equals(lock.sessionId);
    }

    /** Get the session id that holds the lock, or null. */
    public static String getSelector(String ref) {
        ControlLock lock = locks.get(ref);
        return lock != null ? lock.sessionId : null;
    }

    private static class ControlLock {
        final String sessionId;
        ControlLock(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
