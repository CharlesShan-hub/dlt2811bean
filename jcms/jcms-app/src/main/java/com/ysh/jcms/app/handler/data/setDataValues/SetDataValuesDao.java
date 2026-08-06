package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.data.CmsDataRefValueEntry;
import com.ysh.jcms.pdu.data.CmsSetDataValuesRequest;
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

    @Override
    public CmsType toRequest() {
        CmsSetDataValuesRequest req = new CmsSetDataValuesRequest();
        for (Entry src : entries) {
            CmsDataRefValueEntry entry = new CmsDataRefValueEntry().reference(src.reference());
            fillCmsData(entry.value, src.value());
            if (src.fc() != null && src.fc() != 0) {
                entry.fc(src.fc());
            }
            req.data.add(entry);
        }
        return req;
    }

    private static void fillCmsData(CmsData data, String value) {
        if (containsNonAscii(value)) {
            data.alt_unicode_string(value);
        } else {
            data.alt_visible_string(value);
        }
    }

    private static boolean containsNonAscii(String s) {
        if (s == null)
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127)
                return true;
        }
        return false;
    }
}
