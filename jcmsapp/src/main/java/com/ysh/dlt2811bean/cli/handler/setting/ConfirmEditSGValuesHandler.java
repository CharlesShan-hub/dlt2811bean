package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.setting.CmsConfirmEditSGValues;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class ConfirmEditSGValuesHandler extends AbstractServiceHandler {

    public ConfirmEditSGValuesHandler(CliContext ctx) { super(ctx, ServiceInfo.CONFIRM_EDIT_SG_VALUES); }

    protected List<Param> setParams() {
        return List.of(
            new Param("sgRef", "定值组控制块引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE)
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String sgRef = stringVal("sgRef");
        CmsConfirmEditSGValues asdu = new CmsConfirmEditSGValues(MessageType.REQUEST).sgcbReference(sgRef);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String sgRef = stringVal("sgRef");
        ctx.updateSgcbAttribute(sgRef, "cnfEdit", "true");
        CliPrinter.success("Edit SG values confirmed");
    }
}
