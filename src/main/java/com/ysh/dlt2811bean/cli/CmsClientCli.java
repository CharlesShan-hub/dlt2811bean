package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.service.info.DataTypeInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.control.CmsCancel;
import com.ysh.dlt2811bean.service.svc.control.CmsOperate;
import com.ysh.dlt2811bean.service.svc.control.CmsSelect;
import com.ysh.dlt2811bean.service.svc.control.CmsSelectWithValue;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperate;
import com.ysh.dlt2811bean.service.svc.file.CmsDeleteFile;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFile;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileDirectory;
import com.ysh.dlt2811bean.service.svc.file.CmsSetFile;
import com.ysh.dlt2811bean.service.svc.negotiation.CmsAssociateNegotiate;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDefinition;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDefinition;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
import com.ysh.dlt2811bean.service.svc.rpc.CmsRpcCall;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.CmsSetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;

import java.nio.file.Paths;
import java.util.*;

public class CmsClientCli {

    private final CmsClient client = new CmsClient();
    private final Map<String, CommandHandler> handlers = new LinkedHashMap<>();
    private final LineReader reader;
    private CmsConfig config = CmsConfigLoader.load();
    private boolean running = true;
    private final java.util.Set<String> cachedRefs = new java.util.HashSet<>();
    private final java.util.Set<String> cachedLds = new java.util.HashSet<>();

    public CmsClientCli() {
        reader = LineReaderBuilder.builder()
                .highlighter(new org.jline.reader.Highlighter() {
                    public org.jline.utils.AttributedString highlight(org.jline.reader.LineReader rdr, String buffer) {
                        String[] parts = buffer.split("\\s+", 2);
                        String cmd = parts[0].toLowerCase();
                        CommandHandler h = handlers.get(cmd);
                        if (h == null || h.getParams().isEmpty()) {
                            return new org.jline.utils.AttributedStringBuilder().append(buffer).toAttributedString();
                        }
                        int argsCount = buffer.trim().isEmpty() ? 0 : buffer.trim().split("\\s+").length - 1;
                        if (argsCount >= h.getParams().size()) {
                            return new org.jline.utils.AttributedStringBuilder().append(buffer).toAttributedString();
                        }
                        org.jline.utils.AttributedStringBuilder asb = new org.jline.utils.AttributedStringBuilder();
                        asb.append(buffer);
                        for (int i = argsCount; i < h.getParams().size(); i++) {
                            asb.style(org.jline.utils.AttributedStyle.DEFAULT.foreground(org.jline.utils.AttributedStyle.BRIGHT));
                            asb.append(" <" + h.getParams().get(i).getName() + ">");
                        }
                        return asb.toAttributedString();
                    }
                    public void setErrorPattern(java.util.regex.Pattern errorPattern) {}
                    public void setErrorIndex(int errorIndex) {}
                })
                .completer(new Completer() {
                    @Override
                    public void complete(LineReader rdr, ParsedLine parsedLine, java.util.List<Candidate> candidates) {
                        String buffer = parsedLine.line();
                        String word = parsedLine.word();
                        if (buffer.toLowerCase().startsWith("help datatype ")) {
                            for (DataTypeInfo dt : DataTypeInfo.values()) {
                                if (dt.getTypeName().toLowerCase().startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(dt.getTypeName()));
                                }
                            }
                        } else if (buffer.toLowerCase().startsWith("help ")) {
                            for (String cmd : handlers.keySet()) {
                                if (cmd.startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(cmd));
                                }
                            }
                            if ("datatype".startsWith(word.toLowerCase())) {
                                candidates.add(new Candidate("datatype"));
                            }
                        } else if (buffer.contains(" ")) {
                            String[] parts = buffer.trim().split("\\s+");
                            String cmdName = parts[0].toLowerCase();
                            CommandHandler h = handlers.get(cmdName);
                            if (h != null) {
                                int paramIdx = parts.length - 1;
                                if (paramIdx < h.getParams().size()) {
                                    Param param = h.getParams().get(paramIdx);
                                    if (!param.getEnumChoices().isEmpty()) {
                                        for (Param.EnumChoice ec : param.getEnumChoices()) {
                                            if (ec.value.toLowerCase().startsWith(word.toLowerCase())) {
                                                candidates.add(new Candidate(ec.value));
                                            }
                                        }
                                    } else if (isRefParam(cmdName, paramIdx)) {
                                        java.util.Set<String> pool = "ld-dir".equals(cmdName) ? cachedLds : cachedRefs;
                                        for (String ref : pool) {
                                            if (ref.toLowerCase().startsWith(word.toLowerCase())) {
                                                candidates.add(new Candidate(ref));
                                            }
                                        }
                                    } else if (param.getDefaultValue() != null && !param.getDefaultValue().isEmpty()) {
                                        candidates.add(new Candidate(param.getDefaultValue()));
                                    }
                                }
                            }
                        } else {
                            for (String cmd : handlers.keySet()) {
                                if (cmd.startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(cmd));
                                }
                            }
                        }
                    }
                })
                .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".cms_cli_history"))
                .build();
        register(new HelpHandler());
        register(new ExitHandler());
        register(new ConnectHandler());
        register(new ConnectTlsHandler());
        register(new CloseHandler());
        register(new StatusHandler());
        register(new NegotiateHandler());
        register(new AssociateHandler());
        register(new ReleaseHandler());
        register(new AbortHandler());
        register(new TestHandler());
        register(new RpcHandler());
        register(new IfaceDirHandler());
        register(new IfaceDefHandler());
        register(new MethodDirHandler());
        register(new MethodDefHandler());
        register(new FileGetHandler());
        register(new FileSetHandler());
        register(new FileDeleteHandler());
        register(new FileAttrHandler());
        register(new FileDirHandler());
        register(new SelectHandler());
        register(new SelectWithValueHandler());
        register(new OperateHandler());
        register(new CancelHandler());
        register(new TimeActOperateHandler());
        register(new MsvcbValHandler());
        register(new SetMsvcbHandler());
        register(new ServerDirHandler());
        register(new LdDirHandler());
        register(new LnDirHandler());
        register(new GetAllValuesHandler());
        register(new GetAllDefHandler());
        register(new GetAllCbHandler());
        register(new CliSettingHandler());
        register(new ClearHandler());
    }

    private void register(CommandHandler handler) {
        handlers.put(handler.getName(), handler);
    }

    public void run() {
        java.util.logging.Logger.getLogger("org.bouncycastle").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("CMS CLI v1.0 — Type 'help' for commands, 'exit' to quit");
        while (running) {
            System.out.println();
            String raw;
            try {
                raw = reader.readLine("cms> ").trim();
            } catch (Exception e) {
                if (running) {
                    System.out.println(CmsColor.red("\n  Connection lost. Type 'connect' to reconnect."));
                }
                continue;
            }
            String line = raw.toLowerCase();

            if (line.isEmpty()) continue;

            if (line.startsWith("help ")) {
                String helpArg = line.substring(5).trim().toLowerCase();
                if (helpArg.equals("datatype")) {
                    printDatatypeList();
                    continue;
                }
                if (helpArg.startsWith("datatype ")) {
                    String dtName = raw.substring(5).trim().substring(9).trim();
                    DataTypeInfo dt = DataTypeInfo.byTypeName(dtName);
                    if (dt == null) {
                        System.out.println("  " + CmsColor.red("Unknown datatype: " + dtName));
                    } else {
                        printDatatypeHelp(dt);
                    }
                    continue;
                }
                CommandHandler h = handlers.get(helpArg);
                String helpCmdName = helpArg;
                if (h == null) {
                    ServiceInfo sectionInfo = ServiceInfo.bySection(helpArg);
                    if (sectionInfo != null) {
                        helpCmdName = sectionInfo.getCliName();
                        h = handlers.get(helpCmdName);
                    }
                }
                if (h == null) {
                    System.out.println("  " + CmsColor.red("Unknown command: " + helpArg));
                } else {
                    printCommandHelp(helpCmdName, h);
                }
                continue;
            }

            String[] inputParts = raw.split("\\s+", 2);
            String cmdName = inputParts[0].toLowerCase();
            String inlineArgs = inputParts.length > 1 ? inputParts[1].trim() : null;

            CommandHandler handler = handlers.get(cmdName);
            if (handler == null) {
                System.out.println(CmsColor.red("Unknown command: " + cmdName) + "  (type 'help' for available commands)");
                continue;
            }

            try {
                Map<String, String> values = new HashMap<>();
                java.util.List<String> inlineTokens = inlineArgs != null && !inlineArgs.isEmpty()
                    ? new java.util.ArrayList<>(java.util.Arrays.asList(inlineArgs.split("\\s+")))
                    : new java.util.ArrayList<>();

                if (inlineTokens.isEmpty() && !handler.getParams().isEmpty()) {
                    StringBuilder hint = new StringBuilder();
                    hint.append(CmsColor.gray("  Usage: " + cmdName));
                    for (Param p : handler.getParams()) {
                        hint.append(" <" + p.getName() + ">");
                    }
                    System.out.println(hint.toString());
                }
                for (Param param : handler.getParams()) {
                    if (!inlineTokens.isEmpty()) {
                        values.put(param.getName(), inlineTokens.remove(0));
                        continue;
                    }

                    if (!param.getEnumChoices().isEmpty()) {
                        System.out.println("  " + param.getPrompt() + ":");
                        int maxLen = param.getEnumChoices().stream().mapToInt(ec -> ec.value.length()).max().orElse(0);
                        for (Param.EnumChoice ec : param.getEnumChoices()) {
                            System.out.println("    " + padRight(ec.value, maxLen) + "  " + ec.label);
                        }
                    }
                    String simplePrompt = param.getEnumChoices().isEmpty() ? param.getPrompt() : "  值";
                    if (param.getDefaultValue() != null) {
                        simplePrompt += " [" + param.getDefaultValue() + "]";
                    }
                    String input = reader.readLine(simplePrompt + ": ").trim();

                    if (input.isEmpty()) {
                        if (param.getDefaultValue() != null) {
                            values.put(param.getName(), param.getDefaultValue());
                        } else if (param.isRequired()) {
                            System.out.println(CmsColor.gray("  (skipped, using empty)"));
                            values.put(param.getName(), "");
                        } else {
                            values.put(param.getName(), "");
                        }
                    } else {
                        values.put(param.getName(), input);
                    }
                }
                handler.execute(client, values);
                if (!cmdName.equals("connect") && !cmdName.equals("exit") && !cmdName.equals("close") && !cmdName.equals("clear")
                        && !client.isConnected()) {
                    System.out.println(CmsColor.red("  Connection lost. Type 'connect' to reconnect."));
                }
            } catch (Exception e) {
                System.out.println(CmsColor.red("  ERROR: " + e.getMessage()));
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        new CmsClientCli().run();
    }

    // ==================== Help & Exit ====================

    private class HelpHandler implements CommandHandler {
        public String getName() { return "help"; }
        public String getDescription() { return "显示帮助信息"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            System.out.println("\n" + CmsColor.bold("可用命令:") + "\n");
            System.out.println(CmsColor.gray("  " + padRight("命令", 17) + String.format("%-7s", "章节") + String.format("%-5s", "服务码") + " 描述"));
            System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
            java.util.List<CommandHandler> sorted = new java.util.ArrayList<>(handlers.values());
            sorted.sort((a, b) -> {
                ServiceInfo ai = ServiceInfo.byCliName(a.getName());
                ServiceInfo bi = ServiceInfo.byCliName(b.getName());
                if (ai == null && bi == null) return a.getName().compareTo(b.getName());
                if (ai == null) return 1;
                if (bi == null) return -1;
                return compareSection(ai.getSection(), bi.getSection());
            });
            for (CommandHandler h : sorted) {
                if (h.getName().equals("help") || h.getName().equals("exit")) continue;
                ServiceInfo info = ServiceInfo.byCliName(h.getName());
                if (info != null) {
                    System.out.println("  " + CmsColor.bold(padRight(h.getName(), 18))
                            + CmsColor.cyan(String.format("%-8s", "[" + info.getSection() + "]"))
                            + " " + CmsColor.yellow(String.format("0x%02X", info.getServiceCode()))
                            + " " + info.getDescription());
                } else {
                    System.out.println("  " + CmsColor.bold(padRight(h.getName(), 18))
                             + String.format("%-8s", "")
                             + String.format("%-6s", "")
                            + " " + h.getDescription());
                }
            }
            System.out.println("  " + CmsColor.bold(padRight("help", 18)) + String.format("%-8s", "") + String.format("%-6s", "") + " 显示帮助信息");
            System.out.println("  " + CmsColor.bold(padRight("exit", 18)) + String.format("%-8s", "") + String.format("%-6s", "") + " 退出程序");
            System.out.println(CmsColor.gray("\nUse: help <command> 查看命令详细用法  |  help datatype <name> 查看数据类型  |  Tab 键可补全命令"));
        }
    }

    private void printCommandHelp(String cmdName, CommandHandler h) {
        ServiceInfo info = ServiceInfo.byCliName(cmdName);
        System.out.println("\n  " + CmsColor.bold(cmdName) + " - "
                + (info != null ? (CmsColor.cyan("[" + info.getSection() + "] ") + info.getDescription()) : h.getDescription()));
        if (info != null) {
            if (!info.getDescriptionDetail().isEmpty()) {
                System.out.println("\n  " + CmsColor.cyan("功能介绍: "));
                for (String line : info.getDescriptionDetail().split("\n")) {
                    System.out.println("    " + line);
                }
            }
            if (!info.getAsn1Definition().isEmpty()) {
                System.out.println("\n  " + CmsColor.cyan("ASN.1 定义: "));
                for (String line : info.getAsn1Definition().split("\n")) {
                    System.out.println("    " + line);
                }
            }
            System.out.println("\n  " + CmsColor.green("用法: ") + info.getUsage());
        }
        for (Param param : h.getParams()) {
            System.out.println("    " + CmsColor.cyan(param.getName()) + "  " + param.getPrompt()
                    + (param.getDefaultValue() != null && !param.getDefaultValue().isEmpty() ? CmsColor.gray(" (默认: " + param.getDefaultValue() + ")") : ""));
        }
        if (cmdName.equals("cli-setting")) {
            System.out.println("\n  " + CmsColor.bold("可用配置项:"));
            System.out.println("    " + CmsColor.cyan("trace-pdu") + CmsColor.gray("    true/false  ") + "显示请求/响应 PDU 报文");
        }
    }

    private static int compareSection(String a, String b) {
        String[] pa = a.split("\\.");
        String[] pb = b.split("\\.");
        for (int i = 0; i < Math.min(pa.length, pb.length); i++) {
            int cmp = Integer.parseInt(pa[i]) - Integer.parseInt(pb[i]);
            if (cmp != 0) return cmp;
        }
        return pa.length - pb.length;
    }

    private static boolean isRefParam(String cmdName, int paramIdx) {
        return switch (cmdName) {
            case "ld-dir" -> paramIdx == 0;
            case "ln-dir", "get-all-values", "get-all-def", "get-all-cb" -> paramIdx == 0;
            default -> false;
        };
    }

    private void printDatatypeHelp(DataTypeInfo dt) {
        System.out.println("\n  " + CmsColor.bold(dt.getTypeName()) + " - "
                + CmsColor.cyan("[" + dt.getSection() + "] ") + dt.getDescription());
        if (!dt.getAsn1Definition().isEmpty()) {
            System.out.println("\n  " + CmsColor.cyan("ASN.1 定义: "));
            for (String line : dt.getAsn1Definition().split("\n")) {
                System.out.println("    " + line);
            }
        }
    }

    private void printDatatypeList() {
        System.out.println("\n" + CmsColor.bold("可用数据类型:") + "\n");
        System.out.println(CmsColor.gray("  " + padRight("类型名", 22) + String.format("%-7s", "章节") + " 描述"));
        System.out.println(CmsColor.gray("  ────────────────────────────────────────────"));
        java.util.List<DataTypeInfo> sorted = new java.util.ArrayList<>(java.util.Arrays.asList(DataTypeInfo.values()));
        sorted.sort((a, b) -> compareSection(a.getSection(), b.getSection()));
        for (DataTypeInfo dt : sorted) {
            System.out.println("  " + CmsColor.bold(padRight(dt.getTypeName(), 22))
                    + CmsColor.cyan(String.format("%-8s", "[" + dt.getSection() + "]"))
                    + " " + dt.getDescription());
        }
        System.out.println(CmsColor.gray("\nUse: help datatype <name> 查看数据类型详细定义"));
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private CmsApdu sendAndPrint(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        if (config.getCli().isTracePdu()) {
            System.out.println(CmsColor.gray("  >> Request PDU:\n" + asdu.toString().indent(4).stripTrailing()));
        }
        CmsApdu response = client.send(asdu);
        if (config.getCli().isTracePdu()) {
            System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
        }
        return response;
    }

    private class ExitHandler implements CommandHandler {
        public String getName() { return "exit"; }
        public String getDescription() { return "退出程序"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            if (client.isConnected()) {
                try { client.release(); } catch (Exception ignored) {}
                client.close();
            }
            System.out.println("Bye!");
            running = false;
        }
    }

    // ==================== Connection ====================

    private class ConnectHandler implements CommandHandler {
        public String getName() { return "connect"; }
        public String getDescription() { return "连接服务器（自动协商与关联）"; }
        public List<Param> getParams() {
            return List.of(
                new Param("host", "服务器 IP", "127.0.0.1"),
                new Param("port", "服务器端口", String.valueOf(CmsConfigLoader.load().getServer().getPort())),
                new Param("asduSize", "ASDU 大小（1~65531，留空跳过协商）", String.valueOf(CmsConfigLoader.load().getNegotiate().getAsduSize())),
                new Param("protocolVersion", "协议版本", String.valueOf(CmsConfigLoader.load().getNegotiate().getProtocolVersion())),
                new Param("ap", "AccessPoint", CmsConfigLoader.load().getClient().getDefaultAccessPoint()),
                new Param("ep", "Endpoint", CmsConfigLoader.load().getClient().getDefaultEp()),
                new Param("secure", "携带证书认证 (true/false)", "false")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            String host = values.get("host");
            int port = Integer.parseInt(values.get("port"));
            client.connect(host, port);
            System.out.println(CmsColor.green("  Connected to " + host + ":" + port));

            String asduSizeStr = values.get("asduSize");
            if (!asduSizeStr.isEmpty()) {
                int asduSize = Integer.parseInt(asduSizeStr);
                int apduSize = asduSize + 4;
                long protocolVersion = Long.parseLong(values.get("protocolVersion"));
                String ap = values.get("ap");
                String ep = values.get("ep");

                client.setAccessPoint(ap, ep);

                CmsAssociateNegotiate negReq = new CmsAssociateNegotiate(MessageType.REQUEST)
                        .apduSize(apduSize).asduSize(asduSize).protocolVersion(protocolVersion);
                if (config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  >> Request PDU:\n" + negReq.toString().indent(4).stripTrailing()));
                }
                CmsApdu negResponse = client.associateNegotiate(apduSize, asduSize, protocolVersion);
                if (config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  << Response PDU:\n" + negResponse.toString().indent(4).stripTrailing()));
                }
                if (negResponse == null || negResponse.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                    System.out.println(CmsColor.red("  Negotiate failed"));
                    return;
                }
                System.out.println(CmsColor.green("  Negotiated OK") + " (asduSize=" + asduSize + ")");

                boolean secure = Boolean.parseBoolean(values.get("secure"));
                if (secure) {
                    client.enableSecurity();
                    System.out.println(CmsColor.gray("  GM security enabled"));
                }

                CmsAssociate assocReq = new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference(ap, ep);
                if (config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  >> Request PDU:\n" + assocReq.toString().indent(4).stripTrailing()));
                }
                CmsApdu assocResponse = client.associate();
                if (assocResponse != null && config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  << Response PDU:\n" + assocResponse.toString().indent(4).stripTrailing()));
                }
                if (assocResponse != null && assocResponse.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                    System.out.println(CmsColor.green("  Associated") + " (ID=" + bytesToHex(client.getAssociationId(), 8) + "...)");
                    discoverAfterConnect(client);
                } else {
                    System.out.println(CmsColor.red("  Associate failed"));
                }
            }
        }
    }

    private class ConnectTlsHandler implements CommandHandler {
        public String getName() { return "connect-tls"; }
        public String getDescription() { return "TLS 连接服务器（自动协商与关联）"; }
        public List<Param> getParams() {
            return List.of(
                new Param("host", "服务器 IP", "127.0.0.1"),
                new Param("port", "服务器端口", String.valueOf(CmsConfigLoader.load().getServer().getSslPort())),
                new Param("asduSize", "ASDU 大小（1~65531，留空跳过协商）", String.valueOf(CmsConfigLoader.load().getNegotiate().getAsduSize())),
                new Param("protocolVersion", "协议版本", String.valueOf(CmsConfigLoader.load().getNegotiate().getProtocolVersion())),
                new Param("ap", "AccessPoint", CmsConfigLoader.load().getClient().getDefaultAccessPoint()),
                new Param("ep", "Endpoint", CmsConfigLoader.load().getClient().getDefaultEp()),
                new Param("secure", "携带证书认证 (true/false)", "false")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            String host = values.get("host");
            int port = Integer.parseInt(values.get("port"));

            CmsConfig config = CmsConfigLoader.load();
            GmSslContext sslContext = GmSslContext.forClient()
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
            client.sslContext(sslContext);
            client.connectTls(host, port);
            System.out.println(CmsColor.green("  Connected to " + host + ":" + port + " (TLS)"));

            String asduSizeStr = values.get("asduSize");
            if (!asduSizeStr.isEmpty()) {
                int asduSize = Integer.parseInt(asduSizeStr);
                int apduSize = asduSize + 4;
                long protocolVersion = Long.parseLong(values.get("protocolVersion"));
                String ap = values.get("ap");
                String ep = values.get("ep");

                client.setAccessPoint(ap, ep);

                CmsAssociateNegotiate negReq = new CmsAssociateNegotiate(MessageType.REQUEST)
                        .apduSize(apduSize).asduSize(asduSize).protocolVersion(protocolVersion);
                if (config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  >> Request PDU:\n" + negReq.toString().indent(4).stripTrailing()));
                }
                CmsApdu negResponse = client.associateNegotiate(apduSize, asduSize, protocolVersion);
                if (config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  << Response PDU:\n" + negResponse.toString().indent(4).stripTrailing()));
                }
                if (negResponse == null || negResponse.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                    System.out.println(CmsColor.red("  Negotiate failed"));
                    return;
                }
                System.out.println(CmsColor.green("  Negotiated OK") + " (asduSize=" + asduSize + ")");

                boolean secure = Boolean.parseBoolean(values.get("secure"));
                if (secure) {
                    client.enableSecurity();
                    System.out.println(CmsColor.gray("  GM security enabled"));
                }

                CmsAssociate assocReq = new CmsAssociate(MessageType.REQUEST)
                        .serverAccessPointReference(ap, ep);
                if (config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  >> Request PDU:\n" + assocReq.toString().indent(4).stripTrailing()));
                }
                CmsApdu assocResponse = client.associate();
                if (assocResponse != null && config.getCli().isTracePdu()) {
                    System.out.println(CmsColor.gray("  << Response PDU:\n" + assocResponse.toString().indent(4).stripTrailing()));
                }
                if (assocResponse != null && assocResponse.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                    System.out.println(CmsColor.green("  Associated") + " (ID=" + bytesToHex(client.getAssociationId(), 8) + "...)");
                    discoverAfterConnect(client);
                } else {
                    System.out.println(CmsColor.red("  Associate failed"));
                }
            }
        }
    }

    private void discoverAfterConnect(CmsClient client) {
        try {
            CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
                    .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
            CmsApdu response = client.send(asdu);
            if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                CmsGetServerDirectory dir = (CmsGetServerDirectory) response.getAsdu();
                cachedRefs.clear();
                cachedLds.clear();
                System.out.println(CmsColor.gray("  Discovered devices:"));
                for (int i = 0; i < dir.reference().size(); i++) {
                    String ldRef = dir.reference().get(i).get();
                    cachedRefs.add(ldRef);
                    cachedLds.add(ldRef);
                    System.out.println(CmsColor.gray("    [" + i + "] LD: " + ldRef));
                    discoverLnRefs(client, ldRef);
                }
            }
        } catch (Exception ignored) {}
    }

    private void discoverLnRefs(CmsClient client, String ldName) {
        try {
            CmsGetLogicalDeviceDirectory req = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
            req.ldName(ldName);
            CmsApdu response = client.send(req);
            if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
                for (int i = 0; i < asdu.lnReference().size(); i++) {
                    String lnRef = ldName + "/" + asdu.lnReference().get(i).get();
                    cachedRefs.add(lnRef);
                    System.out.println(CmsColor.gray("      LN[" + i + "] " + lnRef));
                }
            }
        } catch (Exception ignored) {}
    }

    private class CloseHandler implements CommandHandler {
        public String getName() { return "close"; }
        public String getDescription() { return "断开连接"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            client.close();
            System.out.println(CmsColor.green("  Disconnected"));
        }
    }

    private class StatusHandler implements CommandHandler {
        public String getName() { return "status"; }
        public String getDescription() { return "查看当前连接和关联状态"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            boolean connected = client.isConnected();
            byte[] assocId = client.getAssociationId();
            System.out.println("  Connected: " + (connected ? CmsColor.green("YES") : CmsColor.red("NO")));
            System.out.println("  Associated: " + (assocId != null ? CmsColor.green("YES") + " (id=" + bytesToHex(assocId, 8) + "...)" : CmsColor.red("NO")));
        }
    }

    // ==================== Association ====================

    private class NegotiateHandler implements CommandHandler {
        public String getName() { return "negotiate"; }
        public String getDescription() { return "协商服务参数 (连接后、关联前)"; }
        public List<Param> getParams() {
            CmsConfig config = CmsConfigLoader.load();
            return List.of(
                new Param("asduSize", "ASDU 大小", String.valueOf(config.getNegotiate().getAsduSize())),
                new Param("protocolVersion", "协议版本号", String.valueOf(config.getNegotiate().getProtocolVersion()))
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println(CmsColor.gray("  Not connected. Type 'connect' first."));
                return;
            }
            int asduSize = Integer.parseInt(values.get("asduSize"));
            int apduSize = asduSize + 4;
            long protocolVersion = Long.parseLong(values.get("protocolVersion"));
            CmsAssociateNegotiate reqAsdu = new CmsAssociateNegotiate(MessageType.REQUEST)
                    .apduSize(apduSize)
                    .asduSize(asduSize)
                    .protocolVersion(protocolVersion);
            System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
            CmsApdu response = client.associateNegotiate(apduSize, asduSize, protocolVersion);
            System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println(CmsColor.green("  Negotiated!"));
            } else {
                System.out.println(CmsColor.red("  Negotiate failed"));
            }
        }
    }

    private class AssociateHandler implements CommandHandler {
        public String getName() { return "associate"; }
        public String getDescription() { return "建立关联"; }
        public List<Param> getParams() {
            CmsConfig config = CmsConfigLoader.load();
            return List.of(
                new Param("ap", "访问点 (AccessPoint)", config.getClient().getDefaultAccessPoint()),
                new Param("ep", "EP", config.getClient().getDefaultEp()),
                new Param("secure", "携带证书认证 (true/false)", "false")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println(CmsColor.gray("  Not connected. Type 'connect' first."));
                return;
            }
            if (Boolean.parseBoolean(values.get("secure"))) {
                client.enableSecurity();
                System.out.println(CmsColor.gray("  GM security enabled"));
            }
            String ap = values.get("ap");
            String ep = values.get("ep");
            CmsAssociate reqAsdu = new CmsAssociate(MessageType.REQUEST)
                    .serverAccessPointReference(ap, ep);
            System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
            CmsApdu response = "E1Q1SB1".equals(ap) && "S1".equals(ep)
                ? client.associate()
                : client.associate(ap, ep);
            System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println(CmsColor.green("  Associated!"));
            } else {
                System.out.println(CmsColor.red("  Associate failed"));
            }
        }
    }

    private class ReleaseHandler implements CommandHandler {
        public String getName() { return "release"; }
        public String getDescription() { return "释放关联"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println(CmsColor.gray("  Not connected. Type 'connect' first."));
                return;
            }
            CmsRelease reqAsdu = new CmsRelease(MessageType.REQUEST);
            System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
            CmsApdu response = client.release();
            System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println(CmsColor.green("  Released"));
            } else {
                System.out.println(CmsColor.red("  Release failed"));
            }
        }
    }

    private class AbortHandler implements CommandHandler {
        public String getName() { return "abort"; }
        public String getDescription() { return "异常中止关联"; }
        public List<Param> getParams() {
            return List.of(new Param("reason", "中止原因", "4", List.of(
                new Param.EnumChoice("0", "正常中止"),
                new Param.EnumChoice("1", "内存不足"),
                new Param.EnumChoice("2", "通信中断"),
                new Param.EnumChoice("3", "未知错误"),
                new Param.EnumChoice("4", "其他")
            )));
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println(CmsColor.gray("  Not connected."));
                return;
            }
            int reason = Integer.parseInt(values.get("reason"));
            CmsAbort reqAsdu = new CmsAbort(MessageType.REQUEST).reason(reason);
            System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
            client.abort(reason);
            System.out.println(CmsColor.green("  Abort sent"));
        }
    }

    private class TestHandler implements CommandHandler {
        public String getName() { return "test"; }
        public String getDescription() { return "发送心跳测试"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println(CmsColor.gray("  Not connected. Type 'connect' first."));
                return;
            }
            CmsTest reqAsdu = new CmsTest(MessageType.REQUEST);
            System.out.println(CmsColor.gray("  >> Request PDU:\n" + reqAsdu.toString().indent(4).stripTrailing()));
            CmsApdu response = client.test();
            if (response != null) {
                System.out.println(CmsColor.gray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing()));
            }
            System.out.println("  Test " + (response != null ? CmsColor.green("OK") : CmsColor.red("failed")));
        }
    }

    // ==================== RPC ====================

    private class RpcHandler implements CommandHandler {
        private final java.util.Map<String, byte[]> lastCallIds = new java.util.HashMap<>();

        public String getName() { return "rpc"; }
        public String getDescription() { return "远程过程调用 (ping/echo/iterate)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("method", "RPC 方法", "ping", List.of(
                    new Param.EnumChoice("ping", "通信测试"),
                    new Param.EnumChoice("echo", "回声测试（发送数据并返回）"),
                    new Param.EnumChoice("iterate", "迭代遍历（分页读取数据）")
                )),
                new Param("data", "请求数据 (仅echo)", "hello")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String method = values.get("method");
            String data = values.get("data");

            if (method.equals("ping") || method.equals("pong")) {
                CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST).method("ping").reqData(new com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U(0));
                CmsApdu response = sendAndPrint(client, asdu);
                System.out.println("  RPC ping: " + (response.getMessageType() == MessageType.RESPONSE_POSITIVE ? CmsColor.green("OK") : CmsColor.red("failed")));
                return;
            }

            if (method.equals("iterate")) {
                byte[] lastId = lastCallIds.get("iterate");
                CmsRpcCall asdu;
                if (lastId != null) {
                    asdu = new CmsRpcCall(MessageType.REQUEST).method("iterate").callID(lastId);
                } else {
                    asdu = new CmsRpcCall(MessageType.REQUEST).method("iterate").reqData(new com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U(0));
                }
                CmsApdu response = sendAndPrint(client, asdu);

                if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                    System.out.println("  RPC iterate failed");
                    lastCallIds.remove("iterate");
                    return;
                }

                CmsRpcCall rpc = (CmsRpcCall) response.getAsdu();
                System.out.println("  RPC result: " + rpc.rspData);

                if (rpc.nextCallID != null && rpc.nextCallID.get() != null && rpc.nextCallID.get().length > 0) {
                    lastCallIds.put("iterate", rpc.nextCallID.get());
                    System.out.println("  More data available — run 'rpc' with method=iterate to continue");
                } else {
                    lastCallIds.remove("iterate");
                    System.out.println("  All data received");
                }
                return;
            }

            CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST).method("echo").reqData(new com.ysh.dlt2811bean.datatypes.string.CmsVisibleString(data));
            CmsApdu response = sendAndPrint(client, asdu);
            System.out.println("  RPC echo: " + (response.getMessageType() == MessageType.RESPONSE_POSITIVE ? CmsColor.green("OK") : CmsColor.red("failed")));
        }
    }

    // ==================== RPC Interface Directory ====================

    private class IfaceDirHandler implements CommandHandler {
        public String getName() { return "iface-dir"; }
        public String getDescription() { return "获取RPC接口目录 (IF1, IF2)"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            CmsGetRpcInterfaceDirectory asdu = new CmsGetRpcInterfaceDirectory(MessageType.REQUEST);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetRpcInterfaceDirectory dir = (CmsGetRpcInterfaceDirectory) response.getAsdu();
            System.out.println("  Interfaces: " + dir.reference.size());
            for (int i = 0; i < dir.reference.size(); i++) {
                System.out.println("    [" + i + "] " + dir.reference.get(i).get());
            }
        }
    }

    // ==================== RPC Interface Definition ====================

    private class IfaceDefHandler implements CommandHandler {
        public String getName() { return "iface-def"; }
        public String getDescription() { return "获取RPC接口定义 (IF1/IF2)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("iface", "接口名", "IF1"),
                new Param("after", "参考点 (可选)", "")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String iface = values.get("iface");
            String after = values.get("after");
            CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.REQUEST).interfaceName(iface);
            if (!after.isEmpty()) {
                asdu.referenceAfter(after);
            }
            CmsApdu response = sendAndPrint(client, asdu);

            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetRpcInterfaceDefinition def = (CmsGetRpcInterfaceDefinition) response.getAsdu();
            System.out.println("  Interface: " + iface + ", methods: " + def.method.size());
            for (int i = 0; i < def.method.size(); i++) {
                var m = def.method.get(i);
                System.out.println("    [" + i + "] " + m.name.get()
                    + " (v" + m.version.get() + ", timeout=" + m.timeout.get() + "ms)");
            }
            if (def.moreFollows.get()) {
                String last = def.method.get(def.method.size() - 1).name.get();
                System.out.println("  More available — use after=" + last + " to continue");
            }
        }
    }

    // ==================== RPC Method Directory ====================

    private class MethodDirHandler implements CommandHandler {
        public String getName() { return "method-dir"; }
        public String getDescription() { return "获取RPC方法目录 (IF1/IF2/空=全部)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("iface", "接口名 (可选)", ""),
                new Param("after", "参考点 (可选)", "")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String iface = values.get("iface");
            String after = values.get("after");
            CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.REQUEST);
            if (!iface.isEmpty()) {
                asdu.interfaceName(iface);
            }
            if (!after.isEmpty()) {
                asdu.referenceAfter(after);
            }
            CmsApdu response = sendAndPrint(client, asdu);

            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetRpcMethodDirectory dir = (CmsGetRpcMethodDirectory) response.getAsdu();
            System.out.println("  Methods: " + dir.reference.size());
            for (int i = 0; i < dir.reference.size(); i++) {
                System.out.println("    [" + i + "] " + dir.reference.get(i).get());
            }
            if (dir.moreFollows.get()) {
                String last = dir.reference.get(dir.reference.size() - 1).get();
                System.out.println("  More available — use after=" + last + " to continue");
            }
        }
    }

    // ==================== RPC Method Definition ====================

    private class MethodDefHandler implements CommandHandler {
        public String getName() { return "method-def"; }
        public String getDescription() { return "获取RPC方法定义"; }
        public List<Param> getParams() {
            return List.of(
                new Param("refs", "方法引用 (逗号分隔)", "IF1.Method1,IF1.Method2")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String refs = values.get("refs");
            CmsGetRpcMethodDefinition asdu = new CmsGetRpcMethodDefinition(MessageType.REQUEST);
            for (String ref : refs.split(",")) {
                asdu.addReference(ref);
            }
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetRpcMethodDefinition rpc = (CmsGetRpcMethodDefinition) response.getAsdu();
            System.out.println("  Method definitions: " + rpc.errorMethod.size() + " entries");
            for (int i = 0; i < rpc.errorMethod.size(); i++) {
                var choice = rpc.errorMethod.get(i);
                if (choice.getSelectedIndex() == 0) {
                    System.out.println("    [" + i + "] ERROR: " + choice.error.get());
                } else {
                    var method = choice.method;
                    System.out.println("    [" + i + "] timeout=" + method.timeout.get()
                        + ", version=" + method.version.get());
                }
            }
        }
    }

    // ==================== File ====================

    private class FileGetHandler implements CommandHandler {
        public String getName() { return "file-get"; }
        public String getDescription() { return "读文件 (startPosition=0 取消)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("fileName", "文件路径", "/README.txt"),
                new Param("start", "起始位置 (1开始, 0=取消)", "1")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String fileName = values.get("fileName");
            long start = Long.parseLong(values.get("start"));
            CmsGetFile asdu = new CmsGetFile(MessageType.REQUEST).fileName(fileName).startPosition(start);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetFile file = (CmsGetFile) response.getAsdu();
            byte[] data = file.fileData.get();
            System.out.println("  File: " + fileName + " (pos=" + start + ", size=" + data.length + " bytes" + (file.endOfFile.get() ? ", EOF" : "") + ")");
            System.out.println("  Data: " + new String(data, java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    private class FileSetHandler implements CommandHandler {
        public String getName() { return "file-set"; }
        public String getDescription() { return "写文件 (endOfFile=true 完成)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("fileName", "文件路径", "/upload.txt"),
                new Param("start", "起始位置 (1开始, 0=取消)", "1"),
                new Param("data", "文件内容", "Hello from CMS CLI"),
                new Param("eof", "是否最后一块 (true/false)", "true")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String fileName = values.get("fileName");
            long start = Long.parseLong(values.get("start"));
            String text = values.get("data");
            boolean eof = Boolean.parseBoolean(values.get("eof"));

            CmsSetFile asdu = new CmsSetFile(MessageType.REQUEST).fileName(fileName).startPosition(start)
                    .fileData(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)).endOfFile(eof);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            System.out.println("  Written " + text.length() + " bytes to " + fileName + (eof ? " (complete)" : ""));
        }
    }

    private class FileDeleteHandler implements CommandHandler {
        public String getName() { return "file-delete"; }
        public String getDescription() { return "删除文件 (内置文件受保护)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("fileName", "文件路径", "/upload.txt")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String fileName = values.get("fileName");
            CmsDeleteFile asdu = new CmsDeleteFile(MessageType.REQUEST).fileName(fileName);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Delete failed");
                return;
            }
            System.out.println("  Deleted " + fileName);
        }
    }

    private class FileAttrHandler implements CommandHandler {
        public String getName() { return "file-attr"; }
        public String getDescription() { return "读文件属性 (大小、时间、校验和)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("fileName", "文件路径", "/README.txt")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String fileName = values.get("fileName");
            CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.REQUEST).fileName(fileName);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetFileAttributeValues attr = (CmsGetFileAttributeValues) response.getAsdu();
            System.out.println("  File: " + attr.fileEntry.fileName.get());
            System.out.println("  Size: " + attr.fileEntry.fileSize.get() + " bytes");
            System.out.println("  CRC32: " + Long.toHexString(attr.fileEntry.checkSum.get()));
        }
    }

    private class FileDirHandler implements CommandHandler {
        public String getName() { return "file-dir"; }
        public String getDescription() { return "列文件目录"; }
        public List<Param> getParams() {
            return List.of(
                new Param("path", "路径", "/"),
                new Param("after", "参考文件名 (可选)", "")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String path = values.get("path");
            String after = values.get("after");
            CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.REQUEST);
            if (!path.isEmpty()) asdu.pathName(path);
            if (!after.isEmpty()) asdu.fileAfter(after);
            CmsApdu response = sendAndPrint(client, asdu);

            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetFileDirectory dir = (CmsGetFileDirectory) response.getAsdu();
            System.out.println("  Files: " + dir.fileEntry.size());
            for (int i = 0; i < dir.fileEntry.size(); i++) {
                var entry = dir.fileEntry.get(i);
                System.out.println("    [" + i + "] " + entry.fileName.get()
                    + " (" + entry.fileSize.get() + " bytes)");
            }
        }
    }

    // ==================== Control ====================

    private class SelectHandler implements CommandHandler {
        public String getName() { return "select"; }
        public String getDescription() { return "选择控制对象"; }
        public List<Param> getParams() {
            return List.of(
                new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String ref = values.get("reference");
            CmsSelect asdu = new CmsSelect(MessageType.REQUEST).reference(ref);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Select failed");
                return;
            }
            System.out.println("  Selected: " + ref);
        }
    }

    private class SelectWithValueHandler implements CommandHandler {
        public String getName() { return "select-with-value"; }
        public String getDescription() { return "带值选择控制对象"; }
        public List<Param> getParams() {
            return List.of(
                new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos"),
                new Param("value", "控制值", "true")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String ref = values.get("reference");
            boolean val = Boolean.parseBoolean(values.get("value"));
            CmsSelectWithValue asdu = new CmsSelectWithValue(MessageType.REQUEST).reference(ref)
                    .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val)).ctlNum(0).test(false);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  SelectWithValue failed");
                return;
            }
            System.out.println("  Selected: " + ref + " with value " + val);
        }
    }

    // ==================== Operate ====================

    private class OperateHandler implements CommandHandler {
        public String getName() { return "operate"; }
        public String getDescription() { return "执行控制操作 (需先 select)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos"),
                new Param("value", "控制值", "true")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String ref = values.get("reference");
            boolean val = Boolean.parseBoolean(values.get("value"));
            CmsOperate asdu = new CmsOperate(MessageType.REQUEST).reference(ref)
                    .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val)).ctlNum(1).test(false);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Operate failed");
                return;
            }
            System.out.println("  Operated: " + ref + " with value " + val);
        }
    }

    // ==================== Cancel ====================

    private class CancelHandler implements CommandHandler {
        public String getName() { return "cancel"; }
        public String getDescription() { return "取消控制操作 (撤销 select/operate)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos"),
                new Param("value", "控制值", "true")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String ref = values.get("reference");
            boolean val = Boolean.parseBoolean(values.get("value"));
            CmsCancel asdu = new CmsCancel(MessageType.REQUEST).reference(ref)
                    .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val)).ctlNum(2).test(false);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Cancel failed");
                return;
            }
            System.out.println("  Cancelled: " + ref + " with value " + val);
        }
    }



    // ==================== TimeActivatedOperate ====================

    private class TimeActOperateHandler implements CommandHandler {
        public String getName() { return "time-act"; }
        public String getDescription() { return "定时执行控制操作"; }
        public List<Param> getParams() {
            return List.of(
                new Param("reference", "对象引用", "E1Q1SB1/XCBR1.Pos"),
                new Param("value", "控制值", "true")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String ref = values.get("reference");
            boolean val = Boolean.parseBoolean(values.get("value"));
            CmsTimeActivatedOperate asdu = new CmsTimeActivatedOperate(MessageType.REQUEST).reference(ref)
                    .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val)).ctlNum(4).test(false);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  TimeActivatedOperate failed");
                return;
            }
            System.out.println("  TimeActivatedOperate: " + ref + " with value " + val);
        }
    }



    // ==================== GetMSVCBValues ====================

    private class MsvcbValHandler implements CommandHandler {
        public String getName() { return "msvcb-val"; }
        public String getDescription() { return "读多播采样值控制块值"; }
        public List<Param> getParams() {
            return List.of(
                new Param("refs", "MSVCB 引用 (逗号分隔)", "C1/LLN0.Volt")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String[] refs = values.get("refs").split(",");
            CmsGetMSVCBValues asduReq = new CmsGetMSVCBValues(MessageType.REQUEST);
            for (String ref : refs) {
                asduReq.addReference(ref);
            }
            CmsApdu response = sendAndPrint(client, asduReq);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed");
                return;
            }

            CmsGetMSVCBValues asdu = (CmsGetMSVCBValues) response.getAsdu();
            System.out.println("  MSVCB entries (" + asdu.errorMsvcb.size() + "):");
            for (int i = 0; i < asdu.errorMsvcb.size(); i++) {
                var choice = asdu.errorMsvcb.get(i);
                if (choice.getSelectedIndex() == 0) {
                    System.out.println("    [" + i + "] ERROR: " + choice.error.get());
                } else {
                    var msvcb = choice.msvcb;
                    System.out.println("    [" + i + "] " + msvcb.msvCBRef.get()
                        + "  id=" + msvcb.msvID.get()
                        + "  ds=" + msvcb.datSet.get()
                        + "  rate=" + msvcb.smpRate.get());
                }
            }
        }
    }

    // ==================== SetMSVCBValues ====================

    private class SetMsvcbHandler implements CommandHandler {
        public String getName() { return "set-msvcb"; }
        public String getDescription() { return "设置多播采样值控制块值"; }
        public List<Param> getParams() {
            return List.of(
                new Param("ref", "MSVCB 引用", "C1/LLN0.Volt"),
                new Param("svEna", "启用 (true/false)", "true"),
                new Param("msvID", "SV ID (留空不修改)", ""),
                new Param("smpRate", "采样率", "4000")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }

            String ref = values.get("ref");
            boolean svEna = Boolean.parseBoolean(values.get("svEna"));

            CmsSetMSVCBValuesEntry entry = new CmsSetMSVCBValuesEntry();
            entry.reference.set(ref);
            entry.svEna.set(svEna);

            String msvID = values.get("msvID");
            if (!msvID.isEmpty()) {
                entry.msvID.set(msvID);
            }

            String smpRate = values.get("smpRate");
            if (!smpRate.isEmpty()) {
                entry.smpRate.set(Integer.parseInt(smpRate));
            }

            CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.REQUEST);
            asdu.addMsvcb(entry);
            CmsApdu response = sendAndPrint(client, asdu);
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Set " + ref + " OK");
            } else {
                System.out.println("  Set " + ref + " failed");
            }
        }
    }

    // ==================== Directory ====================

    private class ServerDirHandler implements CommandHandler {
        public String getName() { return "server-dir"; }
        public String getDescription() { return "读服务器目录"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            CmsGetServerDirectory reqAsdu = new CmsGetServerDirectory(MessageType.REQUEST)
                    .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
            CmsApdu response = sendAndPrint(client, reqAsdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetServerDirectory resAsdu = (CmsGetServerDirectory) response.getAsdu();
            if (resAsdu.reference().isEmpty()) {
                System.out.println(CmsColor.gray("  无数据"));
            } else {
                System.out.println("  Logical devices:");
                for (int i = 0; i < resAsdu.reference().size(); i++) {
                    System.out.println("    [" + i + "] " + resAsdu.reference().get(i).get());
                }
            }
        }
    }

    private class LdDirHandler implements CommandHandler {
        public String getName() { return "ld-dir"; }
        public String getDescription() { return "读逻辑设备目录"; }
        public List<Param> getParams() {
            return List.of(new Param("ldName", "逻辑设备名 (留空=全部)", "C1"));
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String ldName = values.get("ldName");
            CmsGetLogicalDeviceDirectory reqAsdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
            if (!ldName.isEmpty()) reqAsdu.ldName(ldName);
            CmsApdu response = sendAndPrint(client, reqAsdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
            if (asdu.lnReference().isEmpty()) {
                System.out.println(CmsColor.gray("  无数据"));
            } else {
                System.out.println("  Logical nodes" + (ldName.isEmpty() ? "" : " under " + ldName) + ":");
                for (int i = 0; i < asdu.lnReference().size(); i++) {
                    System.out.println("    [" + i + "] " + asdu.lnReference().get(i).get());
                }
            }
        }
    }

    private class LnDirHandler implements CommandHandler {
        public String getName() { return "ln-dir"; }
        public String getDescription() { return "读逻辑节点目录"; }
        public List<Param> getParams() {
            return List.of(
                new Param("target", "引用 (ldName 或 lnReference)", "C1"),
                new Param("acsi", "ACSI 类", "DATA_OBJECT", List.of(
                    new Param.EnumChoice("DATA_OBJECT", "数据对象"),
                    new Param.EnumChoice("DATA_SET", "数据集"),
                    new Param.EnumChoice("BRCB", "报告控制块（缓存）"),
                    new Param.EnumChoice("URCB", "报告控制块（非缓存）"),
                    new Param.EnumChoice("LCB", "日志控制块"),
                    new Param.EnumChoice("LOG", "日志"),
                    new Param.EnumChoice("SGCB", "定值组控制块"),
                    new Param.EnumChoice("GO_CB", "GOOSE 控制块"),
                    new Param.EnumChoice("MSV_CB", "采样值控制块")
                ))
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String target = values.get("target");
            int acsiClass = parseAcsi(values.get("acsi"));
            CmsGetLogicalNodeDirectory reqAsdu = new CmsGetLogicalNodeDirectory(MessageType.REQUEST);
            if (target.contains("/")) {
                reqAsdu.lnReference(target);
            } else {
                reqAsdu.ldName(target);
            }
            reqAsdu.acsiClass(new CmsACSIClass(acsiClass));
            CmsApdu response = sendAndPrint(client, reqAsdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
            if (asdu.referenceResponse().isEmpty()) {
                System.out.println(CmsColor.gray("  无数据"));
            } else {
                System.out.println("  Entries:");
                for (int i = 0; i < asdu.referenceResponse().size(); i++) {
                    System.out.println("    [" + i + "] " + asdu.referenceResponse().get(i).get());
                }
            }
        }
        private int parseAcsi(String s) {
            switch (s.toUpperCase()) {
                case "DATA_SET": return CmsACSIClass.DATA_SET;
                case "BRCB": return CmsACSIClass.BRCB;
                case "URCB": return CmsACSIClass.URCB;
                case "LCB": return CmsACSIClass.LCB;
                case "LOG": return CmsACSIClass.LOG;
                case "SGCB": return CmsACSIClass.SGCB;
                case "GO_CB": return CmsACSIClass.GO_CB;
                case "MSV_CB": return CmsACSIClass.MSV_CB;
                default: return CmsACSIClass.DATA_OBJECT;
            }
        }
    }

    // ==================== Data ====================

    private class GetAllValuesHandler implements CommandHandler {
        public String getName() { return "get-all-values"; }
        public String getDescription() { return "读所有数据值"; }
        public List<Param> getParams() {
            return List.of(
                new Param("target", "引用 (ldName 或 lnReference)", "C1"),
                new Param("fc", "功能约束 (留空=全部, 如 ST/MX/CO)", "")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String target = values.get("target");
            String fc = values.get("fc");
            CmsGetAllDataValues reqAsdu = new CmsGetAllDataValues(MessageType.REQUEST);
            if (target.contains("/")) {
                reqAsdu.lnReference(target);
            } else {
                reqAsdu.ldName(target);
            }
            if (fc != null && !fc.isEmpty()) reqAsdu.fc(fc);
            CmsApdu response = sendAndPrint(client, reqAsdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
            if (asdu.data().isEmpty()) {
                System.out.println(CmsColor.gray("  无数据"));
            } else {
                System.out.println("  Data values (" + asdu.data().size() + " entries):");
                for (int i = 0; i < asdu.data().size(); i++) {
                    CmsDataEntry entry = asdu.data().get(i);
                    System.out.println("    [" + i + "] " + entry.reference().get() + " = " + entry.value());
                }
            }
        }
    }

    private class GetAllDefHandler implements CommandHandler {
        public String getName() { return "get-all-def"; }
        public String getDescription() { return "读所有数据定义"; }
        public List<Param> getParams() {
            return List.of(
                new Param("target", "引用 (ldName 或 lnReference)", "C1"),
                new Param("fc", "功能约束 (留空=全部)", "")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String target = values.get("target");
            String fc = values.get("fc");
            CmsGetAllDataDefinition reqAsdu = new CmsGetAllDataDefinition(MessageType.REQUEST);
            if (target.contains("/")) {
                reqAsdu.lnReference(target);
            } else {
                reqAsdu.ldName(target);
            }
            if (fc != null && !fc.isEmpty()) reqAsdu.fc(fc);
            CmsApdu response = sendAndPrint(client, reqAsdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
            if (asdu.data().isEmpty()) {
                System.out.println(CmsColor.gray("  无数据"));
            } else {
                System.out.println("  Data definitions (" + asdu.data().size() + " entries):");
                for (int i = 0; i < asdu.data().size(); i++) {
                    CmsDataDefinitionEntry entry = asdu.data().get(i);
                    System.out.println("    [" + i + "] " + entry.reference().get()
                        + (entry.cdcType().get() != null ? "  cdc=" + entry.cdcType().get() : ""));
                }
            }
        }
    }

    private class GetAllCbHandler implements CommandHandler {
        public String getName() { return "get-all-cb"; }
        public String getDescription() { return "读所有控制块值"; }
        public List<Param> getParams() {
            return List.of(
                new Param("target", "引用 (ldName 或 lnReference)", "C1"),
                new Param("type", "控制块类型", "URCB", List.of(
                    new Param.EnumChoice("BRCB", "报告控制块（缓存）"),
                    new Param.EnumChoice("URCB", "报告控制块（非缓存）"),
                    new Param.EnumChoice("LCB", "日志控制块"),
                    new Param.EnumChoice("GO_CB", "GOOSE 控制块"),
                    new Param.EnumChoice("MSV_CB", "采样值控制块"),
                    new Param.EnumChoice("SGCB", "定值组控制块")
                ))
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String target = values.get("target");
            int acsiClass = parseAcsi(values.get("type"));
            CmsGetAllCBValues reqAsdu = new CmsGetAllCBValues(MessageType.REQUEST);
            if (target.contains("/")) {
                reqAsdu.lnReference(target);
            } else {
                reqAsdu.ldName(target);
            }
            reqAsdu.acsiClass = new CmsACSIClass(acsiClass);
            CmsApdu response = sendAndPrint(client, reqAsdu);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
            if (asdu.cbValue().isEmpty()) {
                System.out.println(CmsColor.gray("  无数据"));
            } else {
                System.out.println("  CB values (" + asdu.cbValue().size() + " entries):");
                for (int i = 0; i < asdu.cbValue().size(); i++) {
                    CmsCBValueEntry entry = asdu.cbValue().get(i);
                    System.out.println("    [" + i + "] " + entry.reference().get() + " = " + entry.value());
                }
            }
        }
        private int parseAcsi(String s) {
            switch (s.toUpperCase()) {
                case "BRCB": return CmsACSIClass.BRCB;
                case "URCB": return CmsACSIClass.URCB;
                case "LCB": return CmsACSIClass.LCB;
                case "GO_CB": return CmsACSIClass.GO_CB;
                case "MSV_CB": return CmsACSIClass.MSV_CB;
                case "SGCB": return CmsACSIClass.SGCB;
                default: return CmsACSIClass.URCB;
            }
        }
    }

    private static String bytesToHex(byte[] bytes, int len) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(len, bytes.length); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        return sb.toString();
    }

    // ==================== CLI 运行时设置 ====================

    private class CliSettingHandler implements CommandHandler {
        public String getName() { return "cli-setting"; }
        public String getDescription() { return "查看/修改 CLI 运行时配置"; }
        public List<Param> getParams() {
            return List.of(
                new Param("key", "配置项 (trace-pdu)", "trace-pdu"),
                new Param("value", "值 (true/false)", "")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) {
            String key = values.get("key");
            String value = values.get("value");

            if (key.isEmpty()) {
                printCurrentSettings();
                return;
            }

            if (value.isEmpty()) {
                printSetting(key);
                return;
            }

            switch (key) {
                case "trace-pdu" -> {
                    boolean v = Boolean.parseBoolean(value);
                    config.getCli().setTracePdu(v);
                    System.out.println(CmsColor.green("  trace-pdu = " + v));
                }
                default -> System.out.println(CmsColor.red("  Unknown setting: " + key));
            }
        }

        private void printCurrentSettings() {
            System.out.println("\n" + CmsColor.bold("CLI 运行时配置:"));
            System.out.println(CmsColor.gray("  ───────────────────────────────"));
            System.out.println("  trace-pdu    " + (config.getCli().isTracePdu()
                    ? CmsColor.green("true") : CmsColor.gray("false")));
            System.out.println(CmsColor.gray("\n  cli-setting <key> <value>  修改配置"));
            System.out.println(CmsColor.gray("  cli-setting <key>          查看单项"));
            System.out.println(CmsColor.gray("  cli-setting                查看全部"));
        }

        private void printSetting(String key) {
            switch (key) {
                case "trace-pdu" -> System.out.println("  trace-pdu = " + config.getCli().isTracePdu());
                default -> System.out.println(CmsColor.red("  Unknown setting: " + key));
            }
        }
    }

    private class ClearHandler implements CommandHandler {
        public String getName() { return "clear"; }
        public String getDescription() { return "清空控制台"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}