package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.rpc.CmsGetRpcInterfaceDefinitionRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class GetRpcInterfaceDefinitionDao extends BaseDao {
    private String iface;
    private String after;

    @Override
    public CmsType toRequest() {
        CmsGetRpcInterfaceDefinitionRequest req = new CmsGetRpcInterfaceDefinitionRequest().interfaceName(iface);
        if (after != null && !after.isEmpty())
            req.referenceAfter(after);
        return req;
    }
}
