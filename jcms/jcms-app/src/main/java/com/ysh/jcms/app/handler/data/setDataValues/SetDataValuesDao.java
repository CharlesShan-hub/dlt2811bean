package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefValueEntry;
import com.ysh.jcms.core.pdu.data.CmsSetDataValuesRequest;
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

    /** Values to set, same order as references (already parsed by the CLI). */
    private List<CmsData> values;

    /** Optional FunctionalConstraint codes, same order as references. */
    private List<String> fcs;

    @Override
    public CmsType toRequest() {
        CmsSetDataValuesRequest req = new CmsSetDataValuesRequest();
        if (references != null && values != null) {
            int size = Math.min(references.size(), values.size());
            for (int i = 0; i < size; i++) {
                String ref = references.get(i);
                CmsData value = values.get(i);
                if (ref == null || ref.isEmpty() || value == null)
                    continue;
                CmsDataRefValueEntry entry = new CmsDataRefValueEntry().reference(ref).value(value);
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

}
