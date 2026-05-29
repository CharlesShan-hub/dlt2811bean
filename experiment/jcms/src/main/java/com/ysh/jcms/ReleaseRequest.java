package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.Getter;

@Getter
public final class ReleaseRequest {

    private static final int BUF_SIZE = 65536;

    private final long reqId;

    public ReleaseRequest(long reqId) {
        this.reqId = reqId;
    }

    public byte[] encode() {
        IntByReference outLen = new IntByReference(BUF_SIZE);
        byte[] outBuf = new byte[BUF_SIZE];
        int ret = CmsFFI.INSTANCE.cms_encode_release_request(
            reqId, outBuf, outLen
        );
        if (ret != 0) {
            throw new CmsException("ReleaseRequest.encode failed: " + ret);
        }
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static ReleaseRequest decode(byte[] apdu) {
        LongByReference reqId = new LongByReference();
        int ret = CmsFFI.INSTANCE.cms_decode_release_request(
            apdu, apdu.length, reqId
        );
        if (ret != 0) {
            throw new CmsException("ReleaseRequest.decode failed: " + ret);
        }
        return new ReleaseRequest(reqId.getValue());
    }

    @Override
    public String toString() {
        return "ReleaseRequest{reqId=" + reqId + '}';
    }
}
