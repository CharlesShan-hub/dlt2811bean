package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.core.pdu.data.CmsGetDataDefinitionRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetDataDefinitionDao extends BaseDao {

    /** Data references, split and bound by {@link Param#convert(String)}. */
    private List<String> refs;

    /** Functional constraint filter, e.g. ST, MX. Default XX means no filter. */
    private String fc;

    @Override
    public CmsType toRequest() {
        CmsGetDataDefinitionRequest req = new CmsGetDataDefinitionRequest();
        if (refs != null) {
            boolean hasFc = fc != null && !fc.isEmpty() && !"XX".equalsIgnoreCase(fc);
            int fcCode = hasFc ? CmsFC.fromString(fc) : 0;
            for (String ref : refs) {
                if (ref == null || ref.isEmpty())
                    continue;
                CmsDataRefEntry entry = new CmsDataRefEntry().reference(ref);
                if (hasFc)
                    entry.fc(fcCode);
                req.data.add(entry);
            }
        }
        return req;
    }
}
