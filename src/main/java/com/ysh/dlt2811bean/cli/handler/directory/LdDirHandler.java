package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.CliPrinter;
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
        return List.of(
            new Param("ldName", "逻辑设备名 (留空=全部)", "").type(Param.Type.LD_NAME),
            new Param("referenceAfter", "起始引用 (留空=从头)", "").type(Param.Type.REFERENCE)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String ldName = values.get("ldName");
        String after = values.get("referenceAfter");

        if (ldName.contains("/")) {
            String[] parts = ldName.split("/", 2);
            ldName = parts[0];
            after = parts[1];
        } else if (after != null && after.contains("/")) {
            String[] parts = after.split("/", 2);
            if (ldName.isEmpty()) ldName = parts[0];
            after = parts[1];
        }

        CmsGetLogicalDeviceDirectory reqAsdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
        if (!ldName.isEmpty()) reqAsdu.ldName(ldName);
        if (after != null && !after.isEmpty()) reqAsdu.referenceAfter.set(after);

        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        if (!CliPrinter.printIfEmpty(asdu.lnReference().isEmpty())) {
            List<String> lnNames = asdu.lnReference().stream().map(r -> r.get()).collect(Collectors.toList());
            String titlePrefix = ldName.isEmpty() ? "" : " under " + ldName;
            String displayPrefix = ldName.isEmpty() ? "" : ldName + "/";
            CliPrinter.printList("Logical nodes" + titlePrefix, lnNames,
                    item -> displayPrefix + item + CliPrinter.lnClassName(displayPrefix + item));
        }
    }
}
