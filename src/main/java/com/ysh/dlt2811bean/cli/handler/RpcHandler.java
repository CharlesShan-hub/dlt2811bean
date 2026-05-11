package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsRpcCall;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class RpcHandler implements CommandHandler {

    private final CliContext ctx;
    private final java.util.Map<String, byte[]> lastCallIds = new java.util.HashMap<>();

    public RpcHandler(CliContext ctx) { this.ctx = ctx; }

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
            CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST).method("ping").reqData(new CmsInt32U(0));
            CmsApdu response = ctx.sendAndPrint(client, asdu);
            System.out.println("  RPC ping: " + (response.getMessageType() == MessageType.RESPONSE_POSITIVE ? "OK" : "failed"));
            return;
        }

        if (method.equals("iterate")) {
            byte[] lastId = lastCallIds.get("iterate");
            CmsRpcCall asdu;
            if (lastId != null) {
                asdu = new CmsRpcCall(MessageType.REQUEST).method("iterate").callID(lastId);
            } else {
                asdu = new CmsRpcCall(MessageType.REQUEST).method("iterate").reqData(new CmsInt32U(0));
            }
            CmsApdu response = ctx.sendAndPrint(client, asdu);

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

        CmsRpcCall asdu = new CmsRpcCall(MessageType.REQUEST).method("echo").reqData(new CmsVisibleString(data));
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        System.out.println("  RPC echo: " + (response.getMessageType() == MessageType.RESPONSE_POSITIVE ? "OK" : "failed"));
    }
}
