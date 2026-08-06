package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NegotiateConsole extends CommandHandler {

    public NegotiateConsole() {
        super(CommandInfo.NEGOTIATE);
    }

    @Override
    public List<Param> params() {
        com.ysh.jcms.utils.config.CmsConfig.Protocol.Negotiate cfg = CmsConfigLoader.load().protocol().negotiate();
        return Arrays.asList(new Param("apduSize", "APDU 大小", String.valueOf(cfg.apduSize())),
                new Param("asduSize", "ASDU 大小", String.valueOf(cfg.asduSize())),
                new Param("protocolVersion", "协议版本", String.valueOf(cfg.protocolVersion())), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        // negotiate 只需 TCP 连接，无需已关联（connect 不带 --ap 后即可手动协商）
        if (!console.requireTcpConnected(args))
            return;

        NegotiateClientDao dao = new NegotiateClientDao();
        // 兼容两套参数名：CLI 用 apduSize/asduSize/protocolVersion，webui/connect 用
        // apdu/asdu/version
        String apduStr = firstNonEmpty(args.get("apduSize"), args.get("apdu"));
        String asduStr = firstNonEmpty(args.get("asduSize"), args.get("asdu"));
        String protoStr = firstNonEmpty(args.get("protocolVersion"), args.get("version"));
        if (apduStr != null && !apduStr.isEmpty())
            dao.apduSize(Integer.parseInt(apduStr));
        if (asduStr != null && !asduStr.isEmpty())
            dao.asduSize(Long.parseLong(asduStr));
        if (protoStr != null && !protoStr.isEmpty())
            dao.protocolVersion(Long.parseLong(protoStr));

        console.getClient(NegotiateClient.class).execute(dao);
        CmsConsole.outputMessage("Negotiate completed.", args);
    }

    private static String firstNonEmpty(String a, String b) {
        return (a != null && !a.isEmpty()) ? a : b;
    }
}
