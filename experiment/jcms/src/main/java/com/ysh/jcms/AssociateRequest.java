package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public final class AssociateRequest {

    private static final int BUF_SIZE = 65536;

    private final String serverAccessPointReference;
    private final boolean hasAuthenticationParameter;
    private final byte[] cert;
    private final long signedTime;
    private final byte[] sigVal;

    public AssociateRequest(
        String serverAccessPointReference,
        boolean hasAuthenticationParameter,
        byte[] cert, long signedTime, byte[] sigVal) {
        this.serverAccessPointReference = serverAccessPointReference;
        this.hasAuthenticationParameter = hasAuthenticationParameter;
        this.cert = cert;
        this.signedTime = signedTime;
        this.sigVal = sigVal;
    }

    public byte[] encode() {
        IntByReference outLen = new IntByReference(BUF_SIZE);
        byte[] outBuf = new byte[BUF_SIZE];
        byte[] certData = cert != null ? cert : new byte[0];
        byte[] sigData = sigVal != null ? sigVal : new byte[0];
        int ret = CmsFFI.INSTANCE.cms_associate_request_encode(
            serverAccessPointReference, hasAuthenticationParameter ? 1 : 0,
            certData, certData.length, signedTime,
            sigData, sigData.length,
            outBuf, outLen
        );
        if (ret != 0) {
            throw new CmsException("AssociateRequest.encode failed: " + ret);
        }
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static AssociateRequest decode(byte[] apdu) {
        byte[] sapRef = new byte[256];
        IntByReference sapRefCap = new IntByReference(sapRef.length);
        IntByReference hasAuth = new IntByReference();
        byte[] cert = new byte[4096];
        IntByReference certCap = new IntByReference(cert.length);
        LongByReference signedTime = new LongByReference();
        byte[] sigVal = new byte[4096];
        IntByReference sigValCap = new IntByReference(sigVal.length);

        int ret = CmsFFI.INSTANCE.cms_associate_request_decode(
            apdu, apdu.length,
            sapRef, sapRefCap, hasAuth,
            cert, certCap, signedTime,
            sigVal, sigValCap
        );
        if (ret != 0) {
            throw new CmsException("AssociateRequest.decode failed: " + ret);
        }
        int sapLen = sapRefCap.getValue();
        String sapRefStr = new String(sapRef, 0, sapLen, StandardCharsets.UTF_8);
        byte[] certOut = new byte[certCap.getValue()];
        System.arraycopy(cert, 0, certOut, 0, certOut.length);
        byte[] sigOut = new byte[sigValCap.getValue()];
        System.arraycopy(sigVal, 0, sigOut, 0, sigOut.length);
        return new AssociateRequest(
            sapRefStr,
            hasAuth.getValue() != 0,
            certOut, signedTime.getValue(), sigOut
        );
    }

    @Override
    public String toString() {
        return "AssociateRequest{sapRef='" + serverAccessPointReference + '\''
            + ", hasAuth=" + hasAuthenticationParameter + '}';
    }
}
