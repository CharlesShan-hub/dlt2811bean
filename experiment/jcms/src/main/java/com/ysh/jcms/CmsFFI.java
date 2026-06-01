package com.ysh.jcms;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public interface CmsFFI extends Library {

    CmsFFI INSTANCE = Native.load("ccms", CmsFFI.class);

    /* ==================== Associate ==================== */

    int cms_associate_request_encode(
        String sapRef, int hasAuth,
        byte[] cert, int certLen, long signedTime,
        byte[] sigVal, int sigLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_associate_request_decode(
        byte[] inBuf, int inLen,
        byte[] sapRef, IntByReference sapRefCap,
        IntByReference hasAuth,
        byte[] cert, IntByReference certCap,
        LongByReference signedTime,
        byte[] sigVal, IntByReference sigValCap
    );

    int cms_associate_response_encode(
        byte[] assocId, int assocIdLen, int serviceError, int hasAuth,
        byte[] cert, int certLen, long signedTime,
        byte[] sigVal, int sigLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_associate_response_decode(
        byte[] inBuf, int inLen,
        byte[] assocId, IntByReference assocIdCap,
        IntByReference serviceError,
        IntByReference hasAuth,
        byte[] cert, IntByReference certCap,
        LongByReference signedTime,
        byte[] sigVal, IntByReference sigValCap
    );

    int cms_associate_error_encode(
        int serviceError,
        byte[] outBuf, IntByReference outLen
    );

    int cms_associate_error_decode(
        byte[] inBuf, int inLen,
        IntByReference serviceError
    );

    /* ==================== Release ==================== */

    int cms_release_request_encode(
        long reqId,
        byte[] outBuf, IntByReference outLen
    );

    int cms_release_request_decode(
        byte[] inBuf, int inLen,
        LongByReference reqId
    );

    /* ==================== Abort ==================== */

    int cms_abort_encode(
        long reqId, long abortReason,
        byte[] outBuf, IntByReference outLen
    );

    int cms_abort_decode(
        byte[] inBuf, int inLen,
        LongByReference reqId,
        LongByReference abortReason
    );
}
