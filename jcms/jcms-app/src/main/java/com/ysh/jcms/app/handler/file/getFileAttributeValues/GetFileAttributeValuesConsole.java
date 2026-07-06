package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetFileAttributeValuesConsole implements CommandHandler {

    @Override
    public String name() { return "get-file-attrs"; }

    @Override
    public String description() { return "读文件属性值 (GetFileAttributeValues, 8.12.4)。\n" +
        "  用法: get-file-attrs --file /path/to/file"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("file", "文件路径，如 \"/config/myfile.txt\"", null)
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
            ConsolePrinter.error("Missing --file. Usage: get-file-attrs --file <path>");
            return;
        }

        GetFileAttributeValuesDao dao = new GetFileAttributeValuesDao().fileName(file.trim());

        ConsolePrinter.info("Fetching file attributes for " + file);

        console.getClient(GetFileAttributeValuesClient.class).execute(dao);

        GetFileAttributeValuesClient.FileAttrResult result =
            console.getClient(GetFileAttributeValuesClient.class).getLastResult();

        if (result == null) {
            ConsolePrinter.info("No file attributes returned");
            return;
        }

        ConsolePrinter.info("  fileName=" + result.fileName);
        ConsolePrinter.info("  fileSize=" + result.fileSize);
        ConsolePrinter.info("  lastModified=" + result.lastModified);
        ConsolePrinter.info("  checkSum=" + result.checkSum);
    }
}
