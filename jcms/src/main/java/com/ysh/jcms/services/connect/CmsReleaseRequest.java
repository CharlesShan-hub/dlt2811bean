package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsException;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CmsReleaseRequest extends Structure {

    public byte[] assocId = new byte[32];
    public int assocIdLen;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "assocIdLen");
    }

    public CmsReleaseRequest() {}

    public CmsReleaseRequest(byte[] assocId) {
        this.assocIdLen = Math.min(assocId.length, this.assocId.length);
        System.arraycopy(assocId, 0, this.assocId, 0, this.assocIdLen);
    }

    public byte[] encode() {
        write();
        byte[] outBuf = new byte[65536];
        IntByReference outLen = new IntByReference(outBuf.length);
        int ret = CmsFFIServices.INSTANCE.cms_release_request_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsReleaseRequest.encode failed: " + ret);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static CmsReleaseRequest decode(byte[] apdu) {
        CmsReleaseRequest sdu = new CmsReleaseRequest();
        int ret = CmsFFIServices.INSTANCE.cms_release_request_decode(apdu, apdu.length, sdu);
        if (ret != 0) throw new CmsException("CmsReleaseRequest.decode failed: " + ret);
        sdu.read();
        return sdu;
    }

    @Override
    public String toString() {
        return "CmsReleaseRequest{assocId.length=" + assocIdLen + '}';
    }
}
