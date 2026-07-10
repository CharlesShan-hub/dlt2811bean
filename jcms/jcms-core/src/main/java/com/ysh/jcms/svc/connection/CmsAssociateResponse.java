package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.svc.other.CmsAssociationId;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Associate-ResponsePDU ::= SEQUENCE { reqId Int16U, associationId [0] IMPLICIT
 * OCTET STRING (SIZE(0..64)), serviceError [1] IMPLICIT ServiceError,
 * authenticationParameter [2] IMPLICIT AuthenticationParameter OPTIONAL } —
 * 8.2.1
 */
public class CmsAssociateResponse extends CmsType {

    public CmsReqId reqId;
    public CmsAssociationId assocId;
    public CmsServiceError serviceError;
    public CmsBoolean authParamPresent;
    public CmsAuthenticationParameter authParam; /* OPTIONAL */

    public CmsAssociateResponse() {
        super(Codec.ASSOCIATE_RESPONSE);
        this.reqId = new CmsReqId();
        this.assocId = new CmsAssociationId();
        this.serviceError = new CmsServiceError();
        this.authParamPresent = new CmsBoolean();
        this.authParam = new CmsAuthenticationParameter();
    }

    public CmsAssociateResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsAssociateResponse assocId(byte[] v) {
        this.assocId.value(v);
        return this;
    }
    public CmsAssociateResponse assocId(String v) {
        this.assocId.value(v);
        return this;
    }
    public CmsAssociateResponse serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }
    public CmsAssociateResponse authParamPresent(boolean v) {
        this.authParamPresent.value(v);
        return this;
    }
    public CmsAssociateResponse authParam(CmsAuthenticationParameter v) {
        this.authParam = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, assocId, serviceError, authParamPresent, authParam);
    }
}
