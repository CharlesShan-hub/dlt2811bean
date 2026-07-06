package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetFileConsole implements CommandHandler {

    @Override
    public String name() { return "set-file"; }

    @Override
    public String description() { return "写文件 (SetFile, 8.12.2)。\n" +
        "  用法: set-file --local ./localfile.txt --remote /config/remotefile.txt"; }

    @Override
    public List<Param> params() {
        return Arrays.asList(
            new Param("local", "本地文件路径", null),
            new Param("remote", "远程目标路径，如 \"/config/myfile.txt\"", null)
        );
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.isConnected()) {
            ConsolePrinter.error("Not connected. Type 'connect' first.");
            return;
        }

        String local = args.get("local");
        String remote = args.get("remote");
        if (local == null || local.trim().isEmpty() || remote == null || remote.trim().isEmpty()) {
            ConsolePrinter.error("Usage: set-file --local <localPath> --remote <remotePath>");
            return;
        }

        SetFileDao dao = new SetFileDao()
            .localFile(local.trim())
            .remoteFile(remote.trim());

        ConsolePrinter.info("Uploading " + local + " -> " + remote + " ...");
        console.getClient(SetFileClient.class).execute(dao);
        ConsolePrinter.success("Uploaded " + local + " to " + remote + " successfully");
    }
}
