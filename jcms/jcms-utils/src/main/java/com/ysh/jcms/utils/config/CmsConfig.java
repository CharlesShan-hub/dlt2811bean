package com.ysh.jcms.utils.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Accessors(fluent = true)
public class CmsConfig {

    private Server server = new Server();
    private Client client = new Client();
    private Protocol protocol = new Protocol();
    private Security security = new Security();
    private Scl scl = new Scl();

    // ───────── Server ─────────

    @Data
    @Accessors(fluent = true)
    public static class Server {
        private int port = 8102;
        private int sslPort = 9102;
        private List<String> testSclFiles = new ArrayList<>(Collections.singletonList("config/sample-scd-full.scd"));
        private List<String> sclFiles = new ArrayList<>();
        private KeepAlive keepalive = new KeepAlive();

        public String testSclFile() {
            return testSclFiles.isEmpty() ? null : testSclFiles.get(0);
        }

        public Server testSclFile(String sclFile) {
            this.testSclFiles = new ArrayList<>(Collections.singletonList(sclFile));
            return this;
        }

        public String getResolvedTestSclFile() {
            for (String path : testSclFiles) {
                if (Files.exists(Paths.get(path))) {
                    return path;
                }
            }
            return testSclFiles.isEmpty() ? null : testSclFiles.get(0);
        }

        public String sclFile() {
            return sclFiles.isEmpty() ? null : sclFiles.get(0);
        }

        public Server sclFile(String sclFile) {
            this.sclFiles = new ArrayList<>(Collections.singletonList(sclFile));
            return this;
        }

        public String getResolvedSclFile() {
            for (String path : sclFiles) {
                if (Files.exists(Paths.get(path))) {
                    return path;
                }
            }
            return null;
        }

        @Data
        @Accessors(fluent = true)
        public static class KeepAlive {
            private int idleTimeoutMs = 30000;
            private int retryIntervalMs = 5000;
            private int maxRetries = 4;
        }
    }

    // ───────── Client ─────────

    @Data
    @Accessors(fluent = true)
    public static class Client {
        private String defaultIedName = "E1Q1SB1";
        private String defaultAccessPoint = "S1";
        private boolean defaultSecure = false;
        private int connectTimeoutMs = 5000;
        private int requestTimeoutMs = 5000;
        private AccessPoint accessPoint = new AccessPoint();
        private Console console = new Console();

        @Data
        @Accessors(fluent = true)
        public static class AccessPoint {
            /** AP 来源: true=从 SCD 读, false=从 defaultAps 列表读 */
            private boolean fromScd = true;
            /** fromScd=false 时的静态 AP 引用列表（如 C_B5041X/S1） */
            private List<String> defaultAps = new ArrayList<>();
        }

        @Data
        @Accessors(fluent = true)
        public static class Console {
            private boolean tracePdu = false;
            private boolean sessionTrace = false;
            private String autoExec = "";
            private boolean showAutoExec = true;
            private boolean showConnectHint = true;
            private boolean apiEnabled = true;
            private int apiPort = 7899;
            private String apiHost = "http://127.0.0.1";
        }
    }

    // ───────── Protocol ─────────

    @Data
    @Accessors(fluent = true)
    public static class Protocol {
        private int maxArraySize = 1024;
        private boolean gbkToUtf8 = false;
        private Negotiate negotiate = new Negotiate();
        private File file = new File();
        private Log log = new Log();
        private Setting setting = new Setting();
        private Dataset dataset = new Dataset();

        @Data
        @Accessors(fluent = true)
        public static class Negotiate {
            private int apduSize = 65535;
            private int asduSize = 65531;
            private int protocolVersion = 1;
            private String modelVersion = "1.0";
        }

        @Data
        @Accessors(fluent = true)
        public static class File {
            private String rootPath = "config/files";
        }

        @Data
        @Accessors(fluent = true)
        public static class Log {
            private String rootPath = "config/logs";
        }

        @Data
        @Accessors(fluent = true)
        public static class Setting {
            private int numOfSG = 4;
            private boolean sgDefaultEnabled = true;
            private String sgDefaultName = "SG1";
        }

        @Data
        @Accessors(fluent = true)
        public static class Dataset {
            private boolean setDataSetPersistent = false;
        }
    }

    // ───────── Security ─────────

    @Data
    @Accessors(fluent = true)
    public static class Security {
        private boolean enabled = false;
        private long timeTolerance = 300;
        private Keystore keystore = new Keystore();
        private Truststore truststore = new Truststore();

        @Data
        @Accessors(fluent = true)
        public static class Keystore {
            private String path = "certs/server.pfx";
            private String password = "changeit";
        }

        @Data
        @Accessors(fluent = true)
        public static class Truststore {
            private String path = "certs/ca.cer";
            private String password = "changeit";
        }
    }

    // ───────── SCL ─────────

    @Data
    @Accessors(fluent = true)
    public static class Scl {
        /**
         * 国网 Q/GDW 1396 符合性校验模式: LOOSE=仅国际标准(默认), STRICT=国网严格模式.
         * <p>
         * LOOSE keeps the historical behaviour; STRICT runs the Q/GDW 1396
         * conformance check after loading the SCL file.
         */
        private String conformanceMode = "LOOSE";
    }

    // ───────── Merge ─────────

    public void merge(CmsConfig other) {
        if (other == null)
            return;
        if (other.server != null) {
            if (other.server.port != 8102)
                server.port = other.server.port;
            if (other.server.sslPort != 9102)
                server.sslPort = other.server.sslPort;
            if (other.server.testSclFiles() != null) {
                List<String> otherFiles = other.server.testSclFiles();
                if (otherFiles.size() > 1 || (otherFiles.size() == 1 && !"config/sample-scd-full.scd".equals(otherFiles.get(0)))) {
                    server.testSclFiles(new ArrayList<>(otherFiles));
                }
            }
            if (other.server.sclFiles() != null && !other.server.sclFiles().isEmpty()) {
                server.sclFiles(new ArrayList<>(other.server.sclFiles()));
            }
            if (other.server.keepalive != null) {
                if (other.server.keepalive.idleTimeoutMs != 30000)
                    server.keepalive.idleTimeoutMs = other.server.keepalive.idleTimeoutMs;
                if (other.server.keepalive.retryIntervalMs != 5000)
                    server.keepalive.retryIntervalMs = other.server.keepalive.retryIntervalMs;
                if (other.server.keepalive.maxRetries != 4)
                    server.keepalive.maxRetries = other.server.keepalive.maxRetries;
            }
        }
        if (other.client != null) {
            if (!other.client.defaultIedName.equals("E1Q1SB1"))
                client.defaultIedName = other.client.defaultIedName;
            if (!other.client.defaultAccessPoint.equals("S1"))
                client.defaultAccessPoint = other.client.defaultAccessPoint;
            if (other.client.connectTimeoutMs != 5000)
                client.connectTimeoutMs = other.client.connectTimeoutMs;
            if (other.client.requestTimeoutMs != 5000)
                client.requestTimeoutMs = other.client.requestTimeoutMs;
            if (other.client.accessPoint != null) {
                if (other.client.accessPoint.fromScd != true)
                    client.accessPoint.fromScd = other.client.accessPoint.fromScd;
                if (other.client.accessPoint.defaultAps != null && !other.client.accessPoint.defaultAps.isEmpty())
                    client.accessPoint.defaultAps = new ArrayList<>(other.client.accessPoint.defaultAps);
            }
        }
        if (other.protocol != null) {
            if (other.protocol.maxArraySize != 1024)
                protocol.maxArraySize = other.protocol.maxArraySize;
            if (other.protocol.negotiate != null) {
                if (other.protocol.negotiate.apduSize != 65535)
                    protocol.negotiate.apduSize = other.protocol.negotiate.apduSize;
                if (other.protocol.negotiate.asduSize != 65531)
                    protocol.negotiate.asduSize = other.protocol.negotiate.asduSize;
                if (other.protocol.negotiate.protocolVersion != 1)
                    protocol.negotiate.protocolVersion = other.protocol.negotiate.protocolVersion;
                if (other.protocol.negotiate.modelVersion != null && !other.protocol.negotiate.modelVersion.equals("1.0"))
                    protocol.negotiate.modelVersion = other.protocol.negotiate.modelVersion;
            }
            if (other.protocol.file != null) {
                if (other.protocol.file.rootPath != null && !other.protocol.file.rootPath.equals("config/files"))
                    protocol.file.rootPath = other.protocol.file.rootPath;
            }
            if (other.protocol.log != null) {
                if (other.protocol.log.rootPath != null && !other.protocol.log.rootPath.equals("config/logs"))
                    protocol.log.rootPath = other.protocol.log.rootPath;
            }
            if (other.protocol.setting != null) {
                if (other.protocol.setting.numOfSG != 4)
                    protocol.setting.numOfSG = other.protocol.setting.numOfSG;
                if (other.protocol.setting.sgDefaultEnabled)
                    protocol.setting.sgDefaultEnabled = other.protocol.setting.sgDefaultEnabled;
                if (other.protocol.setting.sgDefaultName != null && !other.protocol.setting.sgDefaultName.equals("SG1"))
                    protocol.setting.sgDefaultName = other.protocol.setting.sgDefaultName;
            }
        }
        if (other.security != null) {
            security.enabled = other.security.enabled;
        }
        if (other.scl != null) {
            if (other.scl.conformanceMode != null && !other.scl.conformanceMode.equals("LOOSE"))
                scl.conformanceMode = other.scl.conformanceMode;
        }
        if (other.client != null && other.client.console != null) {
            if (other.client.console.autoExec != null && !other.client.console.autoExec.isEmpty())
                client.console.autoExec = other.client.console.autoExec;
            if (other.client.console.apiPort != 7899)
                client.console.apiPort = other.client.console.apiPort;
            if (other.client.console.apiHost != null && !other.client.console.apiHost.equals("http://127.0.0.1"))
                client.console.apiHost = other.client.console.apiHost;
        }
    }
}
