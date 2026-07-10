package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeleteFileConsole implements CommandHandler {

    @Override
    public String name() {
        return "delete-file";
    }

    @Override
    public String description() {
        return "删除文件 (DeleteFile, 8.12.3)。\n" + "  用法: delete-file --file /path/to/file [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("file", "文件路径，如 \"/config/myfile.txt\"", null), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "file", "Usage: delete-file --file <path>"))
            return;

        String file = args.get("file");
        DeleteFileDao dao = new DeleteFileDao().fileName(file.trim());

        ConsolePrinter.info("Deleting file " + file);

        console.getClient(DeleteFileClient.class).execute(dao);

        CmsConsole.outputMessage("Deleted file " + file + " successfully", args);
    }
}
