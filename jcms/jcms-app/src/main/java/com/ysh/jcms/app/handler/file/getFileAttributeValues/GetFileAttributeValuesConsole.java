package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetFileAttributeValuesConsole extends CommandHandler<GetFileAttributeValuesDao, GetFileAttributeValuesClient> {

    public GetFileAttributeValuesConsole() {
        super(CommandInfo.GET_FILE_ATTRS, true);
        Param p = Param.of("file", null, "fileName", String.class, true);
        param(p, "文件路径，如 \"/config/myfile.txt\"");
    }
}