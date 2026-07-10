package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Associate-RequestPDU ::= SEQUENCE { reqId Int16U, serverAccessPointReference
 * [0] IMPLICIT VisibleString129 OPTIONAL, authenticationParameter [1] IMPLICIT
 * AuthenticationParameter OPTIONAL } — 8.2.1
 */
public class CmsAssociateRequest extends CmsType {

    public CmsReqId reqId;
    public CmsBoolean sapRefPresent;
    public CmsUint8Array sapRef; /* VisibleString129 OPTIONAL */
    public CmsBoolean authParamPresent;
    public CmsAuthenticationParameter authParam; /* OPTIONAL */

    public CmsAssociateRequest() {
        super(Codec.ASSOCIATE_REQUEST);
        this.reqId = new CmsReqId();
        this.sapRefPresent = new CmsBoolean();
        this.sapRef = new CmsUint8Array();
        this.authParamPresent = new CmsBoolean();
        this.authParam = new CmsAuthenticationParameter();
    }

    public CmsAssociateRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsAssociateRequest sapRefPresent(boolean v) {
        this.sapRefPresent.value(v);
        return this;
    }
    public CmsAssociateRequest sapRef(byte[] v) {
        this.sapRefPresent.value(v != null && v.length > 0);
        if (v != null)
            this.sapRef.value(v);
        return this;
    }
    public CmsAssociateRequest sapRef(String v) {
        this.sapRefPresent.value(v != null);
        if (v != null)
            this.sapRef.value(v);
        return this;
    }
    public CmsAssociateRequest authParamPresent(boolean v) {
        this.authParamPresent.value(v);
        return this;
    }
    public CmsAssociateRequest authParam(CmsAuthenticationParameter v) {
        this.authParam = v;
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, sapRefPresent, sapRef, authParamPresent, authParam);
    }
}
