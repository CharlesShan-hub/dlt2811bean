package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;

/**
 * LCB 构建器
 */
public class LcbBuilder {

    private final CmsLcb lcb = new CmsLcb();

    public LcbBuilder datSet(String datSet) {
        if (datSet != null) lcb.datSet(datSet);
        return this;
    }

    public LcbBuilder logRef(String logRef) {
        if (logRef != null) lcb.logRef(logRef);
        return this;
    }

    public CmsCbValueChoice build() {
        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.LCB);
        result.altLcb = lcb;
        return result;
    }

    public static CmsCbValueChoice from(SclLogControl lc) {
        return new LcbBuilder()
                .datSet(lc.getDatSet())
                .logRef(lc.getLogName())
                .build();
    }
}
