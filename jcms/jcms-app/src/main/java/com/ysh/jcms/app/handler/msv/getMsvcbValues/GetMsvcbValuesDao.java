package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.util.CmsRequestHelper;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.msv.CmsGetMsvcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetMsvcbValuesDao extends BaseDao {
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        CmsGetMsvcbValuesRequest req = new CmsGetMsvcbValuesRequest();
        CmsRequestHelper.addAll(refs, req.reference, CmsObjectReference::new);
        return req;
    }
}
