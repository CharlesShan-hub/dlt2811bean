package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.data.time.CmsUtcTime;

/**
 * AuthenticationParameter ::= SEQUENCE { signatureCertificate [0] IMPLICIT
 * OCTET STRING, signedTime [1] IMPLICIT UtcTime, signedValue [2] IMPLICIT OCTET
 * STRING } — 8.2.1
 *
 * Field container used by CmsAssociateRequest and CmsAssociateResponse.
 * Encode/decode is handled by the parent PDU's Inner* type.
 */
public class CmsAuthenticationParameter extends CmsType {

    public CmsUint8Array signature;
    public CmsUtcTime signedTime;
    public CmsUint8Array signedValue;

    public CmsAuthenticationParameter() {
        this.signature = new CmsUint8Array();
        this.signedTime = new CmsUtcTime();
        this.signedValue = new CmsUint8Array();
    }
}
