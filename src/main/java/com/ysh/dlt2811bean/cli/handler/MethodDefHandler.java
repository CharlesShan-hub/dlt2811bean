package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDefinition;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class MethodDefHandler extends AbstractServiceHandler {

    public MethodDefHandler(CliContext ctx) { super(ctx); }

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
        CmsApdu response = ctx.sendAndPrint(client, asdu);
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
