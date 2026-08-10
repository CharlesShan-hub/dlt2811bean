package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.goose.CmsGetGoCbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetGoCbValuesDao extends BaseDao {
    public GetGoCbValuesDao() {
        paginationContext(new PaginationContext());
    }

    private final List<String> refs = new ArrayList<>();

    public GetGoCbValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetGoCbValuesRequest req = new CmsGetGoCbValuesRequest();
        for (String ref : refs) {
            req.reference.add(new CmsObjectReference(ref));
        }
        return req;
    }
}
