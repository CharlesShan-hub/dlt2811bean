package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.report.CmsGetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorUrcbChoice;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetURCBValuesHandler extends AbstractServiceHandler {

    public GetURCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_URCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "URCB 引用", "C1/LLN0.PosReport").type(Param.Type.URCB_REF)
        );
    }

    protected void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = values.get("ref");
        if (ref == null || ref.isEmpty()) return;
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST)
                .addReference(ref);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        CmsGetURCBValues resp = (CmsGetURCBValues) response.getAsdu();
        List<CmsErrorUrcbChoice> choices = resp.urcb.toList();
        CliPrinter.printList("URCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            CmsURCB urcb = item.value;
            return urcb.urcbRef.get()
                    + "  rptEna=" + urcb.rptEna.get()
                    + "  rptID=" + urcb.rptID.get()
                    + "  datSet=" + urcb.datSet.get()
                    + "  confRev=" + urcb.confRev.get()
                    + "  optFlds=" + urcb.optFlds.get()
                    + "  bufTm=" + urcb.bufTm.get()
                    + "  trgOps=" + urcb.trgOps.get()
                    + "  intgPd=" + urcb.intgPd.get()
                    + "  gi=" + urcb.gi.get()
                    + "  resv=" + urcb.resv.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());

        String ref = values.get("ref");
        for (CmsErrorUrcbChoice choice : choices) {
            if (choice.getSelectedIndex() == 1) {
                CmsURCB urcb = choice.value;
                ctx.updateUrcbAttribute(ref, "rptEna", String.valueOf(urcb.rptEna.get()));
                ctx.updateUrcbAttribute(ref, "rptID", urcb.rptID.get());
                ctx.updateUrcbAttribute(ref, "datSet", urcb.datSet.get());
                ctx.updateUrcbAttribute(ref, "confRev", String.valueOf(urcb.confRev.get()));
                ctx.updateUrcbAttribute(ref, "optFlds", String.valueOf(urcb.optFlds.get()));
                ctx.updateUrcbAttribute(ref, "bufTm", String.valueOf(urcb.bufTm.get()));
                ctx.updateUrcbAttribute(ref, "intgPd", String.valueOf(urcb.intgPd.get()));
                ctx.updateUrcbAttribute(ref, "gi", String.valueOf(urcb.gi.get()));
                ctx.updateUrcbAttribute(ref, "resv", String.valueOf(urcb.resv.get()));
            }
        }
    }
}