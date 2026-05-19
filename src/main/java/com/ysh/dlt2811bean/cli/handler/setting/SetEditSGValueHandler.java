package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetEditSGValue;
import com.ysh.dlt2811bean.service.svc.setting.datatypes.CmsSetEditSGValueEntry;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SetEditSGValueHandler extends AbstractServiceHandler {

    public SetEditSGValueHandler(CliContext ctx) { super(ctx, ServiceInfo.SET_EDIT_SG_VALUE); }

    protected List<Param> setParams() {
        return List.of(
            new Param("ref", "数据引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE),
            new Param("value", "定值", "100")
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = stringVal("ref");
        int val = Integer.parseInt(stringVal("value"));

        CmsSetEditSGValue asdu = new CmsSetEditSGValue(MessageType.REQUEST);
        CmsSetEditSGValueEntry entry = new CmsSetEditSGValueEntry()
                .reference(ref)
                .value(new CmsInt32(val));
        asdu.data.add(entry);

        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String ref = stringVal("ref");
        String val = stringVal("value");
        ctx.updateSgcbAttribute(ref, "editValue", val);
        CliPrinter.success("Edit SG value set successfully");
    }
}
