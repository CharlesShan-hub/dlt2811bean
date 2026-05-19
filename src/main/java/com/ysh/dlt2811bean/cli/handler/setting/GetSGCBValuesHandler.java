package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetSGCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorSgcbChoice;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetSGCBValuesHandler extends AbstractServiceHandler {

    public GetSGCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_SGCB_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("ref", "定值组控制块引用（多个用逗号分隔）", "C1/LLN0.SGCB").type(Param.Type.REFERENCE)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = stringVal("ref");
        CmsGetSGCBValues asdu = new CmsGetSGCBValues(MessageType.REQUEST);
        for (String r : ref.split(",")) 
            asdu.addSgcbReference(r.trim());
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
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
