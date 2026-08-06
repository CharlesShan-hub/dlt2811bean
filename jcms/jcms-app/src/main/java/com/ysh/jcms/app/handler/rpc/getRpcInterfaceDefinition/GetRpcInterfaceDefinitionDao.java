package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.rpc.CmsGetRpcInterfaceDefinitionRequest;

public class GetRpcInterfaceDefinitionDao extends BaseDao {
    private String iface;
    private String after;

    public String iface() {
        return iface;
    }
    public GetRpcInterfaceDefinitionDao iface(String v) {
        this.iface = v;
        return this;
    }
    public String after() {
        return after;
    }
    public GetRpcInterfaceDefinitionDao after(String v) {
        this.after = v;
        return this;
    }

    @Override
    public CmsType toRequest() {
        CmsGetRpcInterfaceDefinitionRequest req = new CmsGetRpcInterfaceDefinitionRequest().interfaceName(iface);
        if (after != null && !after.isEmpty())
            req.referenceAfter(after);
        return req;
    }
}
