package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsException;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CmsAssociateResponse extends Structure {

    public byte[] assocId = new byte[32];
    public int assocIdLen;
    public int serviceError;
    public int hasAuth;
    public byte[] cert = new byte[2048];
    public int certLen;
    public long signedTime;
    public byte[] sigVal = new byte[2048];
    public int sigLen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "assocIdLen", "serviceError", "hasAuth",
            "cert", "certLen", "signedTime", "sigVal", "sigLen");
    }

    public CmsAssociateResponse() {}

    public CmsAssociateResponse(byte[] assocId, int serviceError, boolean hasAuthenticationParameter,
                                byte[] cert, long signedTime, byte[] sigVal) {
        this.assocIdLen = Math.min(assocId.length, this.assocId.length);
        System.arraycopy(assocId, 0, this.assocId, 0, this.assocIdLen);
        this.serviceError = serviceError;
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
        int ret = CmsFFIServices.INSTANCE.cms_associate_response_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsAssociateResponse.encode failed: " + ret);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAssociateResponse decode(byte[] apdu) {
        CmsAssociateResponse sdu = new CmsAssociateResponse();
        int ret = CmsFFIServices.INSTANCE.cms_associate_response_decode(apdu, apdu.length, sdu);
        if (ret != 0) throw new CmsException("CmsAssociateResponse.decode failed: " + ret);
        sdu.read();
        return sdu;
    }

    @Override
    public String toString() {
        return "CmsAssociateResponse{assocId.length=" + assocIdLen + ", serviceError=" + serviceError + '}';
    }
}
