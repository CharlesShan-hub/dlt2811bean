package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.data.CmsDataRefValueEntry;
import com.ysh.jcms.pdu.data.CmsSetDataValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class SetDataValuesDao extends BaseDao {

    /** Object references, e.g. "LD0/LLN0.Mod.stVal". */
    private List<String> references;

    /** Values to set (as strings), same order as references. */
    private List<String> values;

    /** Optional FunctionalConstraint codes, same order as references. */
    private List<String> fcs;

    @Override
    public CmsType toRequest() {
        CmsSetDataValuesRequest req = new CmsSetDataValuesRequest();
        if (references != null && values != null) {
            int size = Math.min(references.size(), values.size());
            for (int i = 0; i < size; i++) {
                String ref = references.get(i);
                String value = values.get(i);
                if (ref == null || ref.isEmpty() || value == null)
                    continue;
                CmsDataRefValueEntry entry = new CmsDataRefValueEntry().reference(ref);
                fillCmsData(entry.value, value);
                if (fcs != null && i < fcs.size()) {
                    String fcStr = fcs.get(i);
                    if (fcStr != null && !fcStr.isEmpty()) {
                        int fc = Integer.parseInt(fcStr);
                        if (fc != 0)
                            entry.fc(fc);
                    }
                }
                req.data.add(entry);
            }
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
