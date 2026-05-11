package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class LdDirHandler extends AbstractServiceHandler {

    public LdDirHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "ld-dir"; }
    public String getDescription() { return "读逻辑设备目录"; }
    public List<Param> getParams() {
        return List.of(new Param("ldName", "逻辑设备名 (留空=全部)", "C1"));
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String ldName = values.get("ldName");
        CmsGetLogicalDeviceDirectory reqAsdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
        if (!ldName.isEmpty()) reqAsdu.ldName(ldName);
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        if (!printIfEmpty(asdu.lnReference().isEmpty())) {
            System.out.println("  Logical nodes" + (ldName.isEmpty() ? "" : " under " + ldName) + ":");
            for (int i = 0; i < asdu.lnReference().size(); i++) {
                System.out.println("    [" + i + "] " + asdu.lnReference().get(i).get());
            }
        }
    }
}
