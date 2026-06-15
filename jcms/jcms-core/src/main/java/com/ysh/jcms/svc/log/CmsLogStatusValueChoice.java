package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.core.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * LogStatusValueChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     value       [1] IMPLICIT LogStatusValue
 * }  —  8.8.6
 *
 * Used by GetLogStatusValues response.
 */
public class CmsLogStatusValueChoice extends CmsType {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    public CmsEnumerated       choice;       /* 0=error, 1=value */
    public CmsServiceError     altError;
    public CmsLogStatusValue   altValue;

    public CmsLogStatusValueChoice() {
        this.choice   = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altValue = new CmsLogStatusValue();
    }
    
    public CmsLogStatusValueChoice choice(int v) { this.choice.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altError, altValue);
    }
}