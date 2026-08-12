package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetSGCBValuesResponsePDUSgscb;
import com.ysh.jcms.core.data.core.CmsChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.block.CmsSgcb;

/**
 * <pre>
 * {@code
 * SGCBValue ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT SGCB
 * } — 8.6.6
 * }
 * </pre>
 *
 * <p>
 * Used by GetSGCBValues-ResponsePDU (SEQUENCE OF).
 */
public class CmsSgcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsSgcb altValue;

    public CmsSgcbValueChoice() {
        super(new InnerAnonymousGetSGCBValuesResponsePDUSgscb());
    }

    public CmsSgcbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsSgcbValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsSgcbValueChoice altValue(CmsSgcb v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsSgcbValueChoice (fluent). */
    public CmsSgcbValueChoice value(CmsSgcbValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown SgcbValueChoice choice: " + v.choice());
        }
    }
}
