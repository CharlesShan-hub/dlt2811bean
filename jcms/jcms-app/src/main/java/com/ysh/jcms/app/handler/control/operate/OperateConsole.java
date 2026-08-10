package com.ysh.jcms.app.handler.control.operate;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param.ParamType;

public class OperateConsole extends CommandHandler<OperateDao, OperateClient> {

    public OperateConsole() {
        super(CommandInfo.OPERATE, false);
        param("ref", "控制对象引用，如 LD0/CTRL1.SPC1", null, "ref", ParamType.STRING, true);
        param("value", "控制值 (SPC: true/false)", "", "value");
        param("origin", "操作源 (0=本地, 1=远程)", "", "origin");
        param("ctlNum", "控制序号", "", "ctlNum");
        param("test", "测试标志", "", "test");
        param("check", "校验码", "", "check");
    }
}
