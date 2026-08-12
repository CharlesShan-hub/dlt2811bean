package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetLCBValuesResponsePDULcb;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsLcb;

/**
 * <pre>
 * {@code
 * LCBValue ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT LCB
 * } — 8.8.2
 * }
 * </pre>
 *
 * <p>
 * Element of GetLCBValues-ResponsePDU lcb.
 */
public class CmsLcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsLcb altValue;

    public CmsLcbValueChoice() {
        super(new InnerAnonymousGetLCBValuesResponsePDULcb());
    }

    public CmsLcbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsLcbValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsLcbValueChoice altValue(CmsLcb v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsLcbValueChoice (fluent). */
    public CmsLcbValueChoice value(CmsLcbValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown LcbValueChoice choice: " + v.choice());
        }
    }
}
