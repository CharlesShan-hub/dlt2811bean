package com.ysh.jcms.app.handler.sg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shared SGCB session state across all setting group services.
 * Manages per-session actSG and editSG values.
 */
public class SgSessionState {

    private static final ConcurrentMap<String, SgcState> SESSION_STATES = new ConcurrentHashMap<>();

    public static SgcState getState(String sessionId) {
        return SESSION_STATES.computeIfAbsent(sessionId, k -> new SgcState());
    }

    public static void clear(String sessionId) {
        SESSION_STATES.remove(sessionId);
    }

    public static class SgcState {
        private final AtomicInteger actSG = new AtomicInteger(1);
        private final AtomicInteger editSG = new AtomicInteger(1);

        public int getActSG() { return actSG.get(); }
        public void setActSG(int actSG) { this.actSG.set(actSG); }
        public int getEditSG() { return editSG.get(); }
        public void setEditSG(int editSG) { this.editSG.set(editSG); }
    }
}
