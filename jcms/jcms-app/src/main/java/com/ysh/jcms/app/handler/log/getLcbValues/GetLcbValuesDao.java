package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetLcbValuesDao extends BaseDao {
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        CmsGetLcbValuesRequest req = new CmsGetLcbValuesRequest();
        if (refs != null) {
            for (String ref : refs) {
                req.reference.add(new CmsObjectReference(ref));
            }
        }
        return req;
    }
}