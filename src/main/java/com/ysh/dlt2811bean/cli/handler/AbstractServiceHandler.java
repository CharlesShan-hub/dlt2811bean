package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;

import java.util.List;

public abstract class AbstractServiceHandler implements CommandHandler {

    protected final CliContext ctx;

    protected AbstractServiceHandler(CliContext ctx) {
        this.ctx = ctx;
    }

    protected void requireConnected(CmsClient client) {
        if (!client.isConnected()) {
            throw new IllegalStateException("Not connected. Type 'connect' first.");
        }
    }

    protected CmsApdu sendAndVerify(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            throw new IllegalStateException("Request failed");
        }
        return response;
    }

    public List<Param> getParams() { return List.of(); }

    protected void printGray(String text) {
        System.out.println(CmsColor.gray(text));
    }

    protected void printRed(String text) {
        System.out.println(CmsColor.red(text));
    }

    protected void printGreen(String text) {
        System.out.println(CmsColor.green(text));
    }

    protected void printMoreFollows(boolean moreFollows) {
        if (moreFollows) {
            printGray("  (more data available)");
        }
    }

    protected boolean printIfEmpty(boolean isEmpty) {
        if (isEmpty) {
            printGray("  无数据");
            return true;
        }
        return false;
    }
}
