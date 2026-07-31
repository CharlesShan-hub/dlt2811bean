package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetBRCBValuesResponsePDUBrcb;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsBrcb;

/**
 * RCBValueChoice ::= CHOICE { error [0] IMPLICIT ServiceError, value [1]
 * IMPLICIT BRCB } — 8.7.2 — used by GetBRCBValues-ResponsePDU (SEQUENCE OF).
 *
 * <p>GetURCBValues-ResponsePDU has its own inline CHOICE (value: URCB);
 * the BRCB variant is covered here.
 */
public class CmsRcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER) public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER) public CmsBrcb altValue;

    public CmsRcbValueChoice() {
        super(new InnerAnonymousGetBRCBValuesResponsePDUBrcb());
    }

    public CmsRcbValueChoice choice(int v) { super.choice(v); return this; }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsRcbValueChoice altError(int v) { choice(ERROR); this.altError.value(v); return this; }
    public CmsRcbValueChoice altValue(CmsBrcb v) { choice(VALUE); this.altValue.value(v); return this; }

    /** Copy choice selection and value from another CmsRcbValueChoice (fluent). */
    public CmsRcbValueChoice value(CmsRcbValueChoice v) {
        int ch = v.choice();
        super.choice(ch);
        switch (ch) {
            case ERROR: this.altError.value(v.altError.value()); break;
            case VALUE: this.altValue.value(v.altValue); break;
        }
        return this;
    }
}
