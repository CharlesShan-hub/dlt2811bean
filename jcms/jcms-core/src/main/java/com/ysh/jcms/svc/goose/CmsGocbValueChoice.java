package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsGoCb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.core.CmsEnumerated;
import java.util.Arrays;
import java.util.List;

/**
 * GoCBValue ::= CHOICE { error [0] IMPLICIT ServiceError, value [1] IMPLICIT
 * GoCB } — 8.9.4
 *
 * Used by GetGoCBValues response.
 */
public class CmsGocbValueChoice extends CmsType {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    public CmsEnumerated choice; /* 0=error, 1=value */
    public CmsServiceError altError;
    public CmsGoCb altValue;

    public CmsGocbValueChoice() {
        this.choice = new CmsEnumerated();
        this.altError = new CmsServiceError();
        this.altValue = new CmsGoCb();
    }

    public CmsGocbValueChoice choice(int v) {
        this.choice.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(choice, altError, altValue);
    }
}
