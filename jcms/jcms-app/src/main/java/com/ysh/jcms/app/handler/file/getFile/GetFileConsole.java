package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetFileConsole implements CommandHandler {

    @Override
    public String name() { return "get-file"; }

    @Override
    public String description() { return "读文件 (GetFile, 8.12.1)。\n" +
        "  用法: get-file --file /remote/path [--output ./local.txt]"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("file", "远程文件路径，如 \"/config/myfile.txt\"", null),
            new Param("output", "本地保存路径（可选），不指定则只打印信息", "")
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String file = args.get("file");
        if (file == null || file.trim().isEmpty()) {
            ConsolePrinter.error("Missing --file. Usage: get-file --file <remotePath> [--output <localPath>]");
            return;
        }

        GetFileDao dao = new GetFileDao().fileName(file.trim());
        String output = args.get("output");
        if (output != null && !output.trim().isEmpty()) {
            dao.outputFile(output.trim());
        }

        ConsolePrinter.info("Downloading " + file + " ...");
        console.getClient(GetFileClient.class).execute(dao);
    }
}
