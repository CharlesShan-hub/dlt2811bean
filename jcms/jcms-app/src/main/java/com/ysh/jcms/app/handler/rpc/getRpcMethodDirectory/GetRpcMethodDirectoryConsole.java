package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetRpcMethodDirectoryConsole extends CommandHandler<GetRpcMethodDirectoryDao, GetRpcMethodDirectoryClient> {

    public GetRpcMethodDirectoryConsole() {
        super(CommandInfo.RPC_METHOD_DIR);
        Param p1 = Param.of("interface", null, "iface", String.class, false);
        param(p1, "接口名称，不指定则返回所有接口的所有方法");
        Param p2 = Param.of("after", null, "after", String.class, false);
        param(p2, "分页起始引用，只返回该引用之后的方法");
    }
}
