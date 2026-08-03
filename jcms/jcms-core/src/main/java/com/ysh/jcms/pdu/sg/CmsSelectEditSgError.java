package com.ysh.jcms.pdu.sg;

import com.ysh.jcms.data.InnerSelectEditSGErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * SelectEditSG-ErrorPDU ::= ServiceError — 8.6.2
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsSelectEditSgError extends CmsServiceError {

    public CmsSelectEditSgError() {
        super(new InnerSelectEditSGErrorPDU());
    }

    public CmsSelectEditSgError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsSelectEditSgError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
