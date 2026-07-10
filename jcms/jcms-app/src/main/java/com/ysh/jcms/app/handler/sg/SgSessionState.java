package com.ysh.jcms.app.handler.sg;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shared SGCB session state across all setting group services. Manages
 * per-session actSG, editSG values, and the runtime edit buffer.
 *
 * <p>
 * Two-tier value storage:
 * <ul>
 * <li><b>editValues</b> — SE (Set Edit) buffer, written by SetEditSGValue, read
 * by GetEditSGValue when fc=SE</li>
 * <li><b>committedValues</b> — SG (Setting Group) layer, promoted from
 * editValues by ConfirmEditSGValues, read by GetEditSGValue when fc=SG</li>
 * </ul>
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
         * SE (Set Edit) buffer: ref -> encoded CmsData bytes. Populated by
         * SetEditSGValue (fc=SE).
         */
        private final ConcurrentMap<String, byte[]> editValues = new ConcurrentHashMap<>();
        /**
         * SG (Setting Group) committed values: ref -> encoded CmsData bytes. Promoted
         * from editValues by ConfirmEditSGValues.
         */
        private final ConcurrentMap<String, byte[]> committedValues = new ConcurrentHashMap<>();

        public int getActSG() {
            return actSG.get();
        }
        public void setActSG(int actSG) {
            this.actSG.set(actSG);
        }
        public int getEditSG() {
            return editSG.get();
        }
        public void setEditSG(int editSG) {
            this.editSG.set(editSG);
        }

        // ── SE (edit) buffer ──

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

        // ── SG (committed) layer ──

        /** Move all edit values to the committed layer. */
        public void commitEditValues() {
            committedValues.putAll(editValues);
            editValues.clear();
        }
        public byte[] getCommittedValue(String ref) {
            return committedValues.get(ref);
        }
        public ConcurrentMap<String, byte[]> getCommittedValues() {
            return committedValues;
        }
    }
}
