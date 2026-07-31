package com.ysh.jcms.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetSGCBValuesResponsePDUSgscb;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsSgcb;

/**
 * SGCBValue ::= CHOICE { error [0] IMPLICIT ServiceError, value [1] IMPLICIT
 * SGCB } — 8.6.6 — used by GetSGCBValues-ResponsePDU (SEQUENCE OF).
 */
public class CmsSgcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER) public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER) public CmsSgcb altValue;

    public CmsSgcbValueChoice() {
        super(new InnerAnonymousGetSGCBValuesResponsePDUSgscb());
    }

    public CmsSgcbValueChoice choice(int v) { super.choice(v); return this; }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsSgcbValueChoice altError(int v) { choice(ERROR); this.altError.value(v); return this; }
    public CmsSgcbValueChoice altValue(CmsSgcb v) { choice(VALUE); this.altValue.value(v); return this; }

    /** Copy choice selection and value from another CmsSgcbValueChoice (fluent). */
    public CmsSgcbValueChoice value(CmsSgcbValueChoice v) {
        int ch = v.choice();
        super.choice(ch);
        switch (ch) {
            case ERROR: this.altError.value(v.altError.value()); break;
            case VALUE: this.altValue.value(v.altValue); break;
        }
        return this;
    }
}
