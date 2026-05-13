package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetLCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetLCBValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetLCBValuesHandler extends AbstractServiceHandler {

    public SetLCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_LCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "LCB 引用", "C1/LLN0.Log"),
            new Param("logEna", "启用日志 (true/false)", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        boolean logEna = Boolean.parseBoolean(values.get("logEna"));

        CmsSetLCBValuesEntry entry = new CmsSetLCBValuesEntry();
        entry.reference.set(ref);
        entry.logEna.set(logEna);

        CmsSetLCBValues asdu = new CmsSetLCBValues(MessageType.REQUEST);
        asdu.addLcb(entry);

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  LCB values set successfully"));
        }
    }
}
