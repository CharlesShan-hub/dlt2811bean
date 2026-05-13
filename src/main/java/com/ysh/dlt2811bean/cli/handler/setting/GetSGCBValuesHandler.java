package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetSGCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorSgcbChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetSGCBValuesHandler extends AbstractServiceHandler {

    public GetSGCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_SGCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "定值组控制块引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getSGCBValues(ref);
        CmsGetSGCBValues resp = (CmsGetSGCBValues) response.getAsdu();
        List<CmsErrorSgcbChoice> choices = resp.errorSgcb.toList();
        CliPrinter.printList("SGCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.sgcb.sgcbRef.get() + "  SG=" + item.sgcb.actSG.get();
        });
    }
}
