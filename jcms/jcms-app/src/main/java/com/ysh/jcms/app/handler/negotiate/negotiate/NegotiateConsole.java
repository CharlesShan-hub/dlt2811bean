package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NegotiateConsole implements CommandHandler {

    @Override
    public String name() { return "negotiate"; }

    @Override
    public String description() { return "协商参数 (Negotiate) — 无参使用默认值，或指定 apduSize asduSize protocolVersion"; }

    @Override
    public List<Param> params() {
        com.ysh.jcms.utils.config.CmsConfig.Negotiate cfg = CmsConfigLoader.load().getNegotiate();
        return Arrays.asList(
            new Param("apduSize", "APDU 大小", String.valueOf(cfg.getApduSize())),
            new Param("asduSize", "ASDU 大小", String.valueOf(cfg.getAsduSize())),
            new Param("protocolVersion", "协议版本", String.valueOf(cfg.getProtocolVersion()))
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) { ConsolePrinter.error("Not connected."); return; }

        NegotiateClientDao dao = new NegotiateClientDao();
        String apduStr = args.get("apduSize");
        String asduStr = args.get("asduSize");
        String protoStr = args.get("protocolVersion");
        if (apduStr != null && !apduStr.isEmpty()) dao.apduSize(Integer.parseInt(apduStr));
        if (asduStr != null && !asduStr.isEmpty()) dao.asduSize(Long.parseLong(asduStr));
        if (protoStr != null && !protoStr.isEmpty()) dao.protocolVersion(Long.parseLong(protoStr));

        console.getClient(NegotiateClient.class).execute(dao);
        ConsolePrinter.success("Negotiate completed.");
    }
}
