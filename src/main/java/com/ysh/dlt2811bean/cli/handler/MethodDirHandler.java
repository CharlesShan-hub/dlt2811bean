package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.rpc.CmsGetRpcMethodDirectory;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class MethodDirHandler extends AbstractServiceHandler {

    public MethodDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_RPC_METHOD_DIRECTORY); }
    public List<Param> getParams() {
        return List.of(
            new Param("iface", "接口名 (可选)", ""),
            new Param("after", "参考点 (可选)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String iface = values.get("iface");
        String after = values.get("after");
        CmsGetRpcMethodDirectory asdu = new CmsGetRpcMethodDirectory(MessageType.REQUEST);
        if (!iface.isEmpty()) {
            asdu.interfaceName(iface);
        }
        if (!after.isEmpty()) {
            asdu.referenceAfter(after);
        }
        CmsApdu response = sendAndVerify(client, asdu);

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
