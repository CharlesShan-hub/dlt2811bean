package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsDeleteFile;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class FileDeleteHandler extends AbstractServiceHandler {

    public FileDeleteHandler(CliContext ctx) { super(ctx, ServiceInfo.DELETE_FILE); }
    public List<Param> getParams() {
        return List.of(
            new Param("fileName", "文件路径", "/upload.txt")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String fileName = values.get("fileName");
        CmsDeleteFile asdu = new CmsDeleteFile(MessageType.REQUEST).fileName(fileName);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Delete failed");
            return;
        }
        System.out.println("  Deleted " + fileName);
    }
}
