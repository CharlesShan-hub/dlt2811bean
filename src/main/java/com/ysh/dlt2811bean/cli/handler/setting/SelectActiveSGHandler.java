package com.ysh.dlt2811bean.cli.handler.setting;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.setting.CmsSelectActiveSG;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import java.util.List;
import java.util.Map;

public class SelectActiveSGHandler extends AbstractServiceHandler {

    public SelectActiveSGHandler(CliContext ctx) { super(ctx, ServiceInfo.SELECT_ACTIVE_SG); }

    protected List<Param> setParams() {
        return List.of(
            new Param("sgRef", "定值组控制块引用", "C1/LLN0.SGCB").type(Param.Type.REFERENCE),
            new Param("sgNum", "定值组号", "1")
        );
    }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        String sgRef = stringVal("sgRef");
        int sgNum = Integer.parseInt(stringVal("sgNum"));
        CmsSelectActiveSG asdu = new CmsSelectActiveSG(MessageType.REQUEST)
                .sgcbReference(sgRef)
                .settingGroupNumber(sgNum);
        response = sendAndVerify(client, asdu);
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
        int sgNum = Integer.parseInt(stringVal("sgNum"));
        CliPrinter.success("Active setting group selected: SG" + sgNum);
    }
}
