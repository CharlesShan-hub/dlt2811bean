package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetFileConsole extends CommandHandler {

    public GetFileConsole() {
        super(CommandInfo.GET_FILE);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("file", "远程文件路径，如 \"/config/myfile.txt\"", null), new Param("output", "本地保存路径（可选），不指定则只打印信息", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "file", "Usage: get-file --file <remotePath> [--output <localPath>]"))
            return;

        String file = args.get("file");

        GetFileDao dao = new GetFileDao().fileName(file.trim());
        String output = args.get("output");
        if (output != null && !output.trim().isEmpty()) {
            dao.outputFile(output.trim());
        }

        ConsolePrinter.info("Downloading " + file + " ...");
        console.getClient(GetFileClient.class).execute(dao);

        StringBuilder sb = new StringBuilder("{\"success\":true,\"data\":{");
        sb.append("\"file\":\"").append(CmsFormatUtil.escapeJson(file.trim())).append('"');
        if (output != null && !output.trim().isEmpty()) {
            sb.append(",\"output\":\"").append(CmsFormatUtil.escapeJson(output.trim())).append('"');
        }
        sb.append("}}");
        ConsolePrinter.raw(sb.toString());
    }
}
