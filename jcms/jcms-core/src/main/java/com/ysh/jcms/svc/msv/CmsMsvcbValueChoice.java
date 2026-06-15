package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsMsvcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.core.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * MSVCBValue ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT MSVCB
 * }  —  8.10.2
 *
 * Used by GetMSVCBValues response.
 */
public class CmsMsvcbValueChoice extends CmsType {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    public CmsEnumerated  choice;       /* 0=error, 1=value */
    public CmsServiceError altError;
    public CmsMsvcb        altValue;

    public CmsMsvcbValueChoice() {
        this.choice   = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altValue = new CmsMsvcb();
    }
    
    public CmsMsvcbValueChoice choice(int v) { this.choice.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altError, altValue);
    }
}