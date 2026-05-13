package com.ysh.dlt2811bean.cli.handler.sv;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.sv.CmsGetMSVCBValues;
import com.ysh.dlt2811bean.service.svc.sv.datatypes.CmsErrorMsvcbChoice;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetMSVCBValuesHandler extends AbstractServiceHandler {

    public GetMSVCBValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_MSVCB_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "MSVCB 引用", "C1/LLN0.Volt")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        CmsApdu response = client.getMSVCBValues(ref);
        CmsGetMSVCBValues resp = (CmsGetMSVCBValues) response.getAsdu();
        List<CmsErrorMsvcbChoice> choices = resp.errorMsvcb.toList();
        CliPrinter.printList("MSVCB values (" + choices.size() + " entries)", choices, item -> {
            if (item.getSelectedIndex() == 0) {
                return "Error: " + item.error.get();
            }
            return item.msvcb.msvCBRef.get() + "  svEna=" + item.msvcb.svEna.get()
                    + "  msvID=" + item.msvcb.msvID.get()
                    + "  datSet=" + item.msvcb.datSet.get()
                    + "  smpRate=" + item.msvcb.smpRate.get();
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
