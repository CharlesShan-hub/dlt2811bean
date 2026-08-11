package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class SetFileConsole extends CommandHandler<SetFileDao, SetFileClient> {

    public SetFileConsole() {
        super(CommandInfo.SET_FILE);
        Param p = Param.of("local", null, "localFile", String.class, true);
        param(p, "本地文件路径");
        Param p2 = Param.of("remote", null, "remoteFile", String.class, true);
        param(p2, "远程目标路径，如 \"/config/myfile.txt\"");
    }
}