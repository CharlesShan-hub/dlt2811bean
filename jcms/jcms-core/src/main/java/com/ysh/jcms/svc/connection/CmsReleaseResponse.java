package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsAssociationId;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Release-ResponsePDU ::= SEQUENCE { reqId Int16U, associationId [0] IMPLICIT
 * OCTET STRING (SIZE(0..64)), serviceError [1] IMPLICIT ServiceError } — 8.2.2
 */
public class CmsReleaseResponse extends CmsType {

    public CmsReqId reqId;
    public CmsAssociationId assocId;
    public CmsServiceError serviceError;

    public CmsReleaseResponse() {
        super(Codec.RELEASE_RESPONSE);
        this.reqId = new CmsReqId();
        this.assocId = new CmsAssociationId();
        this.serviceError = new CmsServiceError();
    }

    public CmsReleaseResponse reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsReleaseResponse assocId(byte[] v) {
        this.assocId.value(v);
        return this;
    }
    public CmsReleaseResponse assocId(String v) {
        this.assocId.value(v);
        return this;
    }
    public CmsReleaseResponse serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, assocId, serviceError);
    }
}
