package com.ysh.jcms.pdu.connection;

import com.ysh.jcms.data.InnerAssociateErrorPDU;
import com.ysh.jcms.data.enumerate.CmsServiceError;

/**
 * Associate-ErrorPDU ::= ServiceError — 8.2.1
 *
 * <p>
 * Type alias, not a SEQUENCE. Encoding as a plain ServiceError (INTEGER) is
 * required for inter-vendor interoperability.
 *
 * <p>
 * NOTE: reqId is handled at the protocol level, not part of the ASN.1
 * definition.
 */
public class CmsAssociateError extends CmsServiceError {

    public CmsAssociateError() {
        super(new InnerAssociateErrorPDU());
    }

    public CmsAssociateError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsAssociateError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
