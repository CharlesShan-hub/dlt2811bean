package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public final class AssociateRequest {

    private static final int BUF_SIZE = 65536;

    private final long reqId;
    private final String serverAccessPointReference;
    private final boolean hasAuthenticationParameter;

    public AssociateRequest(long reqId, String serverAccessPointReference, boolean hasAuthenticationParameter) {
        this.reqId = reqId;
        this.serverAccessPointReference = serverAccessPointReference;
        this.hasAuthenticationParameter = hasAuthenticationParameter;
    }

    public byte[] encode() {
        IntByReference outLen = new IntByReference(BUF_SIZE);
        byte[] outBuf = new byte[BUF_SIZE];
        int ret = CmsFFI.INSTANCE.cms_associate_request_encode(
            reqId, serverAccessPointReference, hasAuthenticationParameter ? 1 : 0,
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
        LongByReference reqId = new LongByReference();
        byte[] sapRef = new byte[256];
        IntByReference sapRefCap = new IntByReference(sapRef.length);
        IntByReference hasAuth = new IntByReference();
        int ret = CmsFFI.INSTANCE.cms_associate_request_decode(
            apdu, apdu.length,
            reqId, sapRef, sapRefCap, hasAuth
        );
        if (ret != 0) {
            throw new CmsException("AssociateRequest.decode failed: " + ret);
        }
        int len = sapRefCap.getValue();
        String sapRefStr = new String(sapRef, 0, len, StandardCharsets.UTF_8);
        return new AssociateRequest(reqId.getValue(), sapRefStr, hasAuth.getValue() != 0);
    }

    @Override
    public String toString() {
        return "AssociateRequest{reqId=" + reqId
            + ", sapRef='" + serverAccessPointReference + '\''
            + ", hasAuth=" + hasAuthenticationParameter + '}';
    }
}
