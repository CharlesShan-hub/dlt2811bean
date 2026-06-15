package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsSgcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.enumerated.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * SGCBValue ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT SGCB
 * }  —  8.6.6
 *
 * Used by GetSGCBValues response (SEQUENCE OF SGCBValueChoice).
 */
public class CmsSgcbValueChoice extends CmsType {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    public CmsEnumerated  choice;       /* 0=error, 1=value */
    public CmsServiceError altError;
    public CmsSgcb         altValue;

    public CmsSgcbValueChoice() {
        this.choice   = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altValue = new CmsSgcb();
    }
    
    // -- chain setters --
    public CmsSgcbValueChoice choice(int v) { this.choice.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altError, altValue);
    }
}