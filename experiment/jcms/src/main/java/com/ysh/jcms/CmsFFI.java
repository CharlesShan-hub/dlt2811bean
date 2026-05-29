package com.ysh.jcms;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;


public interface CmsFFI extends Library {

    CmsFFI INSTANCE = Native.load("libcmsper_datatypes", CmsFFI.class);

    /* ==================== Services ==================== */

    int cms_ffi_encode_associate_request(
        long reqId, String sapRef, int hasAuth,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_associate_request(
        byte[] inBuf, int inLen,
        LongByReference reqId,
        byte[] sapRef, IntByReference sapRefCap,
        IntByReference hasAuth
    );

    int cms_ffi_encode_release_request(
        long reqId,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_release_request(
        byte[] inBuf, int inLen,
        LongByReference reqId
    );

    int cms_ffi_encode_abort(
        long reqId, long abortReason,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_abort(
        byte[] inBuf, int inLen,
        LongByReference reqId,
        LongByReference abortReason
    );

    /* ==================== 7.1.1 BOOLEAN ==================== */

    int cms_ffi_encode_BOOLEAN(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_BOOLEAN(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    /* ==================== 7.1.2 Integer Types ==================== */

    int cms_ffi_encode_Int8(
        byte value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int8(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Int8U(
        short value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int8U(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Int16(
        short value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int16(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Int16U(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int16U(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Int32(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int32(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Int32U(
        long value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int32U(
        byte[] inBuf, int inLen,
        LongByReference value
    );

    int cms_ffi_encode_Int64(
        long value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int64(
        byte[] inBuf, int inLen,
        LongByReference value
    );

    int cms_ffi_encode_Int64U(
        long value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Int64U(
        byte[] inBuf, int inLen,
        LongByReference value
    );

    /* ==================== 7.1.4 Floating-Point Types ==================== */

    int cms_ffi_encode_Float32(
        float value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Float32(
        byte[] inBuf, int inLen,
        float[] value
    );

    int cms_ffi_encode_Float64(
        double value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Float64(
        byte[] inBuf, int inLen,
        double[] value
    );

    /* ==================== 7.1.5 String Types ==================== */

    int cms_ffi_encode_VisibleString(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_VisibleString(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    int cms_ffi_encode_UTF8String(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_UTF8String(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    int cms_ffi_encode_OctetString(
        byte[] value, int valueLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_OctetString(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.2.1 ObjectName ==================== */

    int cms_ffi_encode_ObjectName(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_ObjectName(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.2.2 ObjectReference ==================== */

    int cms_ffi_encode_ObjectReference(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_ObjectReference(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.2.3 FC ==================== */

    int cms_ffi_encode_FC(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_FC(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.2.4 SubReference ==================== */

    int cms_ffi_encode_SubReference(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_SubReference(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.2.5 EntryID ==================== */

    int cms_ffi_encode_EntryID(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_EntryID(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.2.6 BitString ==================== */

    int cms_ffi_encode_BitString(
        byte[] value, int valueLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_BitString(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.3.1 BinaryTime ==================== */

    int cms_ffi_encode_BinaryTime(
        int hour, int minute, int second, int millisecond,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_BinaryTime(
        byte[] inBuf, int inLen,
        IntByReference hour, IntByReference minute,
        IntByReference second, IntByReference millisecond
    );

    /* ==================== 7.3.2 UtcTime ==================== */

    int cms_ffi_encode_UtcTime(
        long timestampMs,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_UtcTime(
        byte[] inBuf, int inLen,
        LongByReference timestampMs
    );

    /* ==================== 7.3.3 TimeStamp ==================== */

    int cms_ffi_encode_TimeStamp(
        long secondsSinceEpoch, long fractional,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_TimeStamp(
        byte[] inBuf, int inLen,
        LongByReference secondsSinceEpoch, LongByReference fractional
    );

    /* ==================== 7.4 Originator ==================== */

    int cms_ffi_encode_Originator(
        int orCat,
        byte[] orIdent, int orIdentLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Originator(
        byte[] inBuf, int inLen,
        IntByReference orCat,
        byte[] orIdent, IntByReference orIdentCap
    );

    /* ==================== 7.5 PhyComAddr ==================== */

    int cms_ffi_encode_PhyComAddr(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_PhyComAddr(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6 CODED ENUM types ==================== */

    int cms_ffi_encode_Quality(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Quality(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_Dbpos(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Dbpos(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Tcmd(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Tcmd(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_Check(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Check(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_LcbOptFlds(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_LcbOptFlds(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_MsvcbOptFlds(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_MsvcbOptFlds(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_RcbOptFlds(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_RcbOptFlds(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_ReasonCode(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_ReasonCode(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_TimeQuality(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_TimeQuality(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_TriggerConditions(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_TriggerConditions(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_ffi_encode_PackedList(
        byte[] value, int valueLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_PackedList(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.7 Data ==================== */

    int cms_ffi_encode_Data(
        int choice,
        long intVal, double floatVal,
        String strVal, byte[] bytesVal, int bytesLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_Data(
        byte[] inBuf, int inLen,
        IntByReference choice,
        LongByReference intVal, double[] floatVal,
        byte[] strVal, IntByReference strCap,
        byte[] bytesVal, IntByReference bytesCap
    );

    /* ==================== 7.8 DataDefinition ==================== */

    int cms_ffi_encode_DataDefinition(
        String dataName, String dataType,
        byte[] fc,
        int dataChoice, long dataInt, double dataFloat,
        String dataStr, byte[] dataBytes, int dataBytesLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_DataDefinition(
        byte[] inBuf, int inLen,
        byte[] dataName, IntByReference dataNameCap,
        byte[] dataType, IntByReference dataTypeCap,
        byte[] fc,
        IntByReference dataChoice, LongByReference dataInt, double[] dataFloat,
        byte[] dataStr, IntByReference dataStrCap,
        byte[] dataBytes, IntByReference dataBytesCap
    );

    /* ==================== 7.9 ServiceError ==================== */

    int cms_ffi_encode_ServiceError(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_ServiceError(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    /* ==================== 7.10 AddCause / OrCat / SmpMod ==================== */

    int cms_ffi_encode_AddCause(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_AddCause(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_OrCat(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_OrCat(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_ffi_encode_SmpMod(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_ffi_decode_SmpMod(
        byte[] inBuf, int inLen,
        IntByReference value
    );
}
