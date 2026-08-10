package com.ysh.jcms.app.handler.rpc.getRpcMethodDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetRpcMethodDirectoryConsole extends CommandHandler {
    public GetRpcMethodDirectoryConsole() {
        super(CommandInfo.RPC_METHOD_DIR);
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("interface", "接口名称", ""), new Param("after", "分页起始引用", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        String iface = args.get("interface");
        String after = args.get("after");
        ConsolePrinter.info("Fetching RPC method directory" + (iface != null ? " for interface " + iface : ""));
        GetRpcMethodDirectoryDao dao = new GetRpcMethodDirectoryDao().iface(iface).after(after);
        console.getClient(GetRpcMethodDirectoryClient.class).execute(dao);
        ConsolePrinter.success("RPC method directory fetched");
    }
}
