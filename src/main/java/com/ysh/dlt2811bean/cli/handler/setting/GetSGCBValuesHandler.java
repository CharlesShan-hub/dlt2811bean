package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
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
            CmsSGCB sgcb = item.sgcb;
            String ref = sgcb.sgcbRef.get();
            ctx.updateSgcbAttribute(ref, "sgcbName", sgcb.sgcbName.get());
            ctx.updateSgcbAttribute(ref, "numOfSG", String.valueOf(sgcb.numOfSG.get()));
            ctx.updateSgcbAttribute(ref, "actSG", String.valueOf(sgcb.actSG.get()));
            ctx.updateSgcbAttribute(ref, "editSG", String.valueOf(sgcb.editSG.get()));
            ctx.updateSgcbAttribute(ref, "cnfEdit", String.valueOf(sgcb.cnfEdit.get()));
            ctx.updateSgcbAttribute(ref, "lActTm", String.valueOf(sgcb.lActTm.secondsSinceEpoch.get()));
            ctx.updateSgcbAttribute(ref, "resvTms", String.valueOf(sgcb.resvTms.get()));
            return sgcb.sgcbRef.get()
                + "  sgcbName=" + sgcb.sgcbName.get()
                + "  actSG=" + sgcb.actSG.get()
                + "  editSG=" + sgcb.editSG.get()
                + "  numOfSG=" + sgcb.numOfSG.get()
                + "  cnfEdit=" + sgcb.cnfEdit.get()
                + "  lActTm=" + sgcb.lActTm.secondsSinceEpoch.get()
                + "  resvTms=" + sgcb.resvTms.get();
        });
    }
}
