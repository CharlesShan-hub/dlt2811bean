package com.ysh.jcms;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public interface CmsFFI extends Library {

    CmsFFI INSTANCE = Native.load("libccms", CmsFFI.class);

    /* ==================== Services ==================== */

    int cms_encode_associate_request(
        long reqId, String sapRef, int hasAuth,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_associate_request(
        byte[] inBuf, int inLen,
        LongByReference reqId,
        byte[] sapRef, IntByReference sapRefCap,
        IntByReference hasAuth
    );

    int cms_encode_release_request(
        long reqId,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_release_request(
        byte[] inBuf, int inLen,
        LongByReference reqId
    );

    int cms_encode_abort(
        long reqId, long abortReason,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_abort(
        byte[] inBuf, int inLen,
        LongByReference reqId,
        LongByReference abortReason
    );
}
