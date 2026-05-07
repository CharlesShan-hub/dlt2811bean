package com.ysh.dlt2811bean.config;

import java.util.List;

public class CmsConfig {

    private Server server = new Server();
    private Client client = new Client();
    private KeepAlive keepalive = new KeepAlive();
    private Security security = new Security();
    private Protocol protocol = new Protocol();
    private Negotiate negotiate = new Negotiate();
    private File file = new File();

    public Server getServer() { return server; }
    public void setServer(Server server) { this.server = server; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public KeepAlive getKeepalive() { return keepalive; }
    public void setKeepalive(KeepAlive keepalive) { this.keepalive = keepalive; }

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    public Protocol getProtocol() { return protocol; }
    public void setProtocol(Protocol protocol) { this.protocol = protocol; }

    public Negotiate getNegotiate() { return negotiate; }
    public void setNegotiate(Negotiate negotiate) { this.negotiate = negotiate; }

    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }

    public static class Server {
        private int port = 8102;
        private int sslPort = 9102;
        private String sclFile = "config/sample-scd-full.scd";

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public int getSslPort() { return sslPort; }
        public void setSslPort(int sslPort) { this.sslPort = sslPort; }
        public String getSclFile() { return sclFile; }
        public void setSclFile(String sclFile) { this.sclFile = sclFile; }
    }

    public static class Client {
        private String defaultAccessPoint = "E1Q1SB1";
        private String defaultEp = "S1";
        private int connectTimeoutMs = 5000;
        private int requestTimeoutMs = 5000;

        public String getDefaultAccessPoint() { return defaultAccessPoint; }
        public void setDefaultAccessPoint(String defaultAccessPoint) { this.defaultAccessPoint = defaultAccessPoint; }
        public String getDefaultEp() { return defaultEp; }
        public void setDefaultEp(String defaultEp) { this.defaultEp = defaultEp; }
        public int getConnectTimeoutMs() { return connectTimeoutMs; }
        public void setConnectTimeoutMs(int connectTimeoutMs) { this.connectTimeoutMs = connectTimeoutMs; }
        public int getRequestTimeoutMs() { return requestTimeoutMs; }
        public void setRequestTimeoutMs(int requestTimeoutMs) { this.requestTimeoutMs = requestTimeoutMs; }
    }

    public static class KeepAlive {
        private int idleTimeoutMs = 30000;
        private int retryIntervalMs = 5000;
        private int maxRetries = 4;

        public int getIdleTimeoutMs() { return idleTimeoutMs; }
        public void setIdleTimeoutMs(int idleTimeoutMs) { this.idleTimeoutMs = idleTimeoutMs; }
        public int getRetryIntervalMs() { return retryIntervalMs; }
        public void setRetryIntervalMs(int retryIntervalMs) { this.retryIntervalMs = retryIntervalMs; }
        public int getMaxRetries() { return maxRetries; }
        public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    }

    public static class Security {
        private boolean enabled = false;
        private Keystore keystore = new Keystore();
        private Truststore truststore = new Truststore();

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public Keystore getKeystore() { return keystore; }
        public void setKeystore(Keystore keystore) { this.keystore = keystore; }
        public Truststore getTruststore() { return truststore; }
        public void setTruststore(Truststore truststore) { this.truststore = truststore; }

        public static class Keystore {
            private String path = "certs/server.pfx";
            private String password = "changeit";

            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }
            public String getPassword() { return password; }
            public void setPassword(String password) { this.password = password; }
        }

        public static class Truststore {
            private String path = "certs/ca.cer";
            private String password = "changeit";

            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }
            public String getPassword() { return password; }
            public void setPassword(String password) { this.password = password; }
        }
    }

    public static class Protocol {
        private int pi = 0x01;
        private int maxAsduSize = 65531;

        public int getPi() { return pi; }
        public void setPi(int pi) { this.pi = pi; }
        public int getMaxAsduSize() { return maxAsduSize; }
        public void setMaxAsduSize(int maxAsduSize) { this.maxAsduSize = maxAsduSize; }
    }

    public static class Negotiate {
        private int apduSize = 65535;
        private int asduSize = 65531;
        private int protocolVersion = 1;
        private String modelVersion = "1.0";

        public int getApduSize() { return apduSize; }
        public void setApduSize(int apduSize) { this.apduSize = apduSize; }
        public int getAsduSize() { return asduSize; }
        public void setAsduSize(int asduSize) { this.asduSize = asduSize; }
        public int getProtocolVersion() { return protocolVersion; }
        public void setProtocolVersion(int protocolVersion) { this.protocolVersion = protocolVersion; }
        public String getModelVersion() { return modelVersion; }
        public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    }

    public static class File {
        private String rootPath = "config/files";

        public String getRootPath() { return rootPath; }
        public void setRootPath(String rootPath) { this.rootPath = rootPath; }
    }

    public void merge(CmsConfig other) {
        if (other == null) return;
        if (other.server != null) {
            if (other.server.port != 8102) server.port = other.server.port;
            if (other.server.sslPort != 9102) server.sslPort = other.server.sslPort;
            if (other.server.sclFile != null && !other.server.sclFile.equals("config/sample-scd-full.scd"))
                server.sclFile = other.server.sclFile;
        }
        if (other.client != null) {
            if (other.client.connectTimeoutMs != 5000) client.connectTimeoutMs = other.client.connectTimeoutMs;
            if (other.client.requestTimeoutMs != 5000) client.requestTimeoutMs = other.client.requestTimeoutMs;
        }
        if (other.keepalive != null) {
            if (other.keepalive.idleTimeoutMs != 30000) keepalive.idleTimeoutMs = other.keepalive.idleTimeoutMs;
            if (other.keepalive.retryIntervalMs != 5000) keepalive.retryIntervalMs = other.keepalive.retryIntervalMs;
            if (other.keepalive.maxRetries != 4) keepalive.maxRetries = other.keepalive.maxRetries;
        }
        if (other.security != null) {
            security.enabled = other.security.enabled;
        }
        if (other.protocol != null) {
            if (other.protocol.pi != 0x01) protocol.pi = other.protocol.pi;
            if (other.protocol.maxAsduSize != 65531) protocol.maxAsduSize = other.protocol.maxAsduSize;
        }
        if (other.negotiate != null) {
            if (other.negotiate.apduSize != 65535) negotiate.apduSize = other.negotiate.apduSize;
            if (other.negotiate.asduSize != 65531) negotiate.asduSize = other.negotiate.asduSize;
            if (other.negotiate.protocolVersion != 1) negotiate.protocolVersion = other.negotiate.protocolVersion;
            if (other.negotiate.modelVersion != null && !other.negotiate.modelVersion.equals("1.0"))
                negotiate.modelVersion = other.negotiate.modelVersion;
        }
        if (other.file != null) {
            if (other.file.rootPath != null && !other.file.rootPath.equals("config/files"))
                file.rootPath = other.file.rootPath;
        }
    }
}
