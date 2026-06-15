package com.ysh.dlt2811bean.cli.handler.data;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsGetDataValuesEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDataValuesHandler extends AbstractServiceHandler {

    public GetDataValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_DATA_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/MMXU1.Volts").type(Param.Type.DA_REF),
            Param.fc()
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String refs = stringVal("refs");
        String fc = stringVal("fc");

        String[] allRefs = refs.split(",");
        int startIndex = 0;
        List<CmsData<?>> allData = new ArrayList<>();
        int batchCount = 0;

        while (startIndex < allRefs.length) {
            CmsGetDataValues asdu = new CmsGetDataValues(MessageType.REQUEST);
            for (int i = startIndex; i < allRefs.length; i++) {
                CmsGetDataValuesEntry entry = new CmsGetDataValuesEntry().reference(allRefs[i].trim());
                if (!fc.isEmpty() && !"XX".equals(fc)) {
                    entry.fc(fc);
                }
                asdu.data.add(entry);
            }

            response = sendAndVerify(client, asdu);

            CmsGetDataValues resp = (CmsGetDataValues) response.getAsdu();
            List<CmsData<?>> dataList = resp.value.toList();
            allData.addAll(dataList);
            startIndex += dataList.size();
            batchCount++;

            if (!resp.moreFollows.get()) {
                break;
            }
        }

        result = allData;
        resultExtra = batchCount;
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String refs = stringVal("refs");
        String[] allRefs = refs.split(",");
        @SuppressWarnings("unchecked")
        List<CmsData<?>> allData = (List<CmsData<?>>) result;
        int batchCount = (int) resultExtra;

        CliPrinter.printList("Data values (" + allData.size() + " entries)", allData, item -> {
            String raw = item.toString();
            if (raw.contains("CmsServiceError")) {
                return CmsColor.red("Error: " + raw.replaceAll(".*=(CmsServiceError) ", "ServiceError "));
            }
            return raw;
        });

        if (batchCount > 1) {
            CliPrinter.printGray("  (fetched in " + batchCount + " batches)");
        }

        java.util.stream.IntStream.range(0, Math.min(allRefs.length, allData.size()))
                .forEach(i -> ctx.addDataObjectValue(allRefs[i].trim(), allData.get(i).toString()));
    }
}
