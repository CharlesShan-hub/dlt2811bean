package com.ysh.jcms.svc.negotiate;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * AssociateNegotiate-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.13
 */
public class CmsNegotiateError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsNegotiateError() { super(Codec.NEGOTIATE_ERROR);
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsNegotiateError reqId(int v) { this.reqId.value(v); return this; }
    public CmsNegotiateError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}