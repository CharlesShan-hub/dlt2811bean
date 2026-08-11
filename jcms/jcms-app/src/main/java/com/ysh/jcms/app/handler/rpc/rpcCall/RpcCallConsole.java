package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class RpcCallConsole extends CommandHandler<RpcCallDao, RpcCallClient> {

    public RpcCallConsole() {
        super(CommandInfo.RPC_CALL);
        Param p = Param.of("method", null, "method", String.class, true);
        param(p, "方法引用（接口名.方法名），如 SystemInfo.getServerVersion");
    }
}
