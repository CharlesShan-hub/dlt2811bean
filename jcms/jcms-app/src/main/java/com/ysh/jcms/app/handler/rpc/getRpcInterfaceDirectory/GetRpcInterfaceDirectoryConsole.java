package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetRpcInterfaceDirectoryConsole extends CommandHandler<GetRpcInterfaceDirectoryDao, GetRpcInterfaceDirectoryClient> {

    public GetRpcInterfaceDirectoryConsole() {
        super(CommandInfo.RPC_IFACE_DIR);
        Param p = Param.of("after", null, "after", String.class, false);
        param(p, "分页起始引用，只返回该引用之后的接口");
    }
}
