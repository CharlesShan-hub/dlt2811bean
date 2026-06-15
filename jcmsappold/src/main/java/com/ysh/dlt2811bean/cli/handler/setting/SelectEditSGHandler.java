package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectEditSG;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SelectEditSGHandler extends AbstractServiceHandler {

    public SelectEditSGHandler(CliContext ctx) { super(ctx, ServiceInfo.SELECT_EDIT_SG); }

    protected List<Param> setParams() {
        return List.of(
            new Param("sgRef", "定值组控制块引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE),
            new Param("sgNum", "定值组号", "1")
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String sgRef = stringVal("sgRef");
        int sgNum = Integer.parseInt(stringVal("sgNum"));
        CmsSelectEditSG asdu = new CmsSelectEditSG(MessageType.REQUEST)
                .sgcbReference(sgRef)
                .settingGroupNumber(sgNum);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        String sgRef = stringVal("sgRef");
        int sgNum = Integer.parseInt(stringVal("sgNum"));
        ctx.updateSgcbAttribute(sgRef, "editSG", String.valueOf(sgNum));
        CliPrinter.success("Edit SG selected: SG" + sgNum);
    }
}
