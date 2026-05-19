package com.ysh.dlt2811bean.cli.handler.directory;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetAllValuesHandler extends AbstractServiceHandler {

    private String target;

    public GetAllValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_VALUES); }

    @Override
    protected List<Param> setParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1").type(Param.Type.LN_REF),
            Param.fc("功能约束"),
            new Param("referenceAfter", "起始引用 (留空=从头)", "").type(Param.Type.REFERENCE)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {

        target = stringVal("target");
        String fc = stringVal("fc");
        String after = stringVal("referenceAfter");
        CmsGetAllDataValues reqAsdu = new CmsGetAllDataValues(MessageType.REQUEST);

        if (target.contains("/")) reqAsdu.lnReference(target);
        else reqAsdu.ldName(target);
        if (!fc.isEmpty()) reqAsdu.fc(fc);
        if (!after.isEmpty()) reqAsdu.referenceAfter(after);
        response = client.send(reqAsdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            CmsGetAllDataValues errAsdu = (CmsGetAllDataValues) response.getAsdu();
            CliPrinter.error("GetAllDataValues failed: " + errAsdu.serviceError);
            return;
        }
    }

    public void afterExecute(CmsClient client, Map<String, String> values) throws Exception{
        
        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        List<CmsDataEntry> entries = asdu.data().toList();
        CliPrinter.printList("Data values (" + entries.size() + " entries)", entries,
                item -> {
                    String ref = item.reference().get();
                    CmsData<?> data = item.value();
                    String valueStr = CliPrinter.formatCmsDataValue(data);
                    return ref + " = " + valueStr;
                });
        if (target.contains("/")) {
            Map<String, Object> das = ctx.addDataObjectGroup(target);
            entries.forEach(e -> ctx.addDataAttribute(das, e.reference().get(), CliPrinter.formatCmsDataValue(e.value())));
        }
    }

}
