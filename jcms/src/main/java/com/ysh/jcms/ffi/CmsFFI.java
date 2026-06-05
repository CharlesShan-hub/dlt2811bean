package com.ysh.jcms.ffi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

/**
 * FFI 接口 — 所有 C 函数使用 struct 指针传参（CCMS 全 struct 化后）。
 */
public interface CmsFFI extends Library {

    CmsFFI INSTANCE = Native.load("ccms", CmsFFI.class);

    // ==================== BOOLEAN ====================
    int cms_boolean_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_boolean_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT8 (signed) ====================
    int cms_int8_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int8_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT8U (unsigned) ====================
    int cms_int8u_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int8u_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT16 (signed) ====================
    int cms_int16_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int16_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT16U (unsigned) ====================
    int cms_int16u_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int16u_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT32 (signed) ====================
    int cms_int32_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int32_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT32U (unsigned) ====================
    int cms_int32u_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int32u_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT64 (signed) ====================
    int cms_int64_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int64_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT64U (unsigned) ====================
    int cms_int64u_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int64u_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== INT24U (unsigned 24-bit) ====================
    int cms_int24u_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_int24u_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== FLOAT32 ====================
    int cms_float32_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_float32_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== FLOAT64 ====================
    int cms_float64_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_float64_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== EXTENDED — Time ====================
    int cms_time_quality_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_time_quality_decode(Structure v, byte[] inBuf, int inLen);

    int cms_utc_time_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_utc_time_decode(Structure v, byte[] inBuf, int inLen);

    int cms_binary_time_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_binary_time_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== COMMON — Coded Enum / Enumerated ====================
    int cms_dbpos_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_dbpos_decode(Structure v, byte[] inBuf, int inLen);

    int cms_tcmd_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_tcmd_decode(Structure v, byte[] inBuf, int inLen);

    int cms_service_error_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_service_error_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== COMMON — uint8_array aliases ====================
    int cms_entry_id_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_entry_id_decode(Structure v, byte[] inBuf, int inLen);

    int cms_object_name_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_object_name_decode(Structure v, byte[] inBuf, int inLen);

    int cms_object_reference_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_object_reference_decode(Structure v, byte[] inBuf, int inLen);

    int cms_sub_reference_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_sub_reference_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== COMMON — Time aliases ====================
    int cms_entry_time_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_entry_time_decode(Structure v, byte[] inBuf, int inLen);

    int cms_time_stamp_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_time_stamp_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== COMMON — Quality ====================
    int cms_quality_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_quality_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== COMMON — SEQUENCE ====================
    int cms_phy_com_addr_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_phy_com_addr_decode(Structure v, byte[] inBuf, int inLen);

    int cms_file_entry_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_file_entry_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== CONTROL ====================
    int cms_add_cause_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_add_cause_decode(Structure v, byte[] inBuf, int inLen);

    int cms_check_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_check_decode(Structure v, byte[] inBuf, int inLen);

    int cms_originator_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_originator_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== BLOCK — OptFlds / helpers ====================
    int cms_rcb_opt_flds_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_rcb_opt_flds_decode(Structure v, byte[] inBuf, int inLen);

    int cms_msvcb_opt_flds_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_msvcb_opt_flds_decode(Structure v, byte[] inBuf, int inLen);

    int cms_lcb_opt_flds_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_lcb_opt_flds_decode(Structure v, byte[] inBuf, int inLen);

    int cms_smp_mod_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_smp_mod_decode(Structure v, byte[] inBuf, int inLen);

    int cms_trigger_conditions_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_trigger_conditions_decode(Structure v, byte[] inBuf, int inLen);

    int cms_reason_code_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_reason_code_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== BLOCK — Control Blocks ====================
    int cms_brcb_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_brcb_decode(Structure v, byte[] inBuf, int inLen);

    int cms_urcb_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_urcb_decode(Structure v, byte[] inBuf, int inLen);

    int cms_lcb_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_lcb_decode(Structure v, byte[] inBuf, int inLen);

    int cms_gocb_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_gocb_decode(Structure v, byte[] inBuf, int inLen);

    int cms_sgcb_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_sgcb_decode(Structure v, byte[] inBuf, int inLen);

    int cms_msvcb_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_msvcb_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== FC ====================
    int cms_functional_constraint_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_functional_constraint_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== CHOICE ====================
    int cms_data_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_data_decode(Structure v, byte[] inBuf, int inLen);

    int cms_data_definition_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_data_definition_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== SVC — Connection ====================
    int cms_abort_reason_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_abort_reason_decode(Structure v, byte[] inBuf, int inLen);

    int cms_abort_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_abort_decode(Structure v, byte[] inBuf, int inLen);

    int cms_associate_request_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_associate_request_decode(Structure v, byte[] inBuf, int inLen);

    int cms_associate_response_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_associate_response_decode(Structure v, byte[] inBuf, int inLen);

    int cms_release_request_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_release_request_decode(Structure v, byte[] inBuf, int inLen);

    int cms_release_response_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_release_response_decode(Structure v, byte[] inBuf, int inLen);

    int cms_associate_error_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_associate_error_decode(Structure v, byte[] inBuf, int inLen);

    int cms_release_error_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_release_error_decode(Structure v, byte[] inBuf, int inLen);

    int cms_authentication_parameter_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_authentication_parameter_decode(Structure v, byte[] inBuf, int inLen);

    int cms_association_id_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_association_id_decode(Structure v, byte[] inBuf, int inLen);

    // ==================== TEST ====================
    int cms_test_pair_encode(Structure v, byte[] outBuf, IntByReference outLen);
    int cms_test_pair_decode(Structure v, byte[] inBuf, int inLen);
}
