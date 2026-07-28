package com.ysh.jcms.svc.negotiate;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * AssociateNegotiate-RequestPDU ::= SEQUENCE { reqId Int16U, apduSize [0]
 * IMPLICIT INT16U, asduSize [1] IMPLICIT INT32U, protocolVersion [2] IMPLICIT
 * INT32U } — 8.13
 */
public class CmsNegotiateRequest extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsInt16U apduSize;
    public CmsInt32U asduSize;
    public CmsInt32U protocolVersion;

    public CmsNegotiateRequest() {
        super(Codec.NEGOTIATE_REQUEST);
        this.reqId = new CmsReqId();
        this.apduSize = new CmsInt16U();
        this.asduSize = new CmsInt32U();
        this.protocolVersion = new CmsInt32U();
    }

    public CmsNegotiateRequest reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsNegotiateRequest apduSize(int v) {
        this.apduSize.value(v);
        return this;
    }
    public CmsNegotiateRequest asduSize(long v) {
        this.asduSize.value(v);
        return this;
    }
    public CmsNegotiateRequest protocolVersion(long v) {
        this.protocolVersion.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, apduSize, asduSize, protocolVersion);
    }
}
