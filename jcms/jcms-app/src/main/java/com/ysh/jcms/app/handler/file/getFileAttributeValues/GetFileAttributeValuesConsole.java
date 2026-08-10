package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetFileAttributeValuesConsole extends CommandHandler {

    public GetFileAttributeValuesConsole() {
        super(CommandInfo.GET_FILE_ATTRS);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("file", "文件路径，如 \"/config/myfile.txt\"", null));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        if (!CmsConsole.requireParam(args, "file", "Usage: get-file-attrs --file <path>"))
            return;

        String file = args.get("file");

        GetFileAttributeValuesDao dao = new GetFileAttributeValuesDao().fileName(file.trim());

        console.getClient(GetFileAttributeValuesClient.class).execute(dao);

        GetFileAttributeValuesClient.FileAttrResult result = (GetFileAttributeValuesClient.FileAttrResult) dao.result();

        if (result == null) {
            ConsolePrinter.raw("{\"success\":true,\"data\":null}");
            return;
        }

        ConsolePrinter.raw("{\"success\":true,\"data\":{" + "\"fileName\":\"" + CmsFormatUtil.escapeJson(result.fileName) + "\","
                + "\"fileSize\":" + result.fileSize + "," + "\"lastModified\":" + result.lastModified + "," + "\"checkSum\":"
                + result.checkSum + "}}");
    }
}
