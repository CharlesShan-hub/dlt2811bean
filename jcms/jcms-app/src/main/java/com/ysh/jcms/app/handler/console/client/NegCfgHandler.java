package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handler for the {@code neg-cfg} command.
 *
 * <p>
 * 运行时查看/修改协商（negotiate）参数（对齐 ap-cfg 的内存开关模式，改完即生效）。 影响后续 connect 时的默认协商值；也可被
 * connect --apdu/--asdu/--version 显式覆盖。
 */
public class NegCfgHandler extends CommandHandler {

    public NegCfgHandler() {
        super(CommandInfo.NEG_CFG);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("apdu", "APDU 大小", ""), new Param("asdu", "ASDU 大小", ""), new Param("version", "协议版本", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = "true".equals(args.get("json"));
        CmsConfig.Protocol.Negotiate neg = CmsConfigLoader.load().getProtocol().getNegotiate();

        String apdu = args.get("apdu");
        String asdu = args.get("asdu");
        String version = args.get("version");
        if (apdu != null && !apdu.isEmpty())
            neg.setApduSize(Integer.parseInt(apdu));
        if (asdu != null && !asdu.isEmpty())
            neg.setAsduSize(Integer.parseInt(asdu));
        if (version != null && !version.isEmpty())
            neg.setProtocolVersion(Integer.parseInt(version));

        if (jsonMode) {
            String json = "{\"apduSize\":" + neg.getApduSize() + ",\"asduSize\":" + neg.getAsduSize() + ",\"protocolVersion\":"
                    + neg.getProtocolVersion() + ",\"modelVersion\":\"" + neg.getModelVersion() + "\"}";
            ConsolePrinter.raw("{\"success\":true,\"data\":" + json + "}");
            return;
        }

        ConsolePrinter.info("协商参数:");
        ConsolePrinter.info("  apduSize: " + neg.getApduSize());
        ConsolePrinter.info("  asduSize: " + neg.getAsduSize());
        ConsolePrinter.info("  protocolVersion: " + neg.getProtocolVersion());
        ConsolePrinter.info("  modelVersion: " + neg.getModelVersion());
    }
}
