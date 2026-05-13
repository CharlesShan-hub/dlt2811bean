package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.transport.app.CmsClient;

public class ConnectTlsHandler extends AbstractConnectHandler {

    public ConnectTlsHandler(CliContext ctx) {
        super(ctx);
    }

    protected String commandName() {
        return "connect-tls";
    }

    protected String commandDescription() {
        return "TLS 连接服务器（自动协商与关联）";
    }

    protected int defaultPort() {
        return config().getServer().getSslPort();
    }

    protected String connectionSuffix() {
        return " (TLS)";
    }

    protected void doConnect(CmsClient client, String host, int port) throws Exception {
        CmsConfig config = CmsConfigLoader.load();
        GmSslContext sslContext = GmSslContext.forClient()
                .keyStore(config.getSecurity().getKeystore().getPath(), config.getSecurity().getKeystore().getPassword())
                .trustManager(new javax.net.ssl.X509TrustManager[]{
                    new javax.net.ssl.X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[0];
                        }
                    }
                })
                .useStandardTls()
                .build();
        client.sslContext(sslContext);
        client.connectTls(host, port);
    }
}
