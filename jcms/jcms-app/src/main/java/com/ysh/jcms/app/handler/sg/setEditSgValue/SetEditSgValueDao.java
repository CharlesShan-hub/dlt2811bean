package com.ysh.jcms.app.handler.sg.setEditSgValue;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for SetEditSGValue (8.6.3). Holds a list of (reference, value) pairs to
 * be sent to the server.
 */
public class SetEditSgValueDao {

    private final List<Entry> entries = new ArrayList<>();

    public List<Entry> entries() {
        return entries;
    }

    public SetEditSgValueDao addEntry(String ref, byte[] valueBytes, int choiceType) {
        entries.add(new Entry(ref, valueBytes, choiceType));
        return this;
    }

    public static class Entry {
        private final String ref;
        private final byte[] valueBytes;
        private final int choiceType;

        public Entry(String ref, byte[] valueBytes, int choiceType) {
            this.ref = ref;
            this.valueBytes = valueBytes;
            this.choiceType = choiceType;
        }

        public String ref() {
            return ref;
        }
        public byte[] valueBytes() {
            return valueBytes;
        }
        public int choiceType() {
            return choiceType;
        }
    }
}
