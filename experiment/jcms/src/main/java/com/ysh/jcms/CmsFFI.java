package com.ysh.jcms;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public interface CmsFFI extends Library {

    CmsFFI INSTANCE = Native.load("ccms", CmsFFI.class);

    /* ==================== Services ==================== */

    int cms_associate_request_encode(
        long reqId, String sapRef, int hasAuth,
        byte[] outBuf, IntByReference outLen
    );

    int cms_associate_request_decode(
        byte[] inBuf, int inLen,
        LongByReference reqId,
        byte[] sapRef, IntByReference sapRefCap,
        IntByReference hasAuth
    );

    int cms_release_request_encode(
        long reqId,
        byte[] outBuf, IntByReference outLen
    );

    int cms_release_request_decode(
        byte[] inBuf, int inLen,
        LongByReference reqId
    );

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
