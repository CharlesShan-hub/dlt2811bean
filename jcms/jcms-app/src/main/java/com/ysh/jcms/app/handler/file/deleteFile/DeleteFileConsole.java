package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeleteFileConsole extends CommandHandler {

    public DeleteFileConsole() {
        super(CommandInfo.DELETE_FILE);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("file", "文件路径，如 \"/config/myfile.txt\"", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "file", "Usage: delete-file --file <path>"))
            return;

        String file = args.get("file");
        DeleteFileDao dao = new DeleteFileDao().fileName(file.trim());

        ConsolePrinter.info("Deleting file " + file);

        console.getClient(DeleteFileClient.class).execute(dao);

        ConsolePrinter.success("Deleted file " + file + " successfully");
    }
}
