package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class DeleteFileConsole extends CommandHandler<DeleteFileDao, DeleteFileClient> {

    public DeleteFileConsole() {
        super(CommandInfo.DELETE_FILE);
        Param p = Param.of("file", null, "fileName", String.class, true);
        param(p, "文件路径，如 \"/config/myfile.txt\"");
    }
}