package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsSetFile;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class FileSetHandler implements CommandHandler {

    private final CliContext ctx;

    public FileSetHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "file-set"; }
    public String getDescription() { return "写文件 (endOfFile=true 完成)"; }
    public List<Param> getParams() {
        return List.of(
            new Param("fileName", "文件路径", "/upload.txt"),
            new Param("start", "起始位置 (1开始, 0=取消)", "1"),
            new Param("data", "文件内容", "Hello from CMS CLI"),
            new Param("eof", "是否最后一块 (true/false)", "true")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        if (!client.isConnected()) {
            System.out.println("  Not connected. Type 'connect' first.");
            return;
        }

        String fileName = values.get("fileName");
        long start = Long.parseLong(values.get("start"));
        String text = values.get("data");
        boolean eof = Boolean.parseBoolean(values.get("eof"));

        CmsSetFile asdu = new CmsSetFile(MessageType.REQUEST).fileName(fileName).startPosition(start)
                .fileData(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)).endOfFile(eof);
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            System.out.println("  Request failed");
            return;
        }

        System.out.println("  Written " + text.length() + " bytes to " + fileName + (eof ? " (complete)" : ""));
    }
}
