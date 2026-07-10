package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NegotiateConsole implements CommandHandler {

    @Override
    public String name() {
        return "negotiate";
    }

    @Override
    public String description() {
        return "协商参数 (Negotiate) — 用法: negotiate [--apduSize N] [--asduSize N] [--protocolVersion N] [--json]";
    }

    @Override
    public List<Param> params() {
        com.ysh.jcms.utils.config.CmsConfig.Protocol.Negotiate cfg = CmsConfigLoader.load().getProtocol().getNegotiate();
        return Arrays.asList(new Param("apduSize", "APDU 大小", String.valueOf(cfg.getApduSize())),
                new Param("asduSize", "ASDU 大小", String.valueOf(cfg.getAsduSize())),
                new Param("protocolVersion", "协议版本", String.valueOf(cfg.getProtocolVersion())), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        NegotiateClientDao dao = new NegotiateClientDao();
        String apduStr = args.get("apduSize");
        String asduStr = args.get("asduSize");
        String protoStr = args.get("protocolVersion");
        if (apduStr != null && !apduStr.isEmpty())
            dao.apduSize(Integer.parseInt(apduStr));
        if (asduStr != null && !asduStr.isEmpty())
            dao.asduSize(Long.parseLong(asduStr));
        if (protoStr != null && !protoStr.isEmpty())
            dao.protocolVersion(Long.parseLong(protoStr));

        console.getClient(NegotiateClient.class).execute(dao);
        CmsConsole.outputMessage("Negotiate completed.", args);
    }
}
