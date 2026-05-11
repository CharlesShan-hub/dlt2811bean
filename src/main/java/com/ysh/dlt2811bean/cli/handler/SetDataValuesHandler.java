package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.info.FcInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.data.datatypes.CmsSetDataValuesEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class SetDataValuesHandler extends AbstractServiceHandler {

    public SetDataValuesHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "set-data-values"; }
    public String getDescription() { return "写数据值"; }
    public List<Param> getParams() {
        return List.of(
            new Param("refs", "数据引用 (逗号分隔)", "C1/LPHD1.Proxy.stVal"),
            new Param("value", "要设置的值", "true"),
            new Param("fc", "功能约束 (留空=不限制)", "XX", FcInfo.enumChoices())  
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String refs = values.get("refs");
        String val = values.get("value");
        String fc = values.get("fc");

        CmsSetDataValues asdu = new CmsSetDataValues(MessageType.REQUEST);
        for (String ref : refs.split(",")) {
            CmsSetDataValuesEntry entry = new CmsSetDataValuesEntry()
                .reference(ref.trim())
                .value(new CmsVisibleString(val).max(255));
            if (!fc.isEmpty()) {
                entry.fc(fc);
            }
            asdu.data.add(entry);
        }

        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
            System.out.println(CmsColor.green("  All data values set successfully"));
        } else if (response.getMessageType() == MessageType.RESPONSE_NEGATIVE) {
            CmsSetDataValues resp = (CmsSetDataValues) response.getAsdu();
            System.out.println(CmsColor.red("  Some or all values failed:"));
            String[] refArr = refs.split(",");
            for (int i = 0; i < resp.result.size() && i < refArr.length; i++) {
                int errorCode = resp.result.get(i).get();
                if (errorCode != CmsServiceError.NO_ERROR) {
                    System.out.println("    " + CmsColor.red(refArr[i].trim()) + " -> error " + errorCode);
                }
            }
        }
    }
}
