package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetLogStatusValues;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsErrorLogStatusChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetLogStatusValuesHandler extends AbstractServiceHandler {

    public GetLogStatusValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_LOG_STATUS_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "LCB 引用", "C1/LLN0.Log")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getLogStatusValues(ref);
        CmsGetLogStatusValues resp = (CmsGetLogStatusValues) response.getAsdu();
        List<CmsErrorLogStatusChoice> choices = resp.log.toList();
        CliPrinter.printList("Log status (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return "oldEntry=" + item.value.oldEntr + "  newEntry=" + item.value.newEntr
                    + "  oldTime=" + item.value.oldEntrTm.msOfDay.get() + "/" + item.value.oldEntrTm.daysSince1984.get()
                    + "  newTime=" + item.value.newEntrTm.msOfDay.get() + "/" + item.value.newEntrTm.daysSince1984.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
