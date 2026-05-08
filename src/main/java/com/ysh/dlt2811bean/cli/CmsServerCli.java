package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.transport.app.CmsServer;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

import java.util.*;

public class CmsServerCli {

    private CmsServer server;
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public static void main(String[] args) throws Exception {
        new CmsServerCli().run();
    }

    public void run() throws Exception {
        server = new CmsServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop();
            System.out.println("Server stopped");
        }));
        server.start();
        System.out.println("CMS Server running on port " + server.getPort() + "...");
        System.out.println("Type 'help' for commands");

        while (running) {
            System.out.println();
            System.out.print("server> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                String[] parts = line.split("\\s+");
                String cmd = parts[0].toLowerCase();

                switch (cmd) {
                    case "help" -> printHelp();
                    case "exit", "quit" -> { server.stop(); running = false; }
                    case "status" -> printStatus();
                    case "list" -> listSessions();
                    case "push" -> handlePush(parts);
                    default -> System.out.println("  Unknown command: " + cmd);
                }
            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }
    }

    private void printHelp() {
        System.out.println("  Commands:");
        System.out.println("    status                        查看服务器状态");
        System.out.println("    list                          列出已连接的客户端");
        System.out.println("    push cmd-term <ref> <val>     推送命令终止通知 (val=true/false)");
        System.out.println("    push time-act-term <ref> <val> 推送定时终止通知 (val=true/false)");
        System.out.println("    push report <rptID> <ref> <val> 推送报告");
        System.out.println("    exit                          停止服务器并退出");
    }

    private void printStatus() {
        System.out.println("  Port: " + server.getPort());
        System.out.println("  Bound: " + server.isBound());
        System.out.println("  Security: " + (server.isSecurityEnabled() ? "ON" : "OFF"));
        System.out.println("  Active sessions: " + server.getSessions().size());
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