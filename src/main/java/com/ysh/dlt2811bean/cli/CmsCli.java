package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllCBValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsACSIClass;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValueEntry;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDefinition;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDirectory;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDefinition;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
import com.ysh.dlt2811bean.service.svc.rpc.CmsRpcCall;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFile;
import com.ysh.dlt2811bean.service.svc.file.CmsSetFile;
import com.ysh.dlt2811bean.service.svc.file.CmsDeleteFile;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileDirectory;
import com.ysh.dlt2811bean.service.svc.control.CmsSelect;
import com.ysh.dlt2811bean.service.svc.control.CmsSelectWithValue;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.*;

public class CmsCli {

    private final CmsClient client = new CmsClient();
    private final Map<String, CommandHandler> handlers = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public CmsCli() {
        register(new HelpHandler());
        register(new ExitHandler());
        register(new ConnectHandler());
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
        register(new CommandTerminationHandler());
        register(new TimeActOperateHandler());
        register(new TimeActTermHandler());
        register(new MsvcbValHandler());
        register(new SetMsvcbHandler());
        register(new ServerDirHandler());
        register(new LdDirHandler());
        register(new LnDirHandler());
        register(new GetAllValuesHandler());
        register(new GetAllDefHandler());
        register(new GetAllCbHandler());
    }

    private void register(CommandHandler handler) {
        handlers.put(handler.getName(), handler);
    }

    public void run() {
        System.out.println("CMS CLI v1.0 — Type 'help' for commands, 'exit' to quit");
        while (running) {
            System.out.print("cms> ");
            String line = scanner.nextLine().trim().toLowerCase();

            if (line.isEmpty()) continue;

            CommandHandler handler = handlers.get(line);
            if (handler == null) {
                System.out.println("Unknown command: " + line + "  (type 'help' for available commands)");
                continue;
            }

            try {
                Map<String, String> values = new HashMap<>();
                for (Param param : handler.getParams()) {
                    String prompt = param.getPrompt();
                    if (param.getDefaultValue() != null) {
                        prompt += " [" + param.getDefaultValue() + "]";
                    }
                    prompt += ": ";
                    System.out.print(prompt);
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        if (param.getDefaultValue() != null) {
                            values.put(param.getName(), param.getDefaultValue());
                        } else if (param.isRequired()) {
                            System.out.println("  (skipped, using empty)");
                            values.put(param.getName(), "");
                        } else {
                            values.put(param.getName(), "");
                        }
                    } else {
                        values.put(param.getName(), input);
                    }
                }
                handler.execute(client, values);
            } catch (Exception e) {
                System.out.println("  ERROR: " + e.getMessage());
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        new CmsCli().run();
    }

    // ==================== Help & Exit ====================

    private class HelpHandler implements CommandHandler {
        public String getName() { return "help"; }
        public String getDescription() { return "显示帮助信息"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            System.out.println("\nAvailable commands:");
            for (CommandHandler h : handlers.values()) {
                if (h.getName().equals("help") || h.getName().equals("exit")) continue;
                System.out.println("  " + padRight(h.getName(), 18) + h.getDescription());
            }
            System.out.println("  " + padRight("help", 18) + "显示帮助信息");
            System.out.println("  " + padRight("exit", 18) + "退出程序");
            System.out.println();
        }
        private String padRight(String s, int n) {
            return String.format("%-" + n + "s", s);
        }
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
        public String getDescription() { return "连接到 CMS 服务器（可选自动协商+关联）"; }
        public List<Param> getParams() {
            return List.of(
                new Param("host", "服务器 IP", "127.0.0.1"),
                new Param("port", "服务器端口", String.valueOf(CmsConfigLoader.load().getServer().getPort())),
                new Param("apduSize", "APDU 大小（留空跳过协商）", String.valueOf(CmsConfigLoader.load().getNegotiate().getApduSize())),
                new Param("asduSize", "ASDU 大小", String.valueOf(CmsConfigLoader.load().getNegotiate().getAsduSize())),
                new Param("protocolVersion", "协议版本", String.valueOf(CmsConfigLoader.load().getNegotiate().getProtocolVersion())),
                new Param("ap", "AccessPoint", CmsConfigLoader.load().getClient().getDefaultAccessPoint()),
                new Param("ep", "Endpoint", CmsConfigLoader.load().getClient().getDefaultEp())
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            String host = values.get("host");
            int port = Integer.parseInt(values.get("port"));
            client.connect(host, port);
            System.out.println("  Connected to " + host + ":" + port);

            String apduSizeStr = values.get("apduSize");
            if (!apduSizeStr.isEmpty()) {
                int apduSize = Integer.parseInt(apduSizeStr);
                int asduSize = Integer.parseInt(values.get("asduSize"));
                long protocolVersion = Long.parseLong(values.get("protocolVersion"));
                String ap = values.get("ap");
                String ep = values.get("ep");

                client.setAccessPoint(ap, ep);

                CmsApdu negResponse = client.associateNegotiate(apduSize, asduSize, protocolVersion);
                if (negResponse == null || negResponse.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                    System.out.println("  Negotiate failed");
                    return;
                }
                System.out.println("  Negotiated OK");

                CmsApdu assocResponse = client.associate();
                if (assocResponse != null && assocResponse.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                    System.out.println("  Associated (ID=" + bytesToHex(client.getAssociationId(), 8) + "...)");
                } else {
                    System.out.println("  Associate failed");
                }
            }
        }
    }

    private class CloseHandler implements CommandHandler {
        public String getName() { return "close"; }
        public String getDescription() { return "断开连接"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            client.close();
            System.out.println("  Disconnected");
        }
    }

    private class StatusHandler implements CommandHandler {
        public String getName() { return "status"; }
        public String getDescription() { return "查看当前连接和关联状态"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) {
            boolean connected = client.isConnected();
            byte[] assocId = client.getAssociationId();
            System.out.println("  Connected: " + (connected ? "YES" : "NO"));
            System.out.println("  Associated: " + (assocId != null ? "YES (id=" + bytesToHex(assocId, 8) + "...)" : "NO"));
        }
    }

    // ==================== Association ====================

    private class NegotiateHandler implements CommandHandler {
        public String getName() { return "negotiate"; }
        public String getDescription() { return "协商服务参数 (连接后、关联前)"; }
        public List<Param> getParams() {
            CmsConfig config = CmsConfigLoader.load();
            return List.of(
                new Param("apduSize", "APDU 帧大小", String.valueOf(config.getNegotiate().getApduSize())),
                new Param("asduSize", "ASDU 大小", String.valueOf(config.getNegotiate().getAsduSize())),
                new Param("protocolVersion", "协议版本号", String.valueOf(config.getNegotiate().getProtocolVersion()))
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            int apduSize = Integer.parseInt(values.get("apduSize"));
            int asduSize = Integer.parseInt(values.get("asduSize"));
            long protocolVersion = Long.parseLong(values.get("protocolVersion"));
            CmsApdu response = client.associateNegotiate(apduSize, asduSize, protocolVersion);
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Negotiated!");
            } else {
                System.out.println("  Negotiate failed");
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
                new Param("ep", "EP", config.getClient().getDefaultEp())
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String ap = values.get("ap");
            String ep = values.get("ep");
            CmsApdu response = "E1Q1SB1".equals(ap) && "S1".equals(ep)
                ? client.associate()
                : client.associate(ap, ep);
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Associated!");
            } else {
                System.out.println("  Associate failed");
            }
        }
    }

    private class ReleaseHandler implements CommandHandler {
        public String getName() { return "release"; }
        public String getDescription() { return "释放关联"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            CmsApdu response = client.release();
            if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Released");
            } else {
                System.out.println("  Release failed");
            }
        }
    }

    private class AbortHandler implements CommandHandler {
        public String getName() { return "abort"; }
        public String getDescription() { return "异常中止关联"; }
        public List<Param> getParams() {
            return List.of(new Param("reason", "中止原因 (0=normal, 4=other)", "4"));
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected.");
                return;
            }
            int reason = Integer.parseInt(values.get("reason"));
            client.abort(reason);
            System.out.println("  Abort sent");
        }
    }

    private class TestHandler implements CommandHandler {
        public String getName() { return "test"; }
        public String getDescription() { return "发送心跳测试"; }
        public List<Param> getParams() { return List.of(); }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            CmsApdu response = client.test();
            System.out.println("  Test " + (response != null ? "OK" : "failed"));
        }
    }

    // ==================== RPC ====================

    private class RpcHandler implements CommandHandler {
        private final java.util.Map<String, byte[]> lastCallIds = new java.util.HashMap<>();

        public String getName() { return "rpc"; }
        public String getDescription() { return "远程过程调用 (ping/echo/iterate)"; }
        public List<Param> getParams() {
            return List.of(
                new Param("method", "方法名 (ping/echo/iterate)", "ping"),
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
                CmsApdu response = client.rpcCall("ping", new com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U(0));
                System.out.println("  RPC ping: " + (response.getMessageType() == MessageType.RESPONSE_POSITIVE ? "OK" : "failed"));
                return;
            }

            if (method.equals("iterate")) {
                byte[] lastId = lastCallIds.get("iterate");
                CmsApdu response;
                if (lastId != null) {
                    response = client.rpcCall("iterate", lastId);
                } else {
                    response = client.rpcCall("iterate", new com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U(0));
                }

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

            CmsApdu response = client.rpcCall("echo", new com.ysh.dlt2811bean.datatypes.string.CmsVisibleString(data));
            System.out.println("  RPC echo: " + (response.getMessageType() == MessageType.RESPONSE_POSITIVE ? "OK" : "failed"));
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

            CmsApdu response = client.getRpcInterfaceDirectory();
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
            CmsApdu response = after.isEmpty()
                ? client.getRpcInterfaceDefinition(iface)
                : client.getRpcInterfaceDefinition(iface, after);

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
            CmsApdu response;
            if (iface.isEmpty() && after.isEmpty()) {
                response = client.getRpcMethodDirectory();
            } else if (after.isEmpty()) {
                response = client.getRpcMethodDirectory(iface);
            } else {
                response = client.getRpcMethodDirectory(iface.isEmpty() ? null : iface, after);
            }

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
            CmsApdu response = client.getRpcMethodDefinition(refs.split(","));
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

            CmsApdu response = client.getFile(fileName, start);
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

            CmsApdu response = client.setFile(fileName, start, text.getBytes(java.nio.charset.StandardCharsets.UTF_8), eof);
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
            CmsApdu response = client.deleteFile(fileName);
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
            CmsApdu response = client.getFileAttributeValues(fileName);
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
            CmsApdu response = after.isEmpty()
                ? client.getFileDirectory(path)
                : client.getFileDirectory(path, after);

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
            CmsApdu response = client.select(ref);
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
            CmsApdu response = client.selectWithValue(ref, new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val));
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
            CmsApdu response = client.operate(ref, new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val));
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
            CmsApdu response = client.cancel(ref, new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val));
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Cancel failed");
                return;
            }
            System.out.println("  Cancelled: " + ref + " with value " + val);
        }
    }

    // ==================== CommandTermination ====================

    private class CommandTerminationHandler implements CommandHandler {
        public String getName() { return "cmd-term"; }
        public String getDescription() { return "命令终止通知"; }
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
            CmsApdu response = client.commandTermination(ref, new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val));
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  CommandTermination failed");
                return;
            }
            System.out.println("  CommandTermination: " + ref + " with value " + val);
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
            CmsApdu response = client.timeActivatedOperate(ref, new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val));
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  TimeActivatedOperate failed");
                return;
            }
            System.out.println("  TimeActivatedOperate: " + ref + " with value " + val);
        }
    }

    // ==================== TimeActivatedOperateTermination ====================

    private class TimeActTermHandler implements CommandHandler {
        public String getName() { return "time-act-term"; }
        public String getDescription() { return "终止定时控制操作"; }
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
            CmsApdu response = client.timeActivatedOperateTermination(ref, new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(val));
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  TimeActivatedOperateTermination failed");
                return;
            }
            System.out.println("  TimeActivatedOperateTermination: " + ref + " with value " + val);
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
            CmsApdu response = client.getMSVCBValues(refs);
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

            CmsApdu response = client.setMSVCBValues(entry);
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
            CmsApdu response = client.getServerDirectory();
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetServerDirectory asdu = (CmsGetServerDirectory) response.getAsdu();
            System.out.println("  Logical devices:");
            for (int i = 0; i < asdu.reference().size(); i++) {
                System.out.println("    [" + i + "] " + asdu.reference().get(i).get());
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
            CmsApdu response = ldName.isEmpty()
                ? client.getLogicalDeviceDirectory()
                : client.getLogicalDeviceDirectory(ldName);
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
            System.out.println("  Logical nodes" + (ldName.isEmpty() ? "" : " under " + ldName) + ":");
            for (int i = 0; i < asdu.lnReference().size(); i++) {
                System.out.println("    [" + i + "] " + asdu.lnReference().get(i).get());
            }
        }
    }

    private class LnDirHandler implements CommandHandler {
        public String getName() { return "ln-dir"; }
        public String getDescription() { return "读逻辑节点目录"; }
        public List<Param> getParams() {
            return List.of(
                new Param("target", "引用 (ldName 或 lnReference)", "C1"),
                new Param("acsi", "ACSI 类 (DATA_OBJECT/DATA_SET/BRCB/URCB/LCB/GO_CB/MSV_CB)", "DATA_OBJECT")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String target = values.get("target");
            int acsiClass = parseAcsi(values.get("acsi"));
            CmsApdu response;
            if (target.contains("/")) {
                response = client.getLogicalNodeDirectoryByLn(target, acsiClass);
            } else {
                response = client.getLogicalNodeDirectoryByLd(target, acsiClass);
            }
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetLogicalNodeDirectory asdu = (CmsGetLogicalNodeDirectory) response.getAsdu();
            System.out.println("  Entries:");
            for (int i = 0; i < asdu.referenceResponse().size(); i++) {
                System.out.println("    [" + i + "] " + asdu.referenceResponse().get(i).get());
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
            boolean hasFc = fc != null && !fc.isEmpty();
            CmsApdu response;
            if (target.contains("/")) {
                response = hasFc ? client.getAllDataValuesByLn(target, fc) : client.getAllDataValuesByLn(target);
            } else {
                response = hasFc ? client.getAllDataValuesByLd(target, fc) : client.getAllDataValuesByLd(target);
            }
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
            System.out.println("  Data values (" + asdu.data().size() + " entries):");
            for (int i = 0; i < asdu.data().size(); i++) {
                CmsDataEntry entry = asdu.data().get(i);
                System.out.println("    [" + i + "] " + entry.reference().get() + " = " + entry.value());
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
            boolean hasFc = fc != null && !fc.isEmpty();
            CmsApdu response;
            if (target.contains("/")) {
                response = hasFc ? client.getAllDataDefinitionByLn(target, fc) : client.getAllDataDefinitionByLn(target);
            } else {
                response = hasFc ? client.getAllDataDefinitionByLd(target, fc) : client.getAllDataDefinitionByLd(target);
            }
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
            System.out.println("  Data definitions (" + asdu.data().size() + " entries):");
            for (int i = 0; i < asdu.data().size(); i++) {
                CmsDataDefinitionEntry entry = asdu.data().get(i);
                System.out.println("    [" + i + "] " + entry.reference().get()
                    + (entry.cdcType().get() != null ? "  cdc=" + entry.cdcType().get() : ""));
            }
        }
    }

    private class GetAllCbHandler implements CommandHandler {
        public String getName() { return "get-all-cb"; }
        public String getDescription() { return "读所有控制块值"; }
        public List<Param> getParams() {
            return List.of(
                new Param("target", "引用 (ldName 或 lnReference)", "C1"),
                new Param("type", "类型 (URCB/BRCB/LCB/GO_CB/MSV_CB/SGCB)", "URCB")
            );
        }
        public void execute(CmsClient client, Map<String, String> values) throws Exception {
            if (!client.isConnected()) {
                System.out.println("  Not connected. Type 'connect' first.");
                return;
            }
            String target = values.get("target");
            int acsiClass = parseAcsi(values.get("type"));
            CmsApdu response;
            if (target.contains("/")) {
                response = client.getAllCBValuesByLn(target, acsiClass);
            } else {
                response = client.getAllCBValuesByLd(target, acsiClass);
            }
            if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
                System.out.println("  Request failed"); return;
            }
            CmsGetAllCBValues asdu = (CmsGetAllCBValues) response.getAsdu();
            System.out.println("  CB values (" + asdu.cbValue().size() + " entries):");
            for (int i = 0; i < asdu.cbValue().size(); i++) {
                CmsCBValueEntry entry = asdu.cbValue().get(i);
                System.out.println("    [" + i + "] " + entry.reference().get() + " = " + entry.value());
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
}