package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetGoCbValuesResponsePDUGocb;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsGoCb;

/**
 * <pre>
 * {@code
 * GoCBValue ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT GoCB
 * } — 8.9.4
 * }
 * </pre>
 *
 * <p>
 * Used by GetGoCBValues-ResponsePDU (SEQUENCE OF).
 */
public class CmsGocbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsGoCb altValue;

    public CmsGocbValueChoice() {
        super(new InnerAnonymousGetGoCbValuesResponsePDUGocb());
    }

    public CmsGocbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsGocbValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsGocbValueChoice altValue(CmsGoCb v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /** Copy choice selection and value from another CmsGocbValueChoice (fluent). */
    public CmsGocbValueChoice value(CmsGocbValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown GocbValueChoice choice: " + v.choice());
        }
    }
}
