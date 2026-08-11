package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class SetEditSgValueConsole extends CommandHandler<SetEditSgValueDao, SetEditSgValueClient> {

    public SetEditSgValueConsole() {
        super(CommandInfo.SET_EDIT_SG, false);
        Param p1 = Param.of("refs", null, "refs", List.class, true);
        param(p1, "数据引用列表（空格分隔），如 \"PROT/OCPTOC2.StrVal PROT/OCPTOC2.OpDlTmms\"");
        Param p2 = Param.of("values", null, "values", List.class, true);
        param(p2, "定值列表（空格分隔），与 refs 一一对应");
        Param p3 = Param.of("type", "visible-string", "type", String.class, false);
        param(p3,
                "数据类型，支持: visible-string(默认), int32, float32, boolean, int8, int16, int8u, int16u, int32u, int64, int64u, float64, octet-string");
        Param p4 = Param.of("delimiter", null, null, String.class, false);
        param(p4, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}
