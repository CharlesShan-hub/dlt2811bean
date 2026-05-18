package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LdDirHandler extends AbstractServiceHandler {

    public LdDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_LOGIC_DEVICE_DIRECTORY); }

    protected List<Param> setParams() {
        return List.of(
            new Param("ldName", "逻辑设备名 (留空=全部)", "").type(Param.Type.LD_NAME),
            new Param("referenceAfter", "起始引用 (留空=从头)", "").type(Param.Type.LN_NAME)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ldName = stringVal("ldName");
        String after = stringVal("referenceAfter");

        if (ldName.contains("/")) {
            String[] parts = ldName.split("/", 2);
            ldName = parts[0];
            after = parts[1];
        } else if (after.contains("/")) {
            String[] parts = after.split("/", 2);
            if (ldName.isEmpty()) ldName = parts[0];
            after = parts[1];
        }

        response = client.getLogicalDeviceDirectory(ldName, after);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            CliPrinter.error("GetLogicalDeviceDirectory failed");
            return;
        }

        String ldName = stringVal("ldName");
        String after = stringVal("referenceAfter");
        if (ldName.contains("/")) {
            ldName = ldName.split("/", 2)[0];
        } else if (after.contains("/")) {
            ldName = ldName.isEmpty() ? after.split("/", 2)[0] : ldName;
        }

        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        if (!CliPrinter.printIfEmpty(asdu.lnReference().isEmpty())) {
            List<String> lnNames = asdu.lnReference().stream().map(r -> r.get()).collect(Collectors.toList());
            String titlePrefix = ldName.isEmpty() ? "" : " under " + ldName;
            String displayPrefix = ldName.isEmpty() ? "" : ldName + "/";
            CliPrinter.printList("Logical nodes" + titlePrefix, lnNames,
                    item -> displayPrefix + item + CliPrinter.lnClassName(displayPrefix + item));
        }
        if (!ldName.isEmpty()) {
            java.util.Map<String, java.util.Map<String, java.util.Map<String, Object>>> lnMap = ctx.ldEntry(ldName);
            boolean hasExistingData = lnMap.values().stream().anyMatch(m -> !m.isEmpty());
            if (!hasExistingData) {
                for (int i = 0; i < asdu.lnReference().size(); i++) {
                    lnMap.putIfAbsent(asdu.lnReference().get(i).get(), new java.util.LinkedHashMap<>());
                }
            }
        }
    }
}
