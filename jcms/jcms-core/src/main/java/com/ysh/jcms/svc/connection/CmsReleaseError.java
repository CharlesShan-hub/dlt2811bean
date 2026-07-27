package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerReleaseErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * Release-ErrorPDU ::= ServiceError — 8.2.2
 *
 * NOTE: reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsReleaseError extends CmsType {

    public CmsServiceError serviceError;

    public CmsReleaseError() {
        super(new InnerReleaseErrorPDU());
        this.serviceError = new CmsServiceError();
    }

    public CmsReleaseError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerReleaseErrorPDU) inner).value = serviceError.value();
    }

    @Override
    public void syncFromInner() {
        this.serviceError.value(((InnerReleaseErrorPDU) inner).value);
    }
}
