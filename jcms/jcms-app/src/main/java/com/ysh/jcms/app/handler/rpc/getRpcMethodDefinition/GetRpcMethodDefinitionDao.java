package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.data.scalar.CmsString;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcMethodDefinitionRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcMethodDefinitionDao extends BaseDao {
    private List<String> refs;

    @Override
    public CmsType toRequest() {
        CmsGetRpcMethodDefinitionRequest req = new CmsGetRpcMethodDefinitionRequest();
        for (String ref : refs) {
            req.reference.add(new CmsString(ref));
        }
        return req;
    }
}
