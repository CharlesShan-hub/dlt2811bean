package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetRpcMethodDefinitionConsole extends CommandHandler<GetRpcMethodDefinitionDao, GetRpcMethodDefinitionClient> {

    public GetRpcMethodDefinitionConsole() {
        super(CommandInfo.RPC_METHOD_DEF);
        Param p = Param.of("refs", null, "refs", List.class, true);
        param(p, "方法引用列表（空格分隔），如 \"SystemInfo.getServerVersion\"");
    }
}
