package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.setting.CmsGetEditSGValue;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class GetEditSGValueHandler extends AbstractServiceHandler {

    public GetEditSGValueHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_EDIT_SG_VALUE); }

    protected List<Param> setParams() {
        return List.of(
            new Param("ref", "数据引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE),
            Param.fc()
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = stringVal("ref");
        String fc = stringVal("fc");

        CmsGetEditSGValue asdu = new CmsGetEditSGValue(MessageType.REQUEST).addData(ref, fc);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
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
