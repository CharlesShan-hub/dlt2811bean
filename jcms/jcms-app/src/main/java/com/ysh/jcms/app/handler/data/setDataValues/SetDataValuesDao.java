package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class SetDataValuesDao extends BaseDao {

    /** Data value entries (reference + value string + optional fc) */
    private List<Entry> entries = new ArrayList<>();

    @Setter
    @Getter
    @Accessors(fluent = true)
    public static class Entry {
        /** Object reference (e.g. "LD0/LLN0.Mod.stVal") */
        private String reference;
        /** Value to set (as string) */
        private String value;
        /** Optional FunctionalConstraint filter */
        private Integer fc;
    }

    public SetDataValuesDao addEntry(String reference, String value) {
        entries.add(new Entry().reference(reference).value(value));
        return this;
    }

    public SetDataValuesDao addEntry(String reference, String value, int fc) {
        entries.add(new Entry().reference(reference).value(value).fc(fc));
        return this;
    }
}
