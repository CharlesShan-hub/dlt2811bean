package com.ysh.jcms.services.connect;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
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
        PointerByReference outBuf = new PointerByReference();
        LongByReference outLen = new LongByReference();
        int ret = CmsFFIServices.INSTANCE.cms_associate_error_encode(this, outBuf, outLen);
        if (ret != 0) throw new CmsException("CmsAssociateError.encode failed: " + ret);
        byte[] result = outBuf.getValue().getByteArray(0, (int)outLen.getValue());
        Native.free(Pointer.nativeValue(outBuf.getValue()));
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
