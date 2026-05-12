package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class MsvcbValHandler extends AbstractServiceHandler {

    public MsvcbValHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_MSVCB_VALUES); }
    public List<Param> getParams() {
        return List.of(
            new Param("refs", "MSVCB 引用 (逗号分隔)", "C1/LLN0.Volt")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String[] refs = values.get("refs").split(",");
        CmsGetMSVCBValues asduReq = new CmsGetMSVCBValues(MessageType.REQUEST);
        for (String ref : refs) {
            asduReq.addReference(ref);
        }
        CmsApdu response = sendAndVerify(client, asduReq);

        CmsGetMSVCBValues asdu = (CmsGetMSVCBValues) response.getAsdu();
        System.out.println("  MSVCB entries (" + asdu.errorMsvcb.size() + "):");
        for (int i = 0; i < asdu.errorMsvcb.size(); i++) {
            var choice = asdu.errorMsvcb.get(i);
            if (choice.getSelectedIndex() == 0) {
                System.out.println("    [" + i + "] ERROR: " + choice.error.get());
            } else {
                var msvcb = choice.msvcb;
                System.out.println("    [" + i + "] " + msvcb.msvCBRef.get()
                    + "  id=" + msvcb.msvID.get()
                    + "  ds=" + msvcb.datSet.get()
                    + "  rate=" + msvcb.smpRate.get());
            }
        }
    }
}
