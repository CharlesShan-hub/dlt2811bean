package com.ysh.jcms.pdu.negotiate;

import com.ysh.jcms.data.InnerAssociateNegotiateErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * <pre>
 * {@code
 * AssociateNegotiate-ErrorPDU ::= ServiceError — 8.15
 * }
 * </pre>
 *
 * <p>
 * Type alias, not a SEQUENCE.
 */
public class CmsNegotiateError extends CmsServiceError {

    public CmsNegotiateError() {
        super(new InnerAssociateNegotiateErrorPDU());
    }

    public CmsNegotiateError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsNegotiateError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
