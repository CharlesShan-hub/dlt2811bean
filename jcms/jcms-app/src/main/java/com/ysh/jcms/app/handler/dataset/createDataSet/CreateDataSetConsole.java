package com.ysh.jcms.app.handler.dataset.createDataSet;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

import java.util.List;

public class CreateDataSetConsole extends CommandHandler<CreateDataSetDao, CreateDataSetClient> {

    public CreateDataSetConsole() {
        super(CommandInfo.CREATE_DATASET, false);
        Param p = Param.of("ds", null, "datasetReference", String.class, true);
        param(p, "数据集引用，如 LD0/LLN0.myDs");
        Param p2 = Param.of("refs", null, "memberRefs", List.class, true);
        param(p2, "成员引用列表（空格分隔），如 LD0/GGIO1.Alm1 LD0/GGIO1.Alm2");
        Param p3 = Param.of("fcs", null, "memberFcs", List.class, true);
        param(p3, "成员功能约束列表（空格分隔），与引用列表一一对应，如 ST ST");
        Param p4 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p4, "追加到现有数据集后的最后一个成员引用");
        Param p5 = Param.of("delimiter", null, null, String.class, false);
        param(p5, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}
