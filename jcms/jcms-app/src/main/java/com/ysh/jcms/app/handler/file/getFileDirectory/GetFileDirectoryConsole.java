package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetFileDirectoryConsole extends CommandHandler<GetFileDirectoryDao, GetFileDirectoryClient> {

    public GetFileDirectoryConsole() {
        super(CommandInfo.GET_FILE_DIR, true);
        Param p = Param.of("path", "", "pathName", String.class, false);
        param(p, "路径筛选（可选），如 /config");
        Param p2 = Param.of("after", "", "fileAfter", String.class, false);
        param(p2, "从指定文件名之后的条目开始（可选）");
    }
}