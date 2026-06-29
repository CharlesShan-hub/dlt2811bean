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

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ConnectTlsHandler implements CommandHandler {

    @Override
    public String name() { return "connect-tls"; }

    @Override
    public String description() { return "TLS 连接 CMS 服务器（默认端口 9102）；可附带 negotiate 参数"; }

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
        int port = CmsConfigLoader.load().getServer().getSslPort();
        String sapRef = args.get("sapRef");

        ConsolePrinter.info("TLS connecting to " + host + ":" + port + " ...");

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, new X509TrustManager[]{
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }
        }, new SecureRandom());

        console.connectTls(host, port, sslContext);

        // 只给了 host → 纯 connect，不做 negotiate/associate
        if (sapRef == null || sapRef.isEmpty()) {
            ConsolePrinter.success("TLS connected: " + host + ":" + port);
            return;
        }

        ConsolePrinter.info("TLS connected, negotiating parameters ...");

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
            .execute(new AssociateClientDao().sapRef(sapRef).secure(true));

        ConsolePrinter.success("TLS associated: " + sapRef);
    }
}
