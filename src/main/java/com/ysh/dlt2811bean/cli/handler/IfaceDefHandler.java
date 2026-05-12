package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcInterfaceDefinition;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class IfaceDefHandler extends AbstractServiceHandler {

    public IfaceDefHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_RPC_INTERFACE_DEFINITION); }
    public List<Param> getParams() {
        return List.of(
            new Param("iface", "接口名", "IF1"),
            new Param("after", "参考点 (可选)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String iface = values.get("iface");
        String after = values.get("after");
        CmsGetRpcInterfaceDefinition asdu = new CmsGetRpcInterfaceDefinition(MessageType.REQUEST).interfaceName(iface);
        if (!after.isEmpty()) {
            asdu.referenceAfter(after);
        }
        CmsApdu response = sendAndVerify(client, asdu);

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
