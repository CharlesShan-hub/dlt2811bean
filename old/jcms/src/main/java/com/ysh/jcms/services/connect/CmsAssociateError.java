package com.ysh.jcms.services.connect;

import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsException;
import com.ysh.jcms.services.type.CmsFFIServices;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class CmsAssociateError extends Structure {

    public int serviceError;

    @Override
    protected List<String> getFieldOrder() {
        return Collections.singletonList("serviceError");
    }

    public CmsAssociateError() {}

    public CmsAssociateError(int serviceError) {
        this.serviceError = serviceError;
    }

    public byte[] encode() {
        write();
        byte[] outBuf = new byte[64];
        IntByReference outLen = new IntByReference(outBuf.length);
        int ret = CmsFFIServices.INSTANCE.cms_associate_error_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsAssociateError.encode failed: " + ret);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(outBuf, 0, result, 0, result.length);
        return result;
    }

    public static CmsAssociateError decode(byte[] apdu) {
        CmsAssociateError sdu = new CmsAssociateError();
        int ret = CmsFFIServices.INSTANCE.cms_associate_error_decode(apdu, apdu.length, sdu);
        if (ret != 0) throw new CmsException("CmsAssociateError.decode failed: " + ret);
        sdu.read();
        return sdu;
    }

    @Override
    public String toString() {
        return "CmsAssociateError{serviceError=" + serviceError + '}';
    }
}
