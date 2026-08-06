package com.ysh.jcms.app.handler.rpc.rpcCall;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RpcCallConsole extends CommandHandler {
    public RpcCallConsole() {
        super(CommandInfo.RPC_CALL);
    }
    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("method", "方法引用（接口名.方法名）", null), new Param("json", "JSON 格式输出", ""));
    }
    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        if (!CmsConsole.requireParam(args, "method", "Usage: rpc-call --method <接口名.方法名>"))
            return;
        String method = args.get("method").trim();
        ConsolePrinter.info("RPC call: " + method);
        RpcCallDao dao = new RpcCallDao().method(method);
        console.getClient(RpcCallClient.class).execute(dao);
        CmsConsole.outputMessage("RPC call " + method + " completed", args);
    }
}
