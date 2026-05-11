package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFile;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class FileGetHandler extends AbstractServiceHandler {

    public FileGetHandler(CliContext ctx) { super(ctx); }

    public String getName() { return "file-get"; }
    public String getDescription() { return "读文件 (startPosition=0 取消)"; }
    public List<Param> getParams() {
        return List.of(
            new Param("fileName", "文件路径", "/README.txt"),
            new Param("start", "起始位置 (1开始, 0=取消)", "1")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String fileName = values.get("fileName");
        long start = Long.parseLong(values.get("start"));
        CmsGetFile asdu = new CmsGetFile(MessageType.REQUEST).fileName(fileName).startPosition(start);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed");
            return;
        }

        CmsGetFile file = (CmsGetFile) response.getAsdu();
        byte[] data = file.fileData.get();
        System.out.println("  File: " + fileName + " (pos=" + start + ", size=" + data.length + " bytes" + (file.endOfFile.get() ? ", EOF" : "") + ")");
        System.out.println("  Data: " + new String(data, java.nio.charset.StandardCharsets.UTF_8));
    }
}
