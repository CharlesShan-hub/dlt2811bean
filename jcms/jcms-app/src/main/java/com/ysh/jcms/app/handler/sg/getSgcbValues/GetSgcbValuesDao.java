package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.sg.CmsGetSgcbValuesRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetSgcbValuesDao extends BaseDao {

    private List<String> refs = new ArrayList<>();

    @Override
    public CmsType toRequest() {
        CmsGetSgcbValuesRequest req = new CmsGetSgcbValuesRequest();
        for (String ref : refs) {
            req.sgcbReference.add(new CmsObjectReference(ref));
        }
        return req;
    }
}
