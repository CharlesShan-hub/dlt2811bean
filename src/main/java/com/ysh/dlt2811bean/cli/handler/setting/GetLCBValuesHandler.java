package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLCBValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLcbChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetLCBValuesHandler extends AbstractServiceHandler {

    public GetLCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_LCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "LCB 引用", "C1/LLN0.Log")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getLCBValues(ref);
        CmsGetLCBValues resp = (CmsGetLCBValues) response.getAsdu();
        List<CmsErrorLcbChoice> choices = resp.lcb.toList();
        CliPrinter.printList("LCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.value.logRef.get() + "  logEna=" + item.value.logEna.get()
                    + "  intgPd=" + item.value.intgPd.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
