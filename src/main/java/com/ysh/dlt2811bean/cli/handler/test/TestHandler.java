package com.ysh.dlt2811bean.cli.handler.test;

import com.ysh.dlt2811bean.cli.handler.common.AbstractServiceHandler;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import java.util.Map;

public class TestHandler extends AbstractServiceHandler {

    public TestHandler(CliContext ctx) { super(ctx, ServiceInfo.TEST); }

    public void doExecute(CmsClient client, Map<String, String> values) throws Exception {
        if(client.test() != null)
            CliPrinter.success("Test OK!");
        else
            CliPrinter.error("Test Failed.");
    }
}
