package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetBRCBValuesHandler extends AbstractServiceHandler {

    public GetBRCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_BRCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "BRCB 引用", "C1/LLN0.PosReport").type(Param.Type.BRCB_REF)
        );
    }

    protected void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = values.get("ref");
        if (ref == null || ref.isEmpty()) return;
        CmsGetBRCBValues asdu = new CmsGetBRCBValues(MessageType.REQUEST)
                .addBrcbReference(ref);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        CmsGetBRCBValues resp = (CmsGetBRCBValues) response.getAsdu();
        List<CmsErrorBrcbChoice> choices = resp.errorBrcb.toList();
        CliPrinter.printList("BRCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            CmsBRCB brcb = item.brcb;
            return brcb.brcbRef.get()
                    + "  rptEna=" + brcb.rptEna.get()
                    + "  rptID=" + brcb.rptID.get()
                    + "  datSet=" + brcb.datSet.get()
                    + "  confRev=" + brcb.confRev.get()
                    + "  optFlds=" + brcb.optFlds.get()
                    + "  bufTm=" + brcb.bufTm.get()
                    + "  trgOps=" + brcb.trgOps.get()
                    + "  intgPd=" + brcb.intgPd.get()
                    + "  gi=" + brcb.gi.get()
                    + "  purgeBuf=" + brcb.purgeBuf.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());

        String ref = values.get("ref");
        for (CmsErrorBrcbChoice choice : choices) {
            if (choice.getSelectedIndex() == 1) {
                CmsBRCB brcb = choice.brcb;
                ctx.updateBrcbAttribute(ref, "rptEna", String.valueOf(brcb.rptEna.get()));
                ctx.updateBrcbAttribute(ref, "rptID", brcb.rptID.get());
                ctx.updateBrcbAttribute(ref, "datSet", brcb.datSet.get());
                ctx.updateBrcbAttribute(ref, "confRev", String.valueOf(brcb.confRev.get()));
                ctx.updateBrcbAttribute(ref, "optFlds", String.valueOf(brcb.optFlds.get()));
                ctx.updateBrcbAttribute(ref, "bufTm", String.valueOf(brcb.bufTm.get()));
                ctx.updateBrcbAttribute(ref, "intgPd", String.valueOf(brcb.intgPd.get()));
                ctx.updateBrcbAttribute(ref, "gi", String.valueOf(brcb.gi.get()));
                ctx.updateBrcbAttribute(ref, "purgeBuf", String.valueOf(brcb.purgeBuf.get()));
            }
        }
    }
}