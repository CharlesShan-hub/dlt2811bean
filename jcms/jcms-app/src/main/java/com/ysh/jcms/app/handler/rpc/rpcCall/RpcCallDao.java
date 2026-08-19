package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.core.data.choice.CmsRpcCallReqChoice;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.pdu.rpc.CmsRpcCallRequest;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class RpcCallDao extends BaseDao {

    /** Method reference, format "iface.method" */
    private String method;

    @Override
    public CmsType toRequest() {
        Objects.requireNonNull(method, "method must not be null");
        return new CmsRpcCallRequest()
            .method(method)
            .req(new CmsRpcCallReqChoice());
    }
}
