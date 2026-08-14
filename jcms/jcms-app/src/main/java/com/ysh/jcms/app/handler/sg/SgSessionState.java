package com.ysh.jcms.app.handler.sg;

import java.util.HashMap;
import java.util.Map;
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
 * <li><b>committedValues</b> — SG (Setting Group) layer, keyed by group number.
 * Promoted from editValues by ConfirmEditSGValues into the current editSG
 * group, read by GetEditSGValue/GetDataValues when fc=SG from the current actSG
 * group.</li>
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
         * SG (Setting Group) committed values: groupNumber -> (ref -> encoded CmsData
         * bytes). Promoted from editValues by ConfirmEditSGValues into the current
         * editSG group.
         */
        private final ConcurrentMap<Integer, ConcurrentMap<String, byte[]>> committedValues = new ConcurrentHashMap<>();

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

        /**
         * Move all edit values to the committed layer of the current editSG group.
         */
        public void commitEditValues() {
            int group = editSG.get();
            ConcurrentMap<String, byte[]> groupValues = committedValues.computeIfAbsent(group, k -> new ConcurrentHashMap<>());
            groupValues.putAll(editValues);
            editValues.clear();
        }

        /**
         * Get the committed value of a ref from the given group.
         *
         * @param ref
         *            the data reference
         * @param group
         *            the group number (1-based)
         * @return encoded CmsData bytes, or {@code null} if not set
         */
        public byte[] getCommittedValue(String ref, int group) {
            ConcurrentMap<String, byte[]> groupValues = committedValues.get(group);
            return groupValues != null ? groupValues.get(ref) : null;
        }

        /**
         * Get the committed value of a ref from the current actSG group.
         */
        public byte[] getCommittedValue(String ref) {
            return getCommittedValue(ref, actSG.get());
        }

        /**
         * Get all committed values for a specific group.
         */
        public ConcurrentMap<String, byte[]> getCommittedValues(int group) {
            return committedValues.get(group);
        }

        /**
         * Get all committed values for the current actSG group.
         */
        public ConcurrentMap<String, byte[]> getCommittedValues() {
            return getCommittedValues(actSG.get());
        }

        // ── Memento ────────────────────────────────────────────────

        /**
         * Immutable snapshot of the entire SgcState.
         * <p>
         * Standard Memento pattern: Originator (SgcState) creates a Memento before
         * mutation, and restores from it to rollback.
         */
        public static class Memento {
            private final int actSG;
            private final int editSG;
            private final Map<String, byte[]> editValues;
            private final Map<Integer, Map<String, byte[]>> committedValues;

            Memento(int actSG, int editSG, Map<String, byte[]> editValues, Map<Integer, Map<String, byte[]>> committedValues) {
                this.actSG = actSG;
                this.editSG = editSG;
                this.editValues = editValues;
                this.committedValues = committedValues;
            }
        }

        /**
         * Capture current state into a Memento. {@code editValues} and
         * {@code committedValues} are defensively copied.
         */
        public Memento saveToMemento() {
            Map<Integer, Map<String, byte[]>> copiedCommitted = new HashMap<>();
            for (Map.Entry<Integer, ConcurrentMap<String, byte[]>> entry : committedValues.entrySet()) {
                copiedCommitted.put(entry.getKey(), new HashMap<>(entry.getValue()));
            }
            return new Memento(actSG.get(), editSG.get(), new HashMap<>(editValues), copiedCommitted);
        }

        /**
         * Restore state from a previously captured Memento. Replaces all fields
         * atomically.
         */
        public void restoreFromMemento(Memento m) {
            this.actSG.set(m.actSG);
            this.editSG.set(m.editSG);
            this.editValues.clear();
            this.editValues.putAll(m.editValues);
            this.committedValues.clear();
            for (Map.Entry<Integer, Map<String, byte[]>> entry : m.committedValues.entrySet()) {
                this.committedValues.put(entry.getKey(), new ConcurrentHashMap<>(entry.getValue()));
            }
        }
    }

    // ── Caretaker (static) ────────────────────────────────────────

    /**
     * Save the session's current state as a Memento. Returns {@code null} if no
     * state exists for the session.
     */
    public static SgcState.Memento saveState(String sessionId) {
        SgcState state = SESSION_STATES.get(sessionId);
        return state != null ? state.saveToMemento() : null;
    }

    /**
     * Restore the session's state from a previously saved Memento. Does nothing if
     * no state exists for the session.
     */
    public static void restoreState(String sessionId, SgcState.Memento memento) {
        SgcState state = SESSION_STATES.get(sessionId);
        if (state != null) {
            state.restoreFromMemento(memento);
        }
    }
}
