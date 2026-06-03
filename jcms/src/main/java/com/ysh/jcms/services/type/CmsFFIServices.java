package com.ysh.jcms.services.type;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.services.connect.CmsAbort;
import com.ysh.jcms.services.connect.CmsAssociateError;
import com.ysh.jcms.services.connect.CmsAssociateRequest;
import com.ysh.jcms.services.connect.CmsAssociateResponse;
import com.ysh.jcms.services.connect.CmsReleaseError;
import com.ysh.jcms.services.connect.CmsReleaseRequest;
import com.ysh.jcms.services.connect.CmsReleaseResponse;

public interface CmsFFIServices extends Library {

    CmsFFIServices INSTANCE = Native.load("ccms", CmsFFIServices.class);

    /* ==================== AbortReason (INTEGER 0..5) ==================== */

    int cms_abort_reason_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_abort_reason_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== Abort ==================== */

    int cms_abort_encode(CmsAbort sdu, byte[] outBuf, IntByReference outLen);

    int cms_abort_decode(byte[] inBuf, int inLen, CmsAbort sdu);

    /* ==================== Associate ==================== */

    int cms_associate_request_encode(CmsAssociateRequest sdu, byte[] outBuf, IntByReference outLen);

    int cms_associate_request_decode(byte[] inBuf, int inLen, CmsAssociateRequest sdu);

    int cms_associate_response_encode(CmsAssociateResponse sdu, byte[] outBuf, IntByReference outLen);

    int cms_associate_response_decode(byte[] inBuf, int inLen, CmsAssociateResponse sdu);

    int cms_associate_error_encode(CmsAssociateError sdu, byte[] outBuf, IntByReference outLen);

    int cms_associate_error_decode(byte[] inBuf, int inLen, CmsAssociateError sdu);

    /* ==================== Release ==================== */

    int cms_release_request_encode(CmsReleaseRequest sdu, byte[] outBuf, IntByReference outLen);

    int cms_release_request_decode(byte[] inBuf, int inLen, CmsReleaseRequest sdu);

    int cms_release_response_encode(CmsReleaseResponse sdu, byte[] outBuf, IntByReference outLen);

    int cms_release_response_decode(byte[] inBuf, int inLen, CmsReleaseResponse sdu);

    int cms_release_error_encode(CmsReleaseError sdu, byte[] outBuf, IntByReference outLen);

    int cms_release_error_decode(byte[] inBuf, int inLen, CmsReleaseError sdu);
}
