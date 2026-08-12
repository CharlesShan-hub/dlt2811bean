package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.log.CmsGetLogStatusValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetLogStatusValuesDao extends BaseDao {
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        CmsGetLogStatusValuesRequest req = new CmsGetLogStatusValuesRequest();
        if (refs != null) {
            for (String ref : refs) {
                req.logReference.add(new CmsObjectReference(ref));
            }
        }
        return req;
    }
}
