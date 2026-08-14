package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.util.CmsRequestHelper;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsObjectReference;
import com.ysh.jcms.core.pdu.goose.CmsGetGoCbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGoCbValuesDao extends BaseDao {
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        CmsGetGoCbValuesRequest req = new CmsGetGoCbValuesRequest();
        CmsRequestHelper.addAll(refs, req.reference, CmsObjectReference::new);
        return req;
    }
}
