package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerConfirmEditSGValuesErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * ConfirmEditSGValues-ErrorPDU ::= ServiceError — 8.6.4
 *
 * <p>Type alias, not a SEQUENCE.
 */
public class CmsConfirmEditSgValuesError extends CmsServiceError {

    public CmsConfirmEditSgValuesError() {
        super(new InnerConfirmEditSGValuesErrorPDU());
    }

    public CmsConfirmEditSgValuesError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsConfirmEditSgValuesError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
