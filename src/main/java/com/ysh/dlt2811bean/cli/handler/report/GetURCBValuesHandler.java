package com.ysh.dlt2811bean.cli.handler.report;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsGetURCBValues;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsErrorUrcbChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetURCBValuesHandler extends AbstractServiceHandler {

    public GetURCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_URCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "URCB 引用", "C1/LLN0.PosReport")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getURCBValues(ref);
        CmsGetURCBValues resp = (CmsGetURCBValues) response.getAsdu();
        List<CmsErrorUrcbChoice> choices = resp.urcb.toList();
        printList("URCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.value.rptID.get() + "  rptEna=" + item.value.rptEna.get()
                    + "  datSet=" + item.value.datSet.get()
                    + "  intgPd=" + item.value.intgPd.get();
        });
        printMoreFollows(resp.moreFollows.get());
    }
}
