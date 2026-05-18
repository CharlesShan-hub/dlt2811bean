package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
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

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsGetURCBValues asdu = new CmsGetURCBValues(MessageType.REQUEST)
                .addReference(ref);

        CmsApdu response = sendAndVerify(client, asdu);

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
    }
}
