package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDefinition;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcInterfaceDefinitionConsole extends CommandHandler {
    public GetRpcInterfaceDefinitionConsole() {
        super(CommandInfo.RPC_IFACE_DEF);
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("interface", "接口名称", null), new Param("after", "分页起始引用", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "interface", "Usage: rpc-iface-def --interface <name>"))
            return;
        String iface = args.get("interface").trim();
        String after = args.get("after");
        ConsolePrinter.info("Fetching RPC interface definition: " + iface);
        GetRpcInterfaceDefinitionDao dao = new GetRpcInterfaceDefinitionDao().iface(iface).after(after);
        console.getClient(GetRpcInterfaceDefinitionClient.class).execute(dao);
        ConsolePrinter.success("RPC interface definition fetched for " + iface);
    }
}
