package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsException;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CmsAbort extends Structure {

    public byte[] assocId = new byte[32];
    public int assocIdLen;
    public int reason;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "assocIdLen", "reason");
    }

    public CmsAbort() {}

    public CmsAbort(byte[] assocId, int reason) {
        this.assocIdLen = Math.min(assocId.length, this.assocId.length);
        System.arraycopy(assocId, 0, this.assocId, 0, this.assocIdLen);
        this.reason = reason;
    }

    public byte[] encode() {
        write();
        byte[] outBuf = new byte[65536];
        IntByReference outLen = new IntByReference(outBuf.length);
        int ret = CmsFFIServices.INSTANCE.cms_abort_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsAbort.encode failed: " + ret);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAbort decode(byte[] apdu) {
        CmsAbort sdu = new CmsAbort();
        int ret = CmsFFIServices.INSTANCE.cms_abort_decode(apdu, apdu.length, sdu);
        if (ret != 0) throw new CmsException("CmsAbort.decode failed: " + ret);
        sdu.read();
        return sdu;
    }

    @Override
    public String toString() {
        return "CmsAbort{assocId.length=" + assocIdLen + ", reason=" + reason + '}';
    }
}