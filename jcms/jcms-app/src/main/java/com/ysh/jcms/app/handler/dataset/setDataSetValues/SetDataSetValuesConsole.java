package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import com.ysh.jcms.core.data.choice.CmsData;

import java.util.List;

public class SetDataSetValuesConsole extends CommandHandler<SetDataSetValuesDao, SetDataSetValuesClient> {

    public SetDataSetValuesConsole() {
        super(CommandInfo.SET_DATASET_VALUES, false);
        Param p = Param.of("ds", null, "datasetReference", String.class, true);
        param(p, "数据集引用，如 LD0/LLN0.dsAlarm");
        Param p2 = Param.of("values", null, "values", List.class, true);
        p2.itemType(CmsData.class);
        param(p2, "数据值列表（空格分隔），如 aa bb cc");
        Param p3 = Param.of("after", null, "referenceAfter", String.class, false);
        param(p3, "起始引用（分页截取，不传则从头开始）");
        Param p4 = Param.of("delimiter", null, null, String.class, false);
        param(p4, "列表分隔符（默认空格，可指定逗号、竖线等）");
    }
}
