package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcMethodDirectoryConsole implements CommandHandler {
    @Override
    public String name() {
        return "rpc-method-dir";
    }
    @Override
    public String description() {
        return "读RPC方法目录 (GetRpcMethodDirectory)。用法: rpc-method-dir [--interface <name>] [--after <ref>] [--json]";
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("interface", "接口名称", ""), new Param("after", "分页起始引用", ""), new Param("json", "JSON 格式输出", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        String iface = args.get("interface");
        String after = args.get("after");
        ConsolePrinter.info("Fetching RPC method directory" + (iface != null ? " for interface " + iface : ""));
        GetRpcMethodDirectoryDao dao = new GetRpcMethodDirectoryDao().iface(iface).after(after);
        console.getClient(GetRpcMethodDirectoryClient.class).execute(dao);
        CmsConsole.outputMessage("RPC method directory fetched", args);
    }
}
