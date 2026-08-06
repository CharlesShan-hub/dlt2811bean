package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.handler.BaseDao;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
public class RpcCallDao extends BaseDao {
    private String method;
}
