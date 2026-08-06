package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetUrcbValuesDao extends BaseDao {
    private final List<String> refs = new ArrayList<>();

    public GetUrcbValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetUrcbValuesRequest req = new CmsGetUrcbValuesRequest();
        for (String ref : refs) {
            req.reference.add(new CmsObjectReference(ref));
        }
        return req;
    }
}
