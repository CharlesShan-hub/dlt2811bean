package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetLcbValuesDao extends BaseDao {
    public GetLcbValuesDao() {
        paginationContext(new PaginationContext());
    }

    private final List<String> refs = new ArrayList<>();

    public GetLcbValuesDao addRef(String ref) {
        refs.add(ref);
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetLcbValuesRequest req = new CmsGetLcbValuesRequest();
        for (String ref : refs) {
            req.reference.add(new CmsObjectReference(ref));
        }
        return req;
    }
}
