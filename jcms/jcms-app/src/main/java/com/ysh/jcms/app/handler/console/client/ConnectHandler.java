package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClientDao;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConnectHandler implements CommandHandler {

    @Override
    public String name() { return "connect"; }

    @Override
    public String description() { return "连接到 CMS 服务器（默认端口 8102）；可附带 negotiate 参数"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("host", "服务器地址", "127.0.0.1"),
            new Param("sapRef", "ServerAccessPoint 引用"),
            new Param("apduSize", "APDU 大小（可选，未传则使用默认值）"),
            new Param("asduSize", "ASDU 大小（可选，未传则使用默认值）"),
            new Param("protocolVersion", "协议版本（可选，未传则使用默认值）")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (console.isConnected()) {
            ConsolePrinter.error("Already connected. Type 'disconnect' first.");
            return;
        }

        String host = args.get("host");
        int port = CmsConfigLoader.load().getServer().getPort();
        String sapRef = args.get("sapRef");

        ConsolePrinter.info("Connecting to " + host + ":" + port + " ...");

        console.connect(host, port);

        // 只给了 host → 纯 connect，不做 negotiate/associate
        if (sapRef == null || sapRef.isEmpty()) {
            ConsolePrinter.success("Connected: " + host + ":" + port);
            return;
        }

        ConsolePrinter.info("Connected, negotiating parameters ...");

        NegotiateClientDao negotiateDao = new NegotiateClientDao();
        String apduStr = args.get("apduSize");
        String asduStr = args.get("asduSize");
        String protoStr = args.get("protocolVersion");
        if (apduStr != null && !apduStr.isEmpty()) negotiateDao.apduSize(Integer.parseInt(apduStr));
        if (asduStr != null && !asduStr.isEmpty()) negotiateDao.asduSize(Long.parseLong(asduStr));
        if (protoStr != null && !protoStr.isEmpty()) negotiateDao.protocolVersion(Long.parseLong(protoStr));

        console.getClient(NegotiateClient.class).execute(negotiateDao);

        ConsolePrinter.info("Negotiated, associating with " + sapRef + " ...");

        console.getClient(AssociateClient.class)
            .execute(new AssociateClientDao().sapRef(sapRef).secure(false));

        ConsolePrinter.success("Associated: " + sapRef);
    }
}
