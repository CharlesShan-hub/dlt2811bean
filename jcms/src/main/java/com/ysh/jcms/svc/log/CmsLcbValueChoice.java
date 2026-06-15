package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.enumerated.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * LCBValue ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT LCB
 * }  —  8.8.2
 *
 * Used by GetLCBValues response.
 */
public class CmsLcbValueChoice extends CmsType {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    public CmsEnumerated  choice;       /* 0=error, 1=value */
    public CmsServiceError altError;
    public CmsLcb          altValue;

    public CmsLcbValueChoice() {
        this.choice   = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altValue = new CmsLcb();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altError, altValue);
    }
}
