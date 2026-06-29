package com.ysh.jcms.utils.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmsConfig {

    private Server server = new Server();
    private Client client = new Client();
    private Protocol protocol = new Protocol();
    private Security security = new Security();

    public Server getServer() { return server; }
    public void setServer(Server server) { this.server = server; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Protocol getProtocol() { return protocol; }
    public void setProtocol(Protocol protocol) { this.protocol = protocol; }

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }

    // ───────── Server ─────────

    public static class Server {
        private int port = 8102;
        private int sslPort = 9102;
        private List<String> testSclFiles = new ArrayList<>(Collections.singletonList("config/sample-scd-full.scd"));
        private List<String> sclFiles = new ArrayList<>();
        private KeepAlive keepalive = new KeepAlive();

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public int getSslPort() { return sslPort; }
        public void setSslPort(int sslPort) { this.sslPort = sslPort; }
        public KeepAlive getKeepalive() { return keepalive; }
        public void setKeepalive(KeepAlive keepalive) { this.keepalive = keepalive; }

        // ── testSclFiles ──

        public String getTestSclFile() {
            return testSclFiles.isEmpty() ? null : testSclFiles.get(0);
        }

        public void setTestSclFile(String sclFile) {
            this.testSclFiles = new ArrayList<>(Collections.singletonList(sclFile));
        }

        public List<String> getTestSclFiles() { return testSclFiles; }

        public void setTestSclFiles(List<String> testSclFiles) {
            this.testSclFiles = testSclFiles;
        }

        public String getResolvedTestSclFile() {
            for (String path : testSclFiles) {
                if (Files.exists(Paths.get(path))) {
                    return path;
                }
            }
            return testSclFiles.isEmpty() ? null : testSclFiles.get(0);
        }

        // ── sclFiles ──

        public String getSclFile() {
            return sclFiles.isEmpty() ? null : sclFiles.get(0);
        }

        public void setSclFile(String sclFile) {
            this.sclFiles = new ArrayList<>(Collections.singletonList(sclFile));
        }

        public List<String> getSclFiles() { return sclFiles; }

        public void setSclFiles(List<String> sclFiles) {
            this.sclFiles = sclFiles;
        }

        public String getResolvedSclFile() {
            for (String path : sclFiles) {
                if (Files.exists(Paths.get(path))) {
                    return path;
                }
            }
            return null;
        }

        // ── KeepAlive (server-side heartbeat) ──

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
    }

    // ───────── Client ─────────

    public static class Client {
        private String defaultIedName = "E1Q1SB1";
        private String defaultAccessPoint = "S1";
        private boolean defaultSecure = false;
        private int connectTimeoutMs = 5000;
        private int requestTimeoutMs = 5000;
        private Console console = new Console();

        public String getDefaultIedName() { return defaultIedName; }
        public void setDefaultIedName(String defaultIedName) { this.defaultIedName = defaultIedName; }
        public String getDefaultAccessPoint() { return defaultAccessPoint; }
        public void setDefaultAccessPoint(String defaultAccessPoint) { this.defaultAccessPoint = defaultAccessPoint; }
        public boolean isDefaultSecure() { return defaultSecure; }
        public void setDefaultSecure(boolean defaultSecure) { this.defaultSecure = defaultSecure; }
        public int getConnectTimeoutMs() { return connectTimeoutMs; }
        public void setConnectTimeoutMs(int connectTimeoutMs) { this.connectTimeoutMs = connectTimeoutMs; }
        public int getRequestTimeoutMs() { return requestTimeoutMs; }
        public void setRequestTimeoutMs(int requestTimeoutMs) { this.requestTimeoutMs = requestTimeoutMs; }
        public Console getConsole() { return console; }
        public void setConsole(Console console) { this.console = console; }

        // ── Console settings ──

        public static class Console {
            private boolean tracePdu = false;
            private String autoExec = "";
            private boolean showAutoExec = true;
            private boolean showConnectHint = true;
            private boolean apiEnabled = true;
            private int apiPort = 7899;

            public boolean isTracePdu() { return tracePdu; }
            public void setTracePdu(boolean tracePdu) { this.tracePdu = tracePdu; }
            public String getAutoExec() { return autoExec; }
            public void setAutoExec(String autoExec) { this.autoExec = autoExec; }
            public boolean isShowAutoExec() { return showAutoExec; }
            public void setShowAutoExec(boolean showAutoExec) { this.showAutoExec = showAutoExec; }
            public boolean isShowConnectHint() { return showConnectHint; }
            public void setShowConnectHint(boolean showConnectHint) { this.showConnectHint = showConnectHint; }
            public boolean isApiEnabled() { return apiEnabled; }
            public void setApiEnabled(boolean apiEnabled) { this.apiEnabled = apiEnabled; }
            public int getApiPort() { return apiPort; }
            public void setApiPort(int apiPort) { this.apiPort = apiPort; }
        }
    }

    // ───────── Protocol ─────────

    public static class Protocol {
        private int maxArraySize = 1024;
        private boolean gbkToUtf8 = false;
        private Negotiate negotiate = new Negotiate();
        private File file = new File();
        private Setting setting = new Setting();

        public int getMaxArraySize() { return maxArraySize; }
        public void setMaxArraySize(int maxArraySize) { this.maxArraySize = maxArraySize; }
        public boolean isGbkToUtf8() { return gbkToUtf8; }
        public void setGbkToUtf8(boolean gbkToUtf8) { this.gbkToUtf8 = gbkToUtf8; }
        public Negotiate getNegotiate() { return negotiate; }
        public void setNegotiate(Negotiate negotiate) { this.negotiate = negotiate; }
        public File getFile() { return file; }
        public void setFile(File file) { this.file = file; }
        public Setting getSetting() { return setting; }
        public void setSetting(Setting setting) { this.setting = setting; }

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

        public static class Setting {
            private int numOfSG = 4;
            private boolean sgDefaultEnabled = true;
            private String sgDefaultName = "SG1";

            public int getNumOfSG() { return numOfSG; }
            public void setNumOfSG(int numOfSG) { this.numOfSG = numOfSG; }
            public boolean isSgDefaultEnabled() { return sgDefaultEnabled; }
            public void setSgDefaultEnabled(boolean sgDefaultEnabled) { this.sgDefaultEnabled = sgDefaultEnabled; }
            public String getSgDefaultName() { return sgDefaultName; }
            public void setSgDefaultName(String sgDefaultName) { this.sgDefaultName = sgDefaultName; }
        }
    }

    // ───────── Security ─────────

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

    // ───────── Merge ─────────

    public void merge(CmsConfig other) {
        if (other == null) return;
        if (other.server != null) {
            if (other.server.port != 8102) server.port = other.server.port;
            if (other.server.sslPort != 9102) server.sslPort = other.server.sslPort;
            if (other.server.getTestSclFiles() != null) {
                List<String> otherFiles = other.server.getTestSclFiles();
                if (otherFiles.size() > 1 || (otherFiles.size() == 1 && !"config/sample-scd-full.scd".equals(otherFiles.get(0)))) {
                    server.setTestSclFiles(new ArrayList<>(otherFiles));
                }
            }
            if (other.server.getSclFiles() != null && !other.server.getSclFiles().isEmpty()) {
                server.setSclFiles(new ArrayList<>(other.server.getSclFiles()));
            }
            if (other.server.keepalive != null) {
                if (other.server.keepalive.idleTimeoutMs != 30000) server.keepalive.idleTimeoutMs = other.server.keepalive.idleTimeoutMs;
                if (other.server.keepalive.retryIntervalMs != 5000) server.keepalive.retryIntervalMs = other.server.keepalive.retryIntervalMs;
                if (other.server.keepalive.maxRetries != 4) server.keepalive.maxRetries = other.server.keepalive.maxRetries;
            }
        }
        if (other.client != null) {
            if (!other.client.defaultIedName.equals("E1Q1SB1")) client.defaultIedName = other.client.defaultIedName;
            if (!other.client.defaultAccessPoint.equals("S1")) client.defaultAccessPoint = other.client.defaultAccessPoint;
            if (other.client.connectTimeoutMs != 5000) client.connectTimeoutMs = other.client.connectTimeoutMs;
            if (other.client.requestTimeoutMs != 5000) client.requestTimeoutMs = other.client.requestTimeoutMs;
        }
        if (other.protocol != null) {
            if (other.protocol.maxArraySize != 1024) protocol.maxArraySize = other.protocol.maxArraySize;
            if (other.protocol.negotiate != null) {
                if (other.protocol.negotiate.apduSize != 65535) protocol.negotiate.apduSize = other.protocol.negotiate.apduSize;
                if (other.protocol.negotiate.asduSize != 65531) protocol.negotiate.asduSize = other.protocol.negotiate.asduSize;
                if (other.protocol.negotiate.protocolVersion != 1) protocol.negotiate.protocolVersion = other.protocol.negotiate.protocolVersion;
                if (other.protocol.negotiate.modelVersion != null && !other.protocol.negotiate.modelVersion.equals("1.0"))
                    protocol.negotiate.modelVersion = other.protocol.negotiate.modelVersion;
            }
            if (other.protocol.file != null) {
                if (other.protocol.file.rootPath != null && !other.protocol.file.rootPath.equals("config/files"))
                    protocol.file.rootPath = other.protocol.file.rootPath;
            }
            if (other.protocol.setting != null) {
                if (other.protocol.setting.numOfSG != 4) protocol.setting.numOfSG = other.protocol.setting.numOfSG;
                if (other.protocol.setting.sgDefaultEnabled) protocol.setting.sgDefaultEnabled = other.protocol.setting.sgDefaultEnabled;
                if (other.protocol.setting.sgDefaultName != null && !other.protocol.setting.sgDefaultName.equals("SG1"))
                    protocol.setting.sgDefaultName = other.protocol.setting.sgDefaultName;
            }
        }
        if (other.security != null) {
            security.enabled = other.security.enabled;
        }
        if (other.client != null && other.client.console != null) {
            if (other.client.console.autoExec != null && !other.client.console.autoExec.isEmpty())
                client.console.autoExec = other.client.console.autoExec;
            if (other.client.console.apiPort != 7899)
                client.console.apiPort = other.client.console.apiPort;
        }
    }
}
