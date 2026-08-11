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
        Param p2 = Param.of("delimiter", null, null, String.class, false);
        param(p2, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}
