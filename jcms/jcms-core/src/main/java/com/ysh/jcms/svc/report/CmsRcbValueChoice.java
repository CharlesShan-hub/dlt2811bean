package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.core.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * RCBValueChoice ::= CHOICE { error [0] IMPLICIT ServiceError, value [1]
 * IMPLICIT BRCB } — 8.7.2/8.7.4
 *
 * Used by GetBRCBValues/GetURCBValues response (BRCB or URCB in same slot).
 */
public class CmsRcbValueChoice extends CmsTypeOld {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    public CmsEnumerated choice; /* 0=error, 1=value */
    public CmsServiceError altError;
    public CmsBrcb altValue; /* BRCB or URCB in same slot */

    public CmsRcbValueChoice() {
        this.choice = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altValue = new CmsBrcb();
    }

    public CmsRcbValueChoice choice(int v) {
        this.choice.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(choice, altError, altValue);
    }
}
