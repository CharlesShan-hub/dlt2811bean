package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcInterfaceDirectoryConsole implements CommandHandler {
    @Override
    public String name() {
        return "rpc-iface-dir";
    }
    @Override
    public String description() {
        return "读RPC接口目录 (GetRpcInterfaceDirectory)。用法: rpc-iface-dir [--after <ref>] [--json]";
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("after", "分页起始引用", ""), new Param("json", "JSON 格式输出", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        String after = args.get("after");
        ConsolePrinter.info("Fetching RPC interface directory" + (after != null ? " after " + after : ""));
        console.getClient(GetRpcInterfaceDirectoryClient.class).execute(after);
        CmsConsole.outputMessage("RPC interface directory fetched", args);
    }
}
