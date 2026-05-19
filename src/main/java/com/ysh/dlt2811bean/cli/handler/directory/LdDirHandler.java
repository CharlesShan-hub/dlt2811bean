package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsScalar;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.cli.handler.common.Param;
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

        CmsGetLogicalDeviceDirectory asdu = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
        if (!ldName.isEmpty()) asdu.ldName(ldName);
        if (!after.isEmpty()) asdu.referenceAfter(after);
        response = client.send(asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            CliPrinter.error("GetLogicalDeviceDirectory failed");
            return;
        }
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ldName = stringVal("ldName");

        CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
        if (!CliPrinter.printIfEmpty(asdu.lnReference().isEmpty())) {
            List<String> lnNames = asdu.lnReference().stream().map(AbstractCmsScalar::get).collect(Collectors.toList());
            String titlePrefix = ldName.isEmpty() ? "" : " under " + ldName;
            String displayPrefix = ldName.isEmpty() ? "" : ldName + "/";
            CliPrinter.printList("LN(Logical Nodes)" + titlePrefix, lnNames,
                    item -> displayPrefix + item + CliPrinter.lnClassName(displayPrefix + item));
        }
        if (!ldName.isEmpty()) {
            asdu.lnReference().stream().map(r -> ldName + "/" + r.get()).forEach(ctx::addLogicNode);
        }
    }
}
