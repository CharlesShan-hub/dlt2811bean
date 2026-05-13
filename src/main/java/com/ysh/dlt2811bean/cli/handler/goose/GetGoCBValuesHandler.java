package com.ysh.dlt2811bean.cli.handler.goose;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.goose.CmsGetGoCBValues;
import com.ysh.dlt2811bean.service.svc.goose.datatypes.CmsErrorGocbChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetGoCBValuesHandler extends AbstractServiceHandler {

    public GetGoCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_GOCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "GoCB 引用", "C1/LLN0.ItlPositions")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getGoCBValues(ref);
        CmsGetGoCBValues resp = (CmsGetGoCBValues) response.getAsdu();
        List<CmsErrorGocbChoice> choices = resp.errorGocb.toList();
        CliPrinter.printList("GoCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.gocb.goCBRef.get() + "  goID=" + item.gocb.goID.get()
                    + "  datSet=" + item.gocb.datSet.get()
                    + "  goEna=" + item.gocb.goEna.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
