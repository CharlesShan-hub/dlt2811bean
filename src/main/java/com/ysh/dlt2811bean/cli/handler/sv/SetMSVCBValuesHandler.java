package com.ysh.dlt2811bean.cli.handler.sv;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetMSVCBValuesHandler extends AbstractServiceHandler {

    public SetMSVCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_MSVCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "MSVCB 引用", "C1/LLN0.Volt"),
            new Param("svEna", "启用采样值 (true/false)", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        boolean svEna = Boolean.parseBoolean(values.get("svEna"));

        CmsSetMSVCBValuesEntry entry = new CmsSetMSVCBValuesEntry();
        entry.reference.set(ref);
        entry.svEna.set(svEna);

        CmsApdu response = client.setMSVCBValues(entry);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  MSVCB values set successfully"));
        }
    }
}
