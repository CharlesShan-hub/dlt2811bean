package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsException;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CmsReleaseResponse extends Structure {

    public byte[] assocId = new byte[32];
    public int assocIdLen;
    public int serviceError;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("assocId", "assocIdLen", "serviceError");
    }

    public CmsReleaseResponse() {}

    public CmsReleaseResponse(byte[] assocId, int serviceError) {
        this.assocIdLen = Math.min(assocId.length, this.assocId.length);
        System.arraycopy(assocId, 0, this.assocId, 0, this.assocIdLen);
        this.serviceError = serviceError;
    }

    public byte[] encode() {
        write();
        byte[] outBuf = new byte[65536];
        IntByReference outLen = new IntByReference(outBuf.length);
        int ret = CmsFFIServices.INSTANCE.cms_release_response_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsReleaseResponse.encode failed: " + ret);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static CmsReleaseResponse decode(byte[] apdu) {
        CmsReleaseResponse sdu = new CmsReleaseResponse();
        int ret = CmsFFIServices.INSTANCE.cms_release_response_decode(apdu, apdu.length, sdu);
        if (ret != 0) throw new CmsException("CmsReleaseResponse.decode failed: " + ret);
        sdu.read();
        return sdu;
    }

    @Override
    public String toString() {
        return "CmsReleaseResponse{assocId.length=" + assocIdLen + ", serviceError=" + serviceError + '}';
    }
}
