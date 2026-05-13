package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetEditSGValueHandler extends AbstractServiceHandler {

    public GetEditSGValueHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_EDIT_SG_VALUE); }

    public List<Param> getParams() {
        return List.of(
            new Param("ref", "数据引用", "C1/LLN0.SGCB"),
            Param.fc()
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String ref = values.get("ref");
        String fc = values.get("fc");
        String fcArg = fc.isEmpty() || "XX".equals(fc) ? null : fc;

        CmsApdu response = client.getEditSGValue(ref, fcArg);
        CmsGetEditSGValue resp = (CmsGetEditSGValue) response.getAsdu();
        List<CmsData<?>> dataList = resp.value.toList();
        CliPrinter.printList("Edit SG values (" + dataList.size() + " entries)", dataList, item -> {
            String raw = item.toString();
            if (raw.contains("CmsServiceError")) {
                return "Error: " + raw.replaceAll(".*=(CmsServiceError) ", "ServiceError ");
            }
            return raw;
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
