package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetFileConsole extends CommandHandler<GetFileDao, GetFileClient> {

    public GetFileConsole() {
        super(CommandInfo.GET_FILE);
        Param p = Param.of("file", null, "fileName", String.class, true);
        param(p, "远程文件路径，如 \"/config/myfile.txt\"");
        Param p2 = Param.of("output", "", "outputFile", String.class, false);
        param(p2, "本地保存路径（可选），不指定则只打印信息");
    }
}