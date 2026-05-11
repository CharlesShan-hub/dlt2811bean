package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class FileAttrHandler implements CommandHandler {

    private final CliContext ctx;

    public FileAttrHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "file-attr"; }
    public String getDescription() { return "读文件属性 (大小、时间、校验和)"; }
    public List<Param> getParams() {
        return List.of(
            new Param("fileName", "文件路径", "/README.txt")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String fileName = values.get("fileName");
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.REQUEST).fileName(fileName);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed");
            return;
        }

        CmsGetFileAttributeValues attr = (CmsGetFileAttributeValues) response.getAsdu();
        System.out.println("  File: " + attr.fileEntry.fileName.get());
        System.out.println("  Size: " + attr.fileEntry.fileSize.get() + " bytes");
        System.out.println("  CRC32: " + Long.toHexString(attr.fileEntry.checkSum.get()));
    }
}
