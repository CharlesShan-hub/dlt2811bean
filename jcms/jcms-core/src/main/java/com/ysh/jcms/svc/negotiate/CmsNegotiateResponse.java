package com.ysh.jcms.svc.negotiate;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * AssociateNegotiate-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     apduSize        [0] IMPLICIT INT16U,
 *     asduSize        [1] IMPLICIT INT32U,
 *     protocolVersion [2] IMPLICIT INT32U,
 *     modelVersion    [3] IMPLICIT VisibleString
 * }  —  8.13
 */
public class CmsNegotiateResponse extends CmsType {

    public CmsReqId       reqId;
    public CmsInt16U      apduSize;
    public CmsInt32U      asduSize;
    public CmsInt32U      protocolVersion;
    public CmsUint8Array  modelVersion;     /* VisibleString */

    public CmsNegotiateResponse() { super(Codec.NEGOTIATE_RESPONSE);
        this.reqId           = new CmsReqId();
        this.apduSize        = new CmsInt16U();
        this.asduSize        = new CmsInt32U();
        this.protocolVersion = new CmsInt32U();
        this.modelVersion    = new CmsUint8Array();
    }
    
    public CmsNegotiateResponse reqId(int v) { this.reqId.value(v); return this; }
    public CmsNegotiateResponse apduSize(int v) { this.apduSize.value(v); return this; }
    public CmsNegotiateResponse asduSize(long v) { this.asduSize.value(v); return this; }
    public CmsNegotiateResponse protocolVersion(long v) { this.protocolVersion.value(v); return this; }
    public CmsNegotiateResponse modelVersion(byte[] v) { this.modelVersion.value(v); return this; }
    public CmsNegotiateResponse modelVersion(String v) { this.modelVersion.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, apduSize, asduSize, protocolVersion, modelVersion);
    }
}