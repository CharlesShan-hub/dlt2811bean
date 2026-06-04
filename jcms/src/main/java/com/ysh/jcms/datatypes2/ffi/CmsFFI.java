package com.ysh.jcms.datatypes2.ffi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 * FFI 接口 — data 层的基本类型编码/解码函数。
 * 所有 C 函数来自 ccms DLL。
 */
public interface CmsFFI extends Library {

    CmsFFI INSTANCE = Native.load("ccms", CmsFFI.class);

    // ==================== BOOLEAN ====================
    int cms_boolean_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_boolean_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT8 (signed) ====================
    int cms_int8_encode(byte value, byte[] outBuf, IntByReference outLen);
    int cms_int8_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT8U (unsigned) ====================
    int cms_int8u_encode(byte value, byte[] outBuf, IntByReference outLen);
    int cms_int8u_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT16 (signed) ====================
    int cms_int16_encode(short value, byte[] outBuf, IntByReference outLen);
    int cms_int16_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT16U (unsigned) ====================
    int cms_int16u_encode(short value, byte[] outBuf, IntByReference outLen);
    int cms_int16u_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT32 (signed) ====================
    int cms_int32_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_int32_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT32U (unsigned) ====================
    int cms_int32u_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_int32u_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== INT64 (signed) ====================
    int cms_int64_encode(long value, byte[] outBuf, IntByReference outLen);
    int cms_int64_decode(byte[] inBuf, int inLen, LongByReference value);

    // ==================== INT64U (unsigned) ====================
    int cms_int64u_encode(long value, byte[] outBuf, IntByReference outLen);
    int cms_int64u_decode(byte[] inBuf, int inLen, LongByReference value);

    // ==================== FLOAT32 ====================
    int cms_float32_encode(float value, byte[] outBuf, IntByReference outLen);
    int cms_float32_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== FLOAT64 ====================
    int cms_float64_encode(double value, byte[] outBuf, IntByReference outLen);
    int cms_float64_decode(byte[] inBuf, int inLen, IntByReference value);

    // ==================== String types (struct-based) ====================

    int cms_visible_string_fixed_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_visible_string_fixed_decode(byte[] inBuf, int inLen, Structure v);
    int cms_visible_string_var_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_visible_string_var_decode(byte[] inBuf, int inLen, Structure v);

    int cms_utf8_string_fixed_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_utf8_string_fixed_decode(byte[] inBuf, int inLen, Structure v);
    int cms_utf8_string_var_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_utf8_string_var_decode(byte[] inBuf, int inLen, Structure v);

    int cms_octet_string_fixed_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_octet_string_fixed_decode(byte[] inBuf, int inLen, Structure v);
    int cms_octet_string_var_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_octet_string_var_decode(byte[] inBuf, int inLen, Structure v);

    int cms_bit_string_fixed_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_bit_string_fixed_decode(byte[] inBuf, int inLen, Structure v);
    int cms_bit_string_var_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_bit_string_var_decode(byte[] inBuf, int inLen, Structure v);

    // ==================== Structure-based compound types ====================

    int cms_binary_time_encode(Structure t, byte[] outBuf, IntByReference outLen);
    int cms_binary_time_decode(byte[] inBuf, int inLen, Structure t);

    int cms_utc_time_encode(Structure t, byte[] outBuf, IntByReference outLen);
    int cms_utc_time_decode(byte[] inBuf, int inLen, Structure t);

    int cms_entry_time_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_entry_time_decode(byte[] inBuf, int inLen, Structure value);

    int cms_time_stamp_encode(Structure t, byte[] outBuf, IntByReference outLen);
    int cms_time_stamp_decode(byte[] inBuf, int inLen, Structure t);

    int cms_association_id_encode(Structure id, byte[] outBuf, IntByReference outLen);
    int cms_association_id_decode(byte[] inBuf, int inLen, Structure id);

    int cms_trigger_conditions_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_trigger_conditions_decode(byte[] inBuf, int inLen, byte[] value);

    int cms_quality_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_quality_decode(byte[] inBuf, int inLen, byte[] value);

    int cms_check_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_check_decode(byte[] inBuf, int inLen, byte[] value);

    int cms_authentication_parameter_encode(Structure sdu, byte[] outBuf, IntByReference outLen);
    int cms_authentication_parameter_decode(byte[] inBuf, int inLen, Structure sdu);

    // ==================== Block/Control types (Structure-based) ====================

    int cms_brcb_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_brcb_decode(byte[] inBuf, int inLen, Structure value);
    int cms_gocb_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_gocb_decode(byte[] inBuf, int inLen, Structure value);
    int cms_lcb_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_lcb_decode(byte[] inBuf, int inLen, Structure value);
    int cms_msvcb_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_msvcb_decode(byte[] inBuf, int inLen, Structure value);
    int cms_sgcb_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_sgcb_decode(byte[] inBuf, int inLen, Structure value);
    int cms_urcb_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_urcb_decode(byte[] inBuf, int inLen, Structure value);
    int cms_data_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_data_decode(byte[] inBuf, int inLen, Structure value);
    int cms_data_definition_encode(Structure value, byte[] outBuf, IntByReference outLen);
    int cms_data_definition_decode(byte[] inBuf, int inLen, Structure value);

    // ==================== SVC / Connection types ====================

    int cms_abort_encode(Structure sdu, byte[] outBuf, IntByReference outLen);
    int cms_abort_decode(byte[] inBuf, int inLen, Structure sdu);
    int cms_associate_request_encode(Structure sdu, byte[] outBuf, IntByReference outLen);
    int cms_associate_request_decode(byte[] inBuf, int inLen, Structure sdu);
    int cms_associate_response_encode(Structure sdu, byte[] outBuf, IntByReference outLen);
    int cms_associate_response_decode(byte[] inBuf, int inLen, Structure sdu);
    int cms_release_request_encode(Structure sdu, byte[] outBuf, IntByReference outLen);
    int cms_release_request_decode(byte[] inBuf, int inLen, Structure sdu);
    int cms_release_response_encode(Structure sdu, byte[] outBuf, IntByReference outLen);
    int cms_release_response_decode(byte[] inBuf, int inLen, Structure sdu);
}
