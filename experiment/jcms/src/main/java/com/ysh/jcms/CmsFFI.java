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

    /* ==================== 7.1.1 BOOLEAN ==================== */

    int cms_encode_BOOLEAN(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_BOOLEAN(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    /* ==================== 7.1.2 Integer Types ==================== */

    int cms_encode_Int8(
        byte value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int8(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Int8U(
        short value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int8U(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Int16(
        short value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int16(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Int16U(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int16U(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Int24U(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int24U(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Int32(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int32(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Int32U(
        long value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int32U(
        byte[] inBuf, int inLen,
        LongByReference value
    );

    int cms_encode_Int64(
        long value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int64(
        byte[] inBuf, int inLen,
        LongByReference value
    );

    int cms_encode_Int64U(
        long value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Int64U(
        byte[] inBuf, int inLen,
        LongByReference value
    );

    /* ==================== 7.1.4 Floating-Point Types ==================== */

    int cms_encode_Float32(
        float value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Float32(
        byte[] inBuf, int inLen,
        float[] value
    );

    int cms_encode_Float64(
        double value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Float64(
        byte[] inBuf, int inLen,
        double[] value
    );

    /* ==================== 7.1.5 String Types ==================== */

    int cms_encode_VisibleString(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_VisibleString(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    int cms_encode_UTF8String(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_UTF8String(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    int cms_encode_OctetString(
        byte[] value, int valueLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_OctetString(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.3.1 ObjectName ==================== */

    int cms_encode_ObjectName(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_ObjectName(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.3.2 ObjectReference ==================== */

    int cms_encode_ObjectReference(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_ObjectReference(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.4 FC ==================== */

    int cms_encode_FC(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_FC(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.3.3 SubReference ==================== */

    int cms_encode_SubReference(
        String value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_SubReference(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.3.8 EntryID ==================== */

    int cms_encode_EntryID(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_EntryID(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.1.8 BitString / PackedList ==================== */

    int cms_encode_BitString(
        byte[] value, int valueLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_BitString(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    int cms_encode_PackedList(
        byte[] value, int valueLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_PackedList(
        byte[] inBuf, int inLen,
        byte[] value, IntByReference valueCap
    );

    /* ==================== 7.2.2 BinaryTime ==================== */

    int cms_encode_BinaryTime(
        int hour, int minute, int second, int millisecond,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_BinaryTime(
        byte[] inBuf, int inLen,
        IntByReference hour, IntByReference minute,
        IntByReference second, IntByReference millisecond
    );

    /* ==================== 7.2.1 UtcTime ==================== */

    int cms_encode_UtcTime(
        long timestampMs,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_UtcTime(
        byte[] inBuf, int inLen,
        LongByReference timestampMs
    );

    /* ==================== 7.3.4 TimeStamp ==================== */

    int cms_encode_TimeStamp(
        long secondsSinceEpoch, long fractional,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_TimeStamp(
        byte[] inBuf, int inLen,
        LongByReference secondsSinceEpoch, LongByReference fractional
    );

    /* ==================== 7.5.2 Originator ==================== */

    int cms_encode_Originator(
        int orCat,
        byte[] orIdent, int orIdentLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Originator(
        byte[] inBuf, int inLen,
        IntByReference orCat,
        byte[] orIdent, IntByReference orIdentCap
    );

    /* ==================== 7.3.12 PhyComAddr ==================== */

    int cms_encode_PhyComAddr(
        byte[] addr, int priority, int vid, int appid,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_PhyComAddr(
        byte[] inBuf, int inLen,
        byte[] addr,
        IntByReference priority, IntByReference vid, IntByReference appid
    );

    /* ==================== 7.3.6 Quality / 7.3.5 Dbpos / 7.3.7 Tcmd / 7.5.3 Check ==================== */

    int cms_encode_Quality(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Quality(
        byte[] inBuf, int inLen,
        byte[] value
    );

    int cms_encode_Dbpos(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Dbpos(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Tcmd(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Tcmd(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    int cms_encode_Check(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Check(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6.5 LcbOptFlds ==================== */

    int cms_encode_LcbOptFlds(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_LcbOptFlds(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6.6 MsvcbOptFlds ==================== */

    int cms_encode_MsvcbOptFlds(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_MsvcbOptFlds(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6.4 RcbOptFlds ==================== */

    int cms_encode_RcbOptFlds(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_RcbOptFlds(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6.3 ReasonCode ==================== */

    int cms_encode_ReasonCode(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_ReasonCode(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.2.1 TimeQuality ==================== */

    int cms_encode_TimeQuality(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_TimeQuality(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6.2 TriggerConditions ==================== */

    int cms_encode_TriggerConditions(
        byte[] value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_TriggerConditions(
        byte[] inBuf, int inLen,
        byte[] value
    );

    /* ==================== 7.6.7 SmpMod ==================== */

    int cms_encode_SmpMod(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_SmpMod(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    /* ==================== 7.7 Data ==================== */

    int cms_encode_Data(
        int choice,
        long intVal, double floatVal,
        String strVal, byte[] bytesVal, int bytesLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_Data(
        byte[] inBuf, int inLen,
        IntByReference choice,
        LongByReference intVal, double[] floatVal,
        byte[] strVal, IntByReference strCap,
        byte[] bytesVal, IntByReference bytesCap
    );

    /* ==================== 7.8 DataDefinition ==================== */

    int cms_encode_DataDefinition(
        int choice, long intVal,
        String strVal, byte[] bytesVal, int bytesLen,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_DataDefinition(
        byte[] inBuf, int inLen,
        IntByReference choice, LongByReference intVal,
        byte[] strVal, IntByReference strCap,
        byte[] bytesVal, IntByReference bytesCap
    );

    /* ==================== 7.3.11 ServiceError ==================== */

    int cms_encode_ServiceError(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_ServiceError(
        byte[] inBuf, int inLen,
        IntByReference value
    );

    /* ==================== 7.5.4 AddCause ==================== */

    int cms_encode_AddCause(
        int value,
        byte[] outBuf, IntByReference outLen
    );

    int cms_decode_AddCause(
        byte[] inBuf, int inLen,
        IntByReference value
    );
}
