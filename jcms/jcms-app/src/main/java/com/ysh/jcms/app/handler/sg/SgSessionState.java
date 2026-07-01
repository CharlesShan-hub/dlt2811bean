package com.ysh.jcms.app.handler.sg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shared SGCB session state across all setting group services.
 * Manages per-session actSG, editSG values, and the runtime edit buffer.
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
        /**
         * Runtime edit buffer: ref -> encoded CmsData bytes.
         * Populated by SetEditSGValue, consumed by GetEditSGValue.
         */
        private final ConcurrentMap<String, byte[]> editValues = new ConcurrentHashMap<>();

        public int getActSG() { return actSG.get(); }
        public void setActSG(int actSG) { this.actSG.set(actSG); }
        public int getEditSG() { return editSG.get(); }
        public void setEditSG(int editSG) { this.editSG.set(editSG); }

        public void putEditValue(String ref, byte[] encodedData) {
            editValues.put(ref, encodedData);
        }
        public byte[] getEditValue(String ref) {
            return editValues.get(ref);
        }
        public ConcurrentMap<String, byte[]> getEditValues() {
            return editValues;
        }
        public void clearEditValues() {
            editValues.clear();
        }
    }
}
