package com.ysh.jcms.app.handler.control.cancel;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;

public class CancelConsole extends CommandHandler<CancelDao, CancelClient> {

    public CancelConsole() {
        super(CommandInfo.CANCEL, false);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(
                new Param("ref", "控制对象引用", (String) null, true),
                new Param("value", "控制值 (SPC: true/false)", ""),
                new Param("origin", "操作源 (0=本地, 1=远程)", ""),
                new Param("ctlNum", "控制序号", ""),
                new Param("test", "测试标志", ""));
    }
}