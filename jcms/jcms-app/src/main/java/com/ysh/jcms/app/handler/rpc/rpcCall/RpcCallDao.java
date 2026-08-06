package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.handler.BaseDao;
import com.ysh.jcms.data.choice.CmsRpcCallReqChoice;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.rpc.CmsRpcCallRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class RpcCallDao extends BaseDao {
    private String method;

    @Override
    public CmsType toRequest() {
        CmsRpcCallRequest req = new CmsRpcCallRequest().method(method);
        req.req(new CmsRpcCallReqChoice());
        return req;
    }
}
