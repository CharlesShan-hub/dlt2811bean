package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.data.choice.CmsData;

import java.util.List;

public class SetEditSgValueConsole extends CommandHandler<SetEditSgValueDao, SetEditSgValueClient> {

    public SetEditSgValueConsole() {
        super(CommandInfo.SET_EDIT_SG, false);
        Param p1 = Param.of("refs", null, "refs", List.class, true);
        param(p1, "数据引用列表（空格分隔），如 \"PROT/OCPTOC2.StrVal PROT/OCPTOC2.OpDlTmms\"");
        Param p2 = Param.of("values", null, "values", List.class, true);
        p2.itemType(CmsData.class);
        param(p2, "定值 JSON 列表（空格分隔），与 refs 一一对应，如 {\"int32\":1} {\"float32\":2.5}");
        Param p3 = Param.of("delimiter", null, null, String.class, false);
        param(p3, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}
