package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.report.CmsGetBrcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetBrcbValuesDao extends BaseDao {
    private final List<String> refs = new ArrayList<>();

    public GetBrcbValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetBrcbValuesRequest req = new CmsGetBrcbValuesRequest();
        for (String ref : refs) {
            req.reference.add(new CmsObjectReference(ref));
        }
        return req;
    }
}
