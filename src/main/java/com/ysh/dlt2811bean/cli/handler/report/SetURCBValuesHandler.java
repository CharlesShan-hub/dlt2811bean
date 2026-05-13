package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsSetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsSetURCBValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetURCBValuesHandler extends AbstractServiceHandler {

    public SetURCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_URCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "URCB 引用", "C1/LLN0.PosReport").type(Param.Type.REFERENCE),
            new Param("rptEna", "启用报告 (true/false)", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        boolean rptEna = Boolean.parseBoolean(values.get("rptEna"));

        CmsSetURCBValuesEntry entry = new CmsSetURCBValuesEntry();
        entry.reference.set(ref);
        entry.rptEna.set(rptEna);

        CmsSetURCBValues asdu = new CmsSetURCBValues(MessageType.REQUEST);
        asdu.addUrcb(entry);

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  URCB values set successfully"));
        }
    }
}
