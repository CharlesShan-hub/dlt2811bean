package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param.ParamType;

public class AssociateConsole extends CommandHandler<AssociateDao, AssociateClient> {

    public AssociateConsole() {
        super(CommandInfo.ASSOCIATE);
        param("ap", "ServerAccessPoint 引用（如 C_B5041X/S1）", "", "sapRef");
        param("secure", "加密关联（不传值，出现即启用）", "false", "secure", ParamType.BOOLEAN);
    }
}
