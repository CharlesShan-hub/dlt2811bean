package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.base.BaseDao;
import com.ysh.jcms.app.handler.base.BaseHandler;
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

public class ConnectHandler extends CommandHandler<BaseDao, BaseClientHandler<BaseDao>> {

    public ConnectHandler() {
        super(CommandInfo.CONNECT);
        Param p1 = Param.of("ip", "127.0.0.1", null, String.class, false);
        param(p1, "服务器地址（默认 127.0.0.1）");
        Param p2 = Param.of("port", null, null, String.class, false);
        param(p2, "服务器端口（默认 8102，TLS 默认 9102）");
        Param p3 = Param.of("ap", null, null, String.class, false);
        param(p3, "ServerAccessPoint 引用（如 C_B5041X/S1）");
        Param p4 = Param.of("secure", null, null, String.class, false);
        param(p4, "使用 TLS 加密连接（不传值，出现即启用）");
        Param p5 = Param.of("apsecure", null, null, String.class, false);
        param(p5, "应用层安全认证（不传值，出现即启用）");
        Param p6 = Param.of("apdu", null, null, String.class, false);
        param(p6, "APDU 大小");
        Param p7 = Param.of("asdu", null, null, String.class, false);
        param(p7, "ASDU 大小");
        Param p8 = Param.of("version", null, null, String.class, false);
        param(p8, "协议版本");
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (console.connected()) {
            CmsPrinter.error("Already connected. Type 'disconnect' first.");
            return;
        }

        String host = args.get("ip");
        boolean secure = "true".equals(args.get("secure"));
        // 应用层安全认证独立于 TLS：TLS 时保持原有行为（默认也带认证），也可单独开启
        boolean apSecure = "true".equals(args.get("apsecure"));
        int port;
        String portStr = args.get("port");
        if (portStr != null && !portStr.isEmpty()) {
            port = Integer.parseInt(portStr);
        } else {
            port = secure ? CmsConfigLoader.load().server().sslPort() : CmsConfigLoader.load().server().port();
        }
        String sapRef = args.get("ap");
        // "--ap" 作布尔 flag 时（未指定具体 AP），使用默认 AP
        if ("true".equals(sapRef)) {
            sapRef = null;
        }

        if (secure) {
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
            BaseHandler.traceSession("TLS Connected: " + host + ":" + port);
        } else {
            CmsPrinter.info("Connecting to " + host + ":" + port + " ...");
            console.connect(host, port);
            BaseHandler.traceSession("TCP Connected: " + host + ":" + port);
        }

        // 只给了 host → 纯 connect，不做 negotiate/associate
        if (sapRef == null || sapRef.isEmpty()) {
            String msg = (secure ? "TLS " : "") + "Connected: " + host + ":" + port;
            CmsPrinter.success(msg);
            return;
        }

        CmsPrinter.info("Connected, negotiating parameters ...");

        NegotiateClientDao negotiateDao = new NegotiateClientDao();
        String apduStr = args.get("apdu");
        String asduStr = args.get("asdu");
        String protoStr = args.get("version");
        if (apduStr != null && !apduStr.isEmpty())
            negotiateDao.apduSize(Integer.parseInt(apduStr));
        if (asduStr != null && !asduStr.isEmpty())
            negotiateDao.asduSize(Long.parseLong(asduStr));
        if (protoStr != null && !protoStr.isEmpty())
            negotiateDao.protocolVersion(Long.parseLong(protoStr));

        console.getClient(NegotiateClient.class).execute(negotiateDao);

        CmsPrinter.info("Negotiated, associating with " + sapRef + " ...");

        console.getClient(AssociateClient.class).execute(new AssociateDao().sapRef(sapRef).secure(secure || apSecure));

        String msg = (secure ? "TLS " : "") + "Associated: " + sapRef;
        CmsPrinter.success(msg);
    }
}
