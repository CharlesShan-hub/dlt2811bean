package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesRequest;
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
        addAll(refs, req.reference, CmsObjectReference::new);
        return req;
    }
}
