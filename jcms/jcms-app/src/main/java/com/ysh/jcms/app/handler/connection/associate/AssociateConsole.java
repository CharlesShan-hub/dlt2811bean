package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class AssociateConsole extends CommandHandler<AssociateDao, AssociateClient> {

    public AssociateConsole() {
        super(CommandInfo.ASSOCIATE);
        Param p = Param.of("ap", "", "sapRef", String.class, false);
        param(p, "关联的目标访问点，格式 IED/AP");
        Param p2 = Param.of("secure", "false", "secure", Boolean.class, false);
        param(p2, "启用加密关联");
    }
}
