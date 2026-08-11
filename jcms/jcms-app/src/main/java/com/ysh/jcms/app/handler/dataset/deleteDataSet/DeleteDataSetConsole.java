package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class DeleteDataSetConsole extends CommandHandler<DeleteDataSetDao, DeleteDataSetClient> {

    public DeleteDataSetConsole() {
        super(CommandInfo.DELETE_DATASET, false);
        Param p = Param.of("ds", null, "datasetReference", String.class, true);
        param(p, "数据集引用，如 LD0/LLN0.myDs");
    }
}
