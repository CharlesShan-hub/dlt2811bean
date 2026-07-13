package com.ysh.jcms.app.handler.rpc.getRpcMethodDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcMethodDefinitionConsole implements CommandHandler {
    @Override
    public String name() {
        return "rpc-method-def";
    }
    @Override
    public String description() {
        return "读RPC方法定义 (GetRpcMethodDefinition)。用法: rpc-method-def --refs \"<ref1> <ref2>...\" [--json]";
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
        console.getClient(GetRpcMethodDefinitionClient.class).execute(refs);
        CmsConsole.outputMessage("RPC method definition fetched", args);
    }
}
