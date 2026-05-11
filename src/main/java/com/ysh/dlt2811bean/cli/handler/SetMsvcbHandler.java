package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsSetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsSetMSVCBValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class SetMsvcbHandler extends AbstractServiceHandler {

    public SetMsvcbHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "set-msvcb"; }
    public String getDescription() { return "设置多播采样值控制块值"; }
    public List<Param> getParams() {
        return List.of(
            new Param("ref", "MSVCB 引用", "C1/LLN0.Volt"),
            new Param("svEna", "启用 (true/false)", "true"),
            new Param("msvID", "SV ID (留空不修改)", ""),
            new Param("smpRate", "采样率", "4000")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        boolean svEna = Boolean.parseBoolean(values.get("svEna"));

        CmsSetMSVCBValuesEntry entry = new CmsSetMSVCBValuesEntry();
        entry.reference.set(ref);
        entry.svEna.set(svEna);

        String msvID = values.get("msvID");
        if (!msvID.isEmpty()) {
            entry.msvID.set(msvID);
        }

        String smpRate = values.get("smpRate");
        if (!smpRate.isEmpty()) {
            entry.smpRate.set(Integer.parseInt(smpRate));
        }

        CmsSetMSVCBValues asdu = new CmsSetMSVCBValues(MessageType.REQUEST);
        asdu.addMsvcb(entry);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Set " + ref + " OK");
        } else {
            System.out.println("  Set " + ref + " failed");
        }
    }
}
