package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.report.CmsGetUrcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetUrcbValuesDao extends BaseDao {
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        CmsGetUrcbValuesRequest req = new CmsGetUrcbValuesRequest();
        addAll(refs, req.reference, CmsObjectReference::new);
        return req;
    }
}
