package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.transport.app.CmsServer;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.utils.CmsColor;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;

import java.nio.file.Paths;
import java.util.*;

public class CmsServerCli {

    private CmsServer server;
    private final LineReader reader;
    private boolean running = true;

    public CmsServerCli() {
        reader = LineReaderBuilder.builder()
                .completer(new StringsCompleter("help", "status", "list", "push", "exit", "quit"))
                .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".cms_server_history"))
                .build();
    }

    public static void main(String[] args) throws Exception {
        new CmsServerCli().run();
    }

    public void run() throws Exception {
        server = new CmsServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped");
        }));
        try {
            CmsConfig config = CmsConfigLoader.load();
            GmSslContext sslContext = GmSslContext.forServer()
                    .keyStore(config.getSecurity().getKeystore().getPath(), config.getSecurity().getKeystore().getPassword())
                    .trustManager(new javax.net.ssl.X509TrustManager[]{
                        new javax.net.ssl.X509TrustManager() {
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                        }
                    })
                    .useStandardTls()
                    .build();
            server.sslContext(sslContext);
        } catch (Exception e) {
            System.out.println(CmsColor.gray("  TLS not available: " + e.getMessage()));
        }
        server.start();
        System.out.println("CMS Server running on port " + server.getPort() + "...");
        System.out.println("Type 'help' for commands");

        while (running) {
            System.out.println();
            String line = reader.readLine("server> ").trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = line.split("\\s+");
                String cmd = parts[0].toLowerCase();

                switch (cmd) {
                    case "help" -> {
                        if (parts.length > 1) {
                            printCommandHelp(parts[1]);
                        } else {
                            printHelp();
                        }
                    }
                    case "exit", "quit" -> { server.stop(); running = false; System.exit(0); }
                    case "status" -> printStatus();
                    case "list" -> listSessions();
                    case "load-scl" -> handleLoadScl(parts);
                    case "reload-config" -> handleReloadConfig();
                    case "push" -> handlePush(parts);
                    default -> System.out.println(CmsColor.red("  Unknown command: " + cmd));
                }
            } catch (Exception e) {
                System.out.println(CmsColor.red("  Error: " + e.getMessage()));
            }
        }
    }

    private void printHelp() {
        System.out.println(CmsColor.bold("\n  Commands:"));
        System.out.println("    " + CmsColor.bold("status") + "                        查看服务器状态");
        System.out.println("    " + CmsColor.bold("list") + "                          列出已连接的客户端");
        System.out.println("    " + CmsColor.bold("load-scl") + " [path]              重新/切换 SCL 配置文件");
        System.out.println("    push " + CmsColor.cyan("cmd-term") + " <ref> <val>      " + section("cmd-term") + "推送命令终止通知");
        System.out.println("    push " + CmsColor.cyan("time-act-term") + " <ref> <val>  " + section("time-act-term") + "推送定时终止通知");
        System.out.println("    push " + CmsColor.cyan("report") + " <rptID> <ref> <val> " + section("report") + "推送报告");
        System.out.println("    " + CmsColor.bold("exit") + "                          停止服务器并退出");
        System.out.println(CmsColor.gray("\nTip: Tab 键可补全命令"));
    }

    private static String section(String cliName) {
        ServiceInfo info = ServiceInfo.byCliName(cliName);
        return info != null ? "[" + info.getSection() + "] " : "";
    }

    private void printCommandHelp(String cmd) {
        switch (cmd) {
            case "status" -> System.out.println("  " + CmsColor.bold("status") + " - 查看服务器运行状态\n    " + CmsColor.green("用法: ") + "status");
            case "list" -> System.out.println("  " + CmsColor.bold("list") + " - 列出所有已连接的客户端会话\n    " + CmsColor.green("用法: ") + "list");
            case "load-scl" -> System.out.println("  " + CmsColor.bold("load-scl") + " - 重新/切换 SCL 配置文件\n    " + CmsColor.green("用法: ") + "load-scl [path]\n    不指定路径则重新加载当前文件");
            case "reload-config" -> System.out.println("  " + CmsColor.bold("reload-config") + " - 重新加载配置文件（JSON/YAML）\n    " + CmsColor.green("用法: ") + "reload-config");
            case "push" -> System.out.println("  " + CmsColor.bold("push") + " - 向客户端推送主动通知\n"
                    + "    " + CmsColor.green("用法: ") + "push cmd-term <reference> <value>       推送命令终止通知\n"
                    + "          push time-act-term <reference> <value>  推送定时激活终止通知\n"
                    + "          push report <rptID> <reference> <value>  推送报告");
            case "exit", "quit" -> System.out.println("  " + CmsColor.bold("exit") + " - 停止服务器并退出\n    " + CmsColor.green("用法: ") + "exit");
            default -> System.out.println(CmsColor.red("  Unknown command: " + cmd));
        }
    }

    private void printStatus() {
        System.out.println("  Port: " + server.getPort() + " (TLS: " + CmsConfigLoader.load().getServer().getSslPort() + ")");
        System.out.println("  Bound: " + server.isBound());
        System.out.println("  Security: " + (server.isSecurityEnabled() ? "ON" : "OFF"));
        System.out.println("  Active sessions: " + server.getSessions().size());
        String scl = server.getSclFilePath();
        if (scl != null) {
            System.out.println("  SCL: " + scl);
        }
    }

    private void handleLoadScl(String[] parts) throws Exception {
        String path;
        if (parts.length > 1) {
            path = parts[1];
            if (path.startsWith("\"") && path.endsWith("\"")) {
                path = path.substring(1, path.length() - 1);
            }
            if (path.startsWith("./")) {
                path = Paths.get(System.getProperty("user.dir"), path).normalize().toString();
            }
        } else {
            path = server.getSclFilePath();
            if (path == null) {
                path = CmsConfigLoader.load().getServer().getSclFile();
            }
        }
        server.loadScl(path);
        System.out.println(CmsColor.green("  SCL loaded: " + path));
    }

    private void handleReloadConfig() {
        CmsConfigLoader.reload();
        System.out.println(CmsColor.green("  Config reloaded"));
    }

    private void listSessions() {
        Collection<CmsServerSession> sessions = server.getSessions();
        if (sessions.isEmpty()) {
            System.out.println("  No connected clients");
            return;
        }
        System.out.println("  Connected clients (" + sessions.size() + "):");
        int i = 0;
        for (CmsServerSession s : sessions) {
            String state = s.getState().name();
            String ap = s.getAccessPointName() != null ? s.getAccessPointName() : "-";
            System.out.println("    [" + i + "] " + s.getSessionId() + "  state=" + state + "  ap=" + ap);
            i++;
        }
    }

    private void handlePush(String[] parts) throws Exception {
        if (parts.length < 2) {
            System.out.println("  Usage: push <type> [params...]");
            return;
        }

        Collection<CmsServerSession> sessions = server.getSessions();
        if (sessions.isEmpty()) {
            System.out.println("  No connected clients to push to");
            return;
        }
        CmsServerSession session = sessions.iterator().next();

        String type = parts[1].toLowerCase();
        switch (type) {
            case "cmd-term" -> {
                if (parts.length < 4) {
                    System.out.println("  Usage: push cmd-term <reference> <value>");
                    return;
                }
                String ref = parts[2];
                boolean val = Boolean.parseBoolean(parts[3]);
                server.pushCommandTermination(session, ref, new CmsBoolean(val));
                System.out.println("  ✓ CommandTermination sent: " + ref);
            }
            case "time-act-term" -> {
                if (parts.length < 4) {
                    System.out.println("  Usage: push time-act-term <reference> <value>");
                    return;
                }
                String ref = parts[2];
                boolean val = Boolean.parseBoolean(parts[3]);
                server.pushTimeActivatedOperateTermination(session, ref, new CmsBoolean(val));
                System.out.println("  ✓ TimeActivatedOperateTermination sent: " + ref);
            }
            case "report" -> {
                if (parts.length < 5) {
                    System.out.println("  Usage: push report <rptID> <reference> <value>");
                    return;
                }
                String rptID = parts[2];
                String ref = parts[3];
                int val = Integer.parseInt(parts[4]);
                server.pushReport(session, rptID, ref, "ST", val);
                System.out.println("  ✓ Report sent: " + rptID);
            }
            default -> System.out.println("  Unknown push type: " + type);
        }
    }
}