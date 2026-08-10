package com.ysh.jcms.app.handler.rpc.getRpcInterfaceDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcInterfaceDirectoryConsole extends CommandHandler {
    public GetRpcInterfaceDirectoryConsole() {
        super(CommandInfo.RPC_IFACE_DIR);
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("after", "分页起始引用", ""), new Param("json", "JSON 格式输出", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        String after = args.get("after");
        ConsolePrinter.info("Fetching RPC interface directory" + (after != null ? " after " + after : ""));
        GetRpcInterfaceDirectoryDao dao = new GetRpcInterfaceDirectoryDao().after(after);
        console.getClient(GetRpcInterfaceDirectoryClient.class).execute(dao);
        ConsolePrinter.success("RPC interface directory fetched");
    }
}
