package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetBRCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorBrcbChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetBRCBValuesHandler extends AbstractServiceHandler {

    public GetBRCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_BRCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "BRCB 引用", "C1/LLN0.PosReport")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getBRCBValues(ref);
        CmsGetBRCBValues resp = (CmsGetBRCBValues) response.getAsdu();
        List<CmsErrorBrcbChoice> choices = resp.errorBrcb.toList();
        CliPrinter.printList("BRCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.brcb.brcbRef.get() + "  rptEna=" + item.brcb.rptEna.get()
                    + "  datSet=" + item.brcb.datSet.get()
                    + "  intgPd=" + item.brcb.intgPd.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
