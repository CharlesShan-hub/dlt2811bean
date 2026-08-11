package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class GetGoReferenceConsole extends CommandHandler<GetGoReferenceDao, GetGoReferenceClient> {

    public GetGoReferenceConsole() {
        super(CommandInfo.GET_GO_REF, true);
        Param p = Param.of("ref", null, "gocbReference", String.class, true);
        param(p, "GoCB 引用，如 LD0/LLN0.gocb1");
        Param p2 = Param.of("offsets", null, "memberOffsets", List.class, true);
        param(p2, "成员偏移列表（空格分隔），如 \"0 1 2\"");
    }
}
