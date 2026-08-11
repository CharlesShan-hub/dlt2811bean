package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetRpcInterfaceDefinitionConsole extends CommandHandler<GetRpcInterfaceDefinitionDao, GetRpcInterfaceDefinitionClient> {

    public GetRpcInterfaceDefinitionConsole() {
        super(CommandInfo.RPC_IFACE_DEF);
        Param p1 = Param.of("interface", null, "iface", String.class, true);
        param(p1, "接口名称");
        Param p2 = Param.of("after", null, "after", String.class, false);
        param(p2, "分页起始引用，只返回该引用之后的方法定义");
    }
}
