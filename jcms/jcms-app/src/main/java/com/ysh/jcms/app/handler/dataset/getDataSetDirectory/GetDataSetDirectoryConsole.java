package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;

public class GetDataSetDirectoryConsole extends CommandHandler<GetDataSetDirectoryDao, GetDataSetDirectoryClient> {

    public GetDataSetDirectoryConsole() {
        super(CommandInfo.GET_DATASET_DIR);
        Param p = Param.of("ds", null, "datasetReference", String.class, true);
        param(p, "数据集引用，如 LD0/LLN0.dsAlarm");
        Param p2 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p2, "起始引用（分页截取，不传则从头开始）");
        Param p3 = Param.of("auto-pull", "false", null, String.class, false);
        param(p3, "自动续拉分页（true/false）");
    }
}
