package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcMethodDefinitionConsole extends CommandHandler {
    public GetRpcMethodDefinitionConsole() {
        super(CommandInfo.RPC_METHOD_DEF);
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("refs", "方法引用列表", null), new Param("json", "JSON 格式输出", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "refs", "Usage: rpc-method-def --refs \"<ref1> <ref2>...\""))
            return;
        List<String> refs = Arrays.asList(args.get("refs").trim().split("\\s+"));
        ConsolePrinter.info("Fetching RPC method definition for " + refs.size() + " ref(s)");
        GetRpcMethodDefinitionDao dao = new GetRpcMethodDefinitionDao().refs(refs);
        console.getClient(GetRpcMethodDefinitionClient.class).execute(dao);
        CmsConsole.outputMessage("RPC method definition fetched", args);
    }
}
