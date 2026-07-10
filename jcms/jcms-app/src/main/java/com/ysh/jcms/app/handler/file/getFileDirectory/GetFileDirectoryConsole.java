package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.CmsFormatUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GetFileDirectoryConsole implements CommandHandler {

    @Override
    public String name() {
        return "get-file-dir";
    }

    @Override
    public String description() {
        return "列文件目录 (GetFileDirectory, 8.12.5)。\n" + "  用法: get-file-dir --path /config --after file.txt [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("path", "路径筛选（可选），如 /config", ""), new Param("after", "从指定文件名之后的条目开始（可选）", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        boolean jsonMode = CmsConsole.isJsonMode(args);
        if (!console.requireConnected(args))
            return;

        GetFileDirectoryDao dao = new GetFileDirectoryDao();
        String path = args.get("path");
        if (path != null && !path.trim().isEmpty())
            dao.pathName(path.trim());
        String after = args.get("after");
        if (after != null && !after.trim().isEmpty())
            dao.fileAfter(after.trim());

        if (!jsonMode) {
            ConsolePrinter.info("Fetching file directory...");
        }

        console.getClient(GetFileDirectoryClient.class).execute(dao);

        GetFileDirectoryClient.FileDirectoryResult result = console.getClient(GetFileDirectoryClient.class).getLastResult();

        if (result == null || result.entries.isEmpty()) {
            if (jsonMode) {
                ConsolePrinter.raw("{\"success\":true,\"data\":[]}");
            } else {
                ConsolePrinter.info("No files found");
            }
            return;
        }

        if (jsonMode) {
            StringBuilder sb = new StringBuilder("{\"success\":true,\"moreFollows\":");
            sb.append(result.moreFollows).append(",\"data\":[");
            for (int i = 0; i < result.entries.size(); i++) {
                if (i > 0)
                    sb.append(',');
                GetFileDirectoryClient.FileEntryResult e = result.entries.get(i);
                sb.append("{\"fileName\":\"").append(CmsFormatUtil.escapeJson(e.fileName)).append("\",\"fileSize\":").append(e.fileSize)
                        .append(",\"lastModified\":").append(e.lastModified).append(",\"checkSum\":").append(e.checkSum)
                        .append(",\"crc32\":").append(e.checkSum).append('}');
            }
            sb.append("]}");
            ConsolePrinter.raw(sb.toString());
        } else {
            for (GetFileDirectoryClient.FileEntryResult e : result.entries) {
                ConsolePrinter.info("  " + e.fileName + "  size=" + e.fileSize + "  modified=" + e.lastModified + "  crc32=" + e.checkSum);
            }

            if (result.moreFollows) {
                ConsolePrinter.info("  ... (more entries available)");
            }
        }
    }
}
