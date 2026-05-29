package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import lombok.Getter;

@Getter
public final class Abort {

    private static final int BUF_SIZE = 65536;

    private final long reqId;
    private final long abortReason;

    public Abort(long reqId, long abortReason) {
        this.reqId = reqId;
        this.abortReason = abortReason;
    }

    public byte[] encode() {
        IntByReference outLen = new IntByReference(BUF_SIZE);
        byte[] outBuf = new byte[BUF_SIZE];
        int ret = CmsFFI.INSTANCE.cms_ffi_encode_abort(
            reqId, abortReason, outBuf, outLen
        );
        if (ret != 0) {
            throw new CmsException("Abort.encode failed: " + ret);
        }
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static Abort decode(byte[] apdu) {
        LongByReference reqId = new LongByReference();
        LongByReference abortReason = new LongByReference();
        int ret = CmsFFI.INSTANCE.cms_ffi_decode_abort(
            apdu, apdu.length, reqId, abortReason
        );
        if (ret != 0) {
            throw new CmsException("Abort.decode failed: " + ret);
        }
        return new Abort(reqId.getValue(), abortReason.getValue());
    }

    @Override
    public String toString() {
        return "Abort{reqId=" + reqId + ", abortReason=" + abortReason + '}';
    }
}
