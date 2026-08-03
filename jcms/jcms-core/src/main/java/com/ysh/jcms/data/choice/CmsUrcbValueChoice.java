package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetURCBValuesResponsePDUUrcb;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsUrcb;

/**
 * <pre>
 * {@code
 * URCBValueChoice ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT URCB
 * } — 8.7.4
 * }
 * </pre>
 *
 * <p>
 * Used by GetURCBValues-ResponsePDU (SEQUENCE OF).
 */
public class CmsUrcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsUrcb altValue;

    public CmsUrcbValueChoice() {
        super(new InnerAnonymousGetURCBValuesResponsePDUUrcb());
    }

    public CmsUrcbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsUrcbValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsUrcbValueChoice altValue(CmsUrcb v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsUrcbValueChoice (fluent). */
    public CmsUrcbValueChoice value(CmsUrcbValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown UrcbValueChoice choice: " + v.choice());
        }
    }
}
