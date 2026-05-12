package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LdDirHandler extends AbstractServiceHandler {

    public LdDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_LOGIC_DEVICE_DIRECTORY); }
    
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
            List<String> lnNames = asdu.lnReference().stream().map(r -> r.get()).collect(Collectors.toList());
            String prefix = ldName.isEmpty() ? "" : ldName + "/";
            printList("Logical nodes" + (ldName.isEmpty() ? "" : " under " + ldName), lnNames,
                    item -> prefix + item + lnClassName(prefix + item));
        }
    }
}
