package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for SetEditSGValue (8.6.3). Holds a list of (reference, value) pairs to
 * be sent to the server.
 */
@Setter
@Getter
@Accessors(fluent = true)
public class SetEditSgValueDao extends BaseDao {

    private final List<Entry> entries = new ArrayList<>();

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
