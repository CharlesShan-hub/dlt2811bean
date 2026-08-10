package com.ysh.jcms.app.handler.connection.release;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;

public class ReleaseConsole extends CommandHandler<ReleaseDao, ReleaseClient> {

    public ReleaseConsole() {
        super(CommandInfo.RELEASE, false);
    }
}