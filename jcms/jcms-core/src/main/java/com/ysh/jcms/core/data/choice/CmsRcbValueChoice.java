package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetBRCBValuesResponsePDUBrcb;
import com.ysh.jcms.core.data.core.CmsChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.block.CmsBrcb;

/**
 * <pre>
 * {@code
 * RCBValueChoice ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT BRCB
 * } — 8.7.2
 * }
 * </pre>
 *
 * <p>
 * Used by GetBRCBValues-ResponsePDU (SEQUENCE OF).
 *
 * <p>
 * GetURCBValues-ResponsePDU has its own inline CHOICE (value: URCB); the BRCB
 * variant is covered here.
 */
public class CmsRcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsBrcb altValue;

    public CmsRcbValueChoice() {
        super(new InnerAnonymousGetBRCBValuesResponsePDUBrcb());
    }

    public CmsRcbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsRcbValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsRcbValueChoice altValue(CmsBrcb v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsRcbValueChoice (fluent). */
    public CmsRcbValueChoice value(CmsRcbValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown RcbValueChoice choice: " + v.choice());
        }
    }
}
