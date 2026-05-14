package com.ysh.dlt2811bean.cli.handler.dataset;

import com.ysh.dlt2811bean.cli.handler.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.dataset.CmsSetDataSetValues;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetDataSetValuesHandler extends AbstractServiceHandler {

    public SetDataSetValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_DATA_SET_VALUES); }

    public List<Param> getParams() {
        return List.of(
            new Param("dsRef", "数据集引用", "C1/LLN0.Positions").type(Param.Type.DS_REF),
            new Param("value", "要设置的值 (逗号分隔，按数据集成员顺序)", "true"),
            new Param("referenceAfter", "从指定成员之后开始设置", "").type(Param.Type.DS_REF)
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String dsRef = values.get("dsRef");
        String val = values.get("value");
        String after = values.get("referenceAfter");

        if (val == null || val.trim().isEmpty()) {
            System.out.println(CmsColor.red("  Error: value is required"));
            return;
        }

        String[] valArr = val.split(",");

        CmsSetDataSetValues asdu = new CmsSetDataSetValues(MessageType.REQUEST)
                .datasetReference(dsRef);

        if (after != null && !after.trim().isEmpty()) {
            asdu.referenceAfter(after.trim());
        }

        for (String v : valArr) {
            asdu.addMemberValue(new com.ysh.dlt2811bean.datatypes.string.CmsUtf8String(v.trim()).max(255));
        }

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  Dataset values set successfully"));
        } else if (response.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
            System.out.println(CmsColor.red("  Failed to set some dataset values"));
        }
    }
}
