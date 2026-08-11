package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class SetDataValuesConsole extends CommandHandler<SetDataValuesDao, SetDataValuesClient> {

    public SetDataValuesConsole() {
        super(CommandInfo.SET_DATA_VALUES, false);
        Param p = Param.of("refs", null, "references", List.class, true);
        param(p, "数据引用列表（空格分隔），如 LD0/LLN0.Mod.stVal LD0/LLN0.Beh.stVal");
        Param p2 = Param.of("values", null, "values", List.class, true);
        param(p2, "值列表（空格分隔），与引用列表一一对应");
        Param p3 = Param.of("fcs", null, "fcs", List.class, false);
        param(p3, "功能约束列表（空格分隔），与引用列表一一对应，如 1 0");
        Param p4 = Param.of("delimiter", null, null, String.class, false);
        param(p4, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}
