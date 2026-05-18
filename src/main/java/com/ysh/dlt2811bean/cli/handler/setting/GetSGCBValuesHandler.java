package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetSGCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorSgcbChoice;
import com.ysh.dlt2811bean.cli.handler.common.Param;
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
        if (response.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
            System.out.println(CmsColor.red("  Server error: " + response.getAsdu()));
            return;
        }
        CmsGetSGCBValues resp = (CmsGetSGCBValues) response.getAsdu();
        List<CmsErrorSgcbChoice> choices = resp.errorSgcb.toList();
        CliPrinter.printList("SGCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.sgcb.sgcbRef.get()
                + "  actSG=" + item.sgcb.actSG.get()
                + "  editSG=" + item.sgcb.editSG.get()
                + "  numOfSG=" + item.sgcb.numOfSG.get()
                + "  cnfEdit=" + item.sgcb.cnfEdit.get();
        });
    }
}
