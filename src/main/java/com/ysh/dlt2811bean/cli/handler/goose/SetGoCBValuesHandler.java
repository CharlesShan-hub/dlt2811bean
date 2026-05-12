package com.ysh.dlt2811bean.cli.handler.goose;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsSetGoCBValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetGoCBValuesHandler extends AbstractServiceHandler {

    public SetGoCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_GOCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "GoCB 引用", "C1/LLN0.ItlPositions"),
            new Param("goEna", "启用 GOOSE (true/false)", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        boolean goEna = Boolean.parseBoolean(values.get("goEna"));

        CmsSetGoCBValuesEntry entry = new CmsSetGoCBValuesEntry();
        entry.reference.set(ref);
        entry.goEna.set(goEna);

        CmsApdu response = client.setGoCBValues(entry);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  GoCB values set successfully"));
        }
    }
}
