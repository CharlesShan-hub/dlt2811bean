package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetLogStatusValuesResponsePDULog;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.log.CmsLogStatusValue;

/**
 * LogStatusValueChoice ::= CHOICE { error [0] IMPLICIT ServiceError, value [1]
 * IMPLICIT LogStatusValue } — 8.8.6
 *
 * <p>Element of GetLogStatusValues-ResponsePDU log.
 */
public class CmsLogStatusValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER) public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER) public CmsLogStatusValue altValue;

    public CmsLogStatusValueChoice() {
        super(new InnerAnonymousGetLogStatusValuesResponsePDULog());
    }

    public CmsLogStatusValueChoice choice(int v) { super.choice(v); return this; }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsLogStatusValueChoice altError(int v) { choice(ERROR); this.altError.value(v); return this; }
    public CmsLogStatusValueChoice altValue(CmsLogStatusValue v) { choice(VALUE); this.altValue.value(v); return this; }

    /** Copy choice selection and value from another CmsLogStatusValueChoice (fluent). */
    public CmsLogStatusValueChoice value(CmsLogStatusValueChoice v) {
        int ch = v.choice();
        super.choice(ch);
        switch (ch) {
            case ERROR: this.altError.value(v.altError.value()); break;
            case VALUE: this.altValue.value(v.altValue); break;
        }
        return this;
    }
}
