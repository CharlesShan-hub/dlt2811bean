package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetValues;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetDataSetValuesHandler extends AbstractServiceHandler {

    public GetDataSetValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_SET_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        CmsApdu response = client.getDataSetValues(dsRef);
        CmsGetDataSetValues resp = (CmsGetDataSetValues) response.getAsdu();
        List<CmsData<?>> dataList = resp.value.toList();
        CliPrinter.printList("Dataset values (" + dataList.size() + " entries)", dataList, item -> {
            String raw = item.toString();
            if (raw.contains("CmsServiceError")) {
                return "Error: " + raw.replaceAll(".*=(CmsServiceError) ", "ServiceError ");
            }
            return raw;
        });
        CliPrinter.printMoreFollows(resp.moreFollows.get());
    }
}
