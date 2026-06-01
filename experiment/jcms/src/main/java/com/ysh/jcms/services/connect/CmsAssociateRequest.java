package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsException;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Getter
public class CmsAssociateRequest extends Structure {

    public byte[] sapRef = new byte[65];
    public int hasAuth;
    public byte[] cert = new byte[2048];
    public int certLen;
    public long signedTime;
    public byte[] sigVal = new byte[2048];
    public int sigLen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sapRef", "hasAuth", "cert", "certLen", "signedTime", "sigVal", "sigLen");
    }

    public CmsAssociateRequest() {}

    public CmsAssociateRequest(String serverAccessPointReference, boolean hasAuthenticationParameter,
                               byte[] cert, long signedTime, byte[] sigVal) {
        byte[] sapBytes = serverAccessPointReference.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(sapBytes, 0, this.sapRef, 0, Math.min(sapBytes.length, this.sapRef.length - 1));
        this.hasAuth = hasAuthenticationParameter ? 1 : 0;
        if (cert != null) {
            this.certLen = Math.min(cert.length, this.cert.length);
            System.arraycopy(cert, 0, this.cert, 0, this.certLen);
        }
        this.signedTime = signedTime;
        if (sigVal != null) {
            this.sigLen = Math.min(sigVal.length, this.sigVal.length);
            System.arraycopy(sigVal, 0, this.sigVal, 0, this.sigLen);
        }
    }

    public byte[] encode() {
        write();
        byte[] outBuf = new byte[65536];
        IntByReference outLen = new IntByReference(outBuf.length);
        int ret = CmsFFIServices.INSTANCE.cms_associate_request_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsAssociateRequest.encode failed: " + ret);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAssociateRequest decode(byte[] apdu) {
        CmsAssociateRequest sdu = new CmsAssociateRequest();
        int ret = CmsFFIServices.INSTANCE.cms_associate_request_decode(apdu, apdu.length, sdu);
        if (ret != 0) throw new CmsException("CmsAssociateRequest.decode failed: " + ret);
        sdu.read();
        return sdu;
    }

    @Override
    public String toString() {
        return "CmsAssociateRequest{sapRef='" + new String(sapRef, StandardCharsets.UTF_8).trim() + "', hasAuth=" + hasAuth + '}';
    }
}
