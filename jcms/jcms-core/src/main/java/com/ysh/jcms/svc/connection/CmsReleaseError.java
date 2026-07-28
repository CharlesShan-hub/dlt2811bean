package com.ysh.jcms.svc.connection;

import com.ysh.jcms.data.InnerReleaseErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * Release-ErrorPDU ::= ServiceError — 8.2.2
 *
 * <p>Type alias, not a SEQUENCE. Encoding as a plain ServiceError (INTEGER)
 * is required for inter-vendor interoperability.
 *
 * <p>NOTE: reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsReleaseError extends CmsServiceError {

    public CmsReleaseError() {
        super(new InnerReleaseErrorPDU());
    }

    public CmsReleaseError(int v) {
        this();
        value(v);
    }

    @Override
    public CmsReleaseError value(int v) {
        super.value(v);
        return this;
    }

    @Override
    public int value() {
        return super.value();
    }
}
