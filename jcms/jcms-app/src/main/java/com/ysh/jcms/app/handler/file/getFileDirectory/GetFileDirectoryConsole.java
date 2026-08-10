package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.util.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetFileDirectoryConsole extends CommandHandler {

    public GetFileDirectoryConsole() {
        super(CommandInfo.GET_FILE_DIR);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("path", "路径筛选（可选），如 /config", ""), new Param("after", "从指定文件名之后的条目开始（可选）", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;

        GetFileDirectoryDao dao = new GetFileDirectoryDao();
        String path = args.get("path");
        if (path != null && !path.trim().isEmpty())
            dao.pathName(path.trim());
        String after = args.get("after");
        if (after != null && !after.trim().isEmpty())
            dao.fileAfter(after.trim());

        console.getClient(GetFileDirectoryClient.class).execute(dao);

        GetFileDirectoryClient.FileDirectoryResult result = (GetFileDirectoryClient.FileDirectoryResult) dao.result();

        if (result == null || result.entries.isEmpty()) {
            ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            return;
        }

        StringBuilder sb = new StringBuilder("{\"success\":true,\"moreFollows\":");
        sb.append(result.moreFollows).append(",\"data\":[");
        for (int i = 0; i < result.entries.size(); i++) {
            if (i > 0)
                sb.append(',');
            GetFileDirectoryClient.FileEntryResult e = result.entries.get(i);
            sb.append("{\"fileName\":\"").append(CmsFormatUtil.escapeJson(e.fileName)).append("\",\"fileSize\":").append(e.fileSize)
                    .append(",\"lastModified\":").append(e.lastModified).append(",\"checkSum\":").append(e.checkSum).append(",\"crc32\":")
                    .append(e.checkSum).append('}');
        }
        sb.append("]}");
        ConsolePrinter.raw(sb.toString());
    }
}
