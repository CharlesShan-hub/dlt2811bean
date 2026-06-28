package com.ysh.jcms.app.handler.console.client;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateClientDao;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.test.test.TestClient;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirClient;
import com.ysh.jcms.app.handler.connection.release.ReleaseClient;
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
    public String description() { return "TLS 连接 CMS 服务器（默认端口 9102）"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("host", "服务器地址", "127.0.0.1"),
            new Param("sapRef", "ServerAccessPoint 引用", "E1Q1SB1/S1")
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

        console.registerClient(new NegotiateClient(console));
        console.registerClient(new AssociateClient(console));
        console.registerClient(new ReleaseClient(console));
        console.registerClient(new TestClient(console));
        console.registerClient(new SvrDirClient(console));
        console.registerClient(new LnDirClient(console));
        console.registerClient(new LdDirClient(console));

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, new X509TrustManager[]{
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }
        }, new SecureRandom());

        console.connectTls(host, port, sslContext);
        ConsolePrinter.info("TLS connected, associating with " + sapRef + " ...");

        console.getClient(AssociateClient.class)
            .execute(new AssociateClientDao().sapRef(sapRef).secure(true));

        ConsolePrinter.success("TLS associated: " + sapRef);
    }
}
