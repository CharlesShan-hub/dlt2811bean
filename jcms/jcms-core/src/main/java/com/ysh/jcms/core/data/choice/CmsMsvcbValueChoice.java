package com.ysh.jcms.core.data.choice;

import com.ysh.jcms.data.InnerAnonymousGetMSVCBValuesResponsePDUMsvcb;
import com.ysh.jcms.data.core.CmsChoice;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.block.CmsMsvcb;

/**
 * <pre>
 * {@code
 * MSVCBValue ::= CHOICE {
 *     error [0] IMPLICIT ServiceError,
 *     value [1] IMPLICIT MSVCB
 * } — 8.10.2
 * }
 * </pre>
 *
 * <p>
 * Used by GetMSVCBValues-ResponsePDU (SEQUENCE OF).
 */
public class CmsMsvcbValueChoice extends CmsChoice {

    public static final int ERROR = 0;
    public static final int VALUE = 1;

    @Choice(index = 0, name = "error", sync = Sync.WRAPPER)
    public CmsServiceError altError;
    @Choice(index = 1, name = "value", sync = Sync.WRAPPER)
    public CmsMsvcb altValue;

    public CmsMsvcbValueChoice() {
        super(new InnerAnonymousGetMSVCBValuesResponsePDUMsvcb());
    }

    public CmsMsvcbValueChoice choice(int v) {
        super.choice(v);
        return this;
    }

    /* ─── Fluent setters (set choice + value in one call) ─── */
    public CmsMsvcbValueChoice altError(int v) {
        choice(ERROR);
        this.altError.value(v);
        return this;
    }
    public CmsMsvcbValueChoice altValue(CmsMsvcb v) {
        choice(VALUE);
        this.altValue.value(v);
        return this;
    }

    /**
     * Copy choice selection and value from another CmsMsvcbValueChoice (fluent).
     */
    public CmsMsvcbValueChoice value(CmsMsvcbValueChoice v) {
        switch (v.choice()) {
            case ERROR :
                return altError(v.altError.value());
            case VALUE :
                return altValue(v.altValue);
            default :
                throw new IllegalArgumentException("Unknown MsvcbValueChoice choice: " + v.choice());
        }
    }
}
