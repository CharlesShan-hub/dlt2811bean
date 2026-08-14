package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateDao;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClientDao;
import com.ysh.jcms.utils.config.CmsConfigLoader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

public class ConnectTlsHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ConnectTlsHandler() {
        super(CommandInfo.CONNECT_TLS);
        Param p1 = Param.of("host", "127.0.0.1", null, String.class, false);
        param(p1, "服务器地址（默认 127.0.0.1）");
        Param p2 = Param.of("sap-ref", null, null, String.class, false);
        param(p2, "ServerAccessPoint 引用（如 C_B5041X/S1）");
        Param p3 = Param.of("apduSize", null, null, String.class, false);
        param(p3, "APDU 大小");
        Param p4 = Param.of("asduSize", null, null, String.class, false);
        param(p4, "ASDU 大小");
        Param p5 = Param.of("protocolVersion", null, null, String.class, false);
        param(p5, "协议版本");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (console.connected()) {
            CmsPrinter.error("Already connected. Type 'disconnect' first.");
            return;
        }

        String host = args.get("host");
        int port = CmsConfigLoader.load().server().sslPort();
        String sapRef = args.get("sap-ref");

        CmsPrinter.info("TLS connecting to " + host + ":" + port + " ...");

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());

        console.connectTls(host, port, sslContext);

        // 只给了 host → 纯 connect，不做 negotiate/associate
        if (sapRef == null || sapRef.isEmpty()) {
            CmsPrinter.success("TLS connected: " + host + ":" + port);
            return;
        }

        CmsPrinter.info("TLS connected, negotiating parameters ...");

        NegotiateClientDao negotiateDao = new NegotiateClientDao();
        String apduStr = args.get("apduSize");
        String asduStr = args.get("asduSize");
        String protoStr = args.get("protocolVersion");
        if (apduStr != null && !apduStr.isEmpty())
            negotiateDao.apduSize(Integer.parseInt(apduStr));
        if (asduStr != null && !asduStr.isEmpty())
            negotiateDao.asduSize(Long.parseLong(asduStr));
        if (protoStr != null && !protoStr.isEmpty())
            negotiateDao.protocolVersion(Long.parseLong(protoStr));

        console.getClient(NegotiateClient.class).execute(negotiateDao);

        CmsPrinter.info("Negotiated, associating with " + sapRef + " ...");

        console.getClient(AssociateClient.class).execute(new AssociateDao().sapRef(sapRef).secure(true));

        CmsPrinter.success("TLS associated: " + sapRef);
    }
}
