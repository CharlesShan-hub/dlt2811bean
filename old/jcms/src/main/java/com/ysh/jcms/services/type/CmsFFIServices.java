package com.ysh.jcms.services.type;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

public interface CmsFFIServices extends Library {

    CmsFFIServices INSTANCE = Native.load("ccms", CmsFFIServices.class);

    /* ==================== AbortReason (INTEGER 0..5) ==================== */

    int cms_abort_reason_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_abort_reason_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== Abort ==================== */

    int cms_abort_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_abort_decode(byte[] inBuf, int inLen, Structure sdu);

    /* ==================== Associate ==================== */

    int cms_associate_request_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_associate_request_decode(byte[] inBuf, int inLen, Structure sdu);

    int cms_associate_response_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_associate_response_decode(byte[] inBuf, int inLen, Structure sdu);

    int cms_associate_error_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_associate_error_decode(byte[] inBuf, int inLen, Structure sdu);

    /* ==================== Release ==================== */

    int cms_release_request_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_release_request_decode(byte[] inBuf, int inLen, Structure sdu);

    int cms_release_response_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_release_response_decode(byte[] inBuf, int inLen, Structure sdu);

    int cms_release_error_encode(Structure sdu, byte[] outBuf, IntByReference outLen);

    int cms_release_error_decode(byte[] inBuf, int inLen, Structure sdu);
}
