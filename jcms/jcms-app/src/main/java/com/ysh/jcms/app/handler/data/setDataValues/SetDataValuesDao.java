package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.util.CmsDataFiller;
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

    /** Values to set (as strings), same order as references. */
    private List<String> values;

    /** Optional FunctionalConstraint codes, same order as references. */
    private List<String> fcs;

    @Override
    public CmsType toRequest() {
        // gray("LOG4 toRequest: refs=" + references + ", values=" + values + ", fcs=" +
        // fcs + ", pairCount="
        // + (references != null && values != null ? Math.min(references.size(),
        // values.size()) : 0));
        CmsSetDataValuesRequest req = new CmsSetDataValuesRequest();
        if (references != null && values != null) {
            int size = Math.min(references.size(), values.size());
            for (int i = 0; i < size; i++) {
                String ref = references.get(i);
                String value = values.get(i);
                if (ref == null || ref.isEmpty() || value == null)
                    continue;
                CmsDataRefValueEntry entry = new CmsDataRefValueEntry().reference(ref);
                CmsDataFiller.fillCmsData(entry.value, value);
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
