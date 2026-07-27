package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAssociateErrorPDU;
import com.ysh.jcms.data.common.CmsServiceError;

/**
 * Associate-ErrorPDU ::= ServiceError — 8.2.1
 *
 * NOTE: reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsAssociateError extends CmsType {

    public CmsServiceError serviceError;

    public CmsAssociateError() {
        super(new InnerAssociateErrorPDU());
        this.serviceError = new CmsServiceError();
    }

    public CmsAssociateError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerAssociateErrorPDU) inner).value = serviceError.value();
    }

    @Override
    public void syncFromInner() {
        this.serviceError.value(((InnerAssociateErrorPDU) inner).value);
    }
}
