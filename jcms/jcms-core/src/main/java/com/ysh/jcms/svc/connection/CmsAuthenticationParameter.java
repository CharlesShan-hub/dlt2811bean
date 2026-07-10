package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.string.CmsUint8Array;
import com.ysh.jcms.data.time.CmsUtcTime;
import java.util.Arrays;
import java.util.List;

/**
 * AuthenticationParameter ::= SEQUENCE { signatureCertificate [0] IMPLICIT
 * OCTET STRING, signedTime [1] IMPLICIT UtcTime, signedValue [2] IMPLICIT OCTET
 * STRING } — 8.2.1
 */
public class CmsAuthenticationParameter extends CmsType {

    public CmsUint8Array cert;
    public CmsUtcTime signedTime;
    public CmsUint8Array sigVal;

    public CmsAuthenticationParameter() {
        this.cert = new CmsUint8Array();
        this.signedTime = new CmsUtcTime();
        this.sigVal = new CmsUint8Array();
    }

    public CmsAuthenticationParameter cert(byte[] v) {
        this.cert.value(v);
        return this;
    }
    public CmsAuthenticationParameter cert(String v) {
        this.cert.value(v);
        return this;
    }
    public CmsAuthenticationParameter signedTime(CmsUtcTime v) {
        this.signedTime = v;
        return this;
    }
    public CmsAuthenticationParameter sigVal(byte[] v) {
        this.sigVal.value(v);
        return this;
    }
    public CmsAuthenticationParameter sigVal(String v) {
        this.sigVal.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(cert, signedTime, sigVal);
    }
}
