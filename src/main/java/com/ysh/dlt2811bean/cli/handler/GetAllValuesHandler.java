package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataEntry;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class GetAllValuesHandler extends AbstractServiceHandler {

    public GetAllValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_ALL_DATA_VALUES); }
    public List<Param> getParams() {
        return List.of(
            new Param("target", "引用 (ldName 或 lnReference)", "C1"),
            Param.fc("功能约束 (留空=全部)")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);
        String target = values.get("target");
        String fc = values.get("fc");
        CmsGetAllDataValues reqAsdu = new CmsGetAllDataValues(MessageType.REQUEST);
        if (target.contains("/")) {
            reqAsdu.lnReference(target);
        } else {
            reqAsdu.ldName(target);
        }
        if (fc != null && !fc.isEmpty()) reqAsdu.fc(fc);
        CmsApdu response = sendAndVerify(client, reqAsdu);
        CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
        if (!printIfEmpty(asdu.data().isEmpty())) {
            System.out.println("  Data values (" + asdu.data().size() + " entries):");
            for (int i = 0; i < asdu.data().size(); i++) {
                CmsDataEntry entry = asdu.data().get(i);
                System.out.println("    [" + i + "] " + entry.reference().get() + " = " + entry.value());
            }
        }
    }
}
