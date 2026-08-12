package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetLogStatusValuesResponsePDULog;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.log.CmsLogStatusValue;

/**
 * <pre>
 * {@code
 * LogStatusValueChoice ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT LogStatusValue
 * } — 8.8.6
 * }
 * </pre>
 *
 * <p>
 * Element of GetLogStatusValues-ResponsePDU log.
 */
public class CmsLogStatusValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsLogStatusValue altValue;

    public CmsLogStatusValueChoice() {
        super(new InnerAnonymousGetLogStatusValuesResponsePDULog());
    }

    public CmsLogStatusValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsLogStatusValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsLogStatusValueChoice altValue(CmsLogStatusValue v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /**
     * Copy choice selection and value from another CmsLogStatusValueChoice
     * (fluent).
     */
    public CmsLogStatusValueChoice value(CmsLogStatusValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown LogStatusValueChoice choice: " + v.choice());
        }
    }
}
