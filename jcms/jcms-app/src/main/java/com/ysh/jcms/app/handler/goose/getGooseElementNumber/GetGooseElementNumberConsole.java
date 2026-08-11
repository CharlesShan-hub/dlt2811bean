package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetGooseElementNumberConsole extends CommandHandler<GetGooseElementNumberDao, GetGooseElementNumberClient> {

    public GetGooseElementNumberConsole() {
        super(CommandInfo.GET_GOOSE_ELEM_NUM, true);
        Param p = Param.of("ref", null, "gocbReference", String.class, true);
        param(p, "GoCB 引用，如 LD0/LLN0.gocb1");
        Param p2 = Param.of("refs", null, "memberRefs", List.class, true);
        param(p2, "成员引用列表（空格分隔），如 \"LD0/LLN0.DO1\"");
        Param p3 = Param.of("fcs", null, "memberFcs", List.class, true);
        param(p3, "成员 FC 列表（空格分隔，与 refs 对应），如 \"1 6\"");
    }
}
