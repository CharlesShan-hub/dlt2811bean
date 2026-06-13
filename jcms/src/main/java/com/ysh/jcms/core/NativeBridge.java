package com.ysh.jcms.core;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * jcms FFI — ccms dll bridge
 */
public class NativeBridge {

    private interface Lib extends Library {
        int cms_boolean_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_boolean_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int8_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int8u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int8u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int16_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int16_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int16u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int16u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int24u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int24u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int32_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int32_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int32u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int32u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int64_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int64_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_int64u_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_int64u_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_float32_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_float32_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_float64_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_float64_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_enumerated_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_enumerated_decode(Pointer v, byte[] inBuf, int inLen);

        // string / alias types
        int cms_object_name_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_object_name_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_object_reference_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_object_reference_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_sub_reference_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_sub_reference_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_entry_id_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_entry_id_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_functional_constraint_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_functional_constraint_decode(Pointer v, byte[] inBuf, int inLen);

        // time
        int cms_time_quality_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_time_quality_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_utc_time_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_utc_time_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_binary_time_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_binary_time_decode(Pointer v, byte[] inBuf, int inLen);

        // common containers
        int cms_quality_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_quality_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_phy_com_addr_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_phy_com_addr_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_file_entry_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_file_entry_decode(Pointer v, byte[] inBuf, int inLen);

        // control
        int cms_originator_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_originator_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_check_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_check_decode(Pointer v, byte[] inBuf, int inLen);

        // block BIT STRING containers
        int cms_trigger_conditions_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_trigger_conditions_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_reason_code_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_reason_code_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_rcb_opt_flds_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_rcb_opt_flds_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_lcb_opt_flds_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_lcb_opt_flds_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_msvcb_opt_flds_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_msvcb_opt_flds_decode(Pointer v, byte[] inBuf, int inLen);

        // block SEQUENCE containers
        int cms_sgcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_sgcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_brcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_brcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_urcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_urcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_lcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_lcb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_go_cb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_go_cb_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_msvcb_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_msvcb_decode(Pointer v, byte[] inBuf, int inLen);

        // choice types
        int cms_data_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_data_decode(Pointer v, byte[] inBuf, int inLen);
        int cms_data_definition_encode(Pointer v, byte[] outBuf, IntByReference outLen);
        int cms_data_definition_decode(Pointer v, byte[] inBuf, int inLen);
    }

    private static final Lib LIB = Native.load("ccms", Lib.class);

    private static byte[] encode(Pointer structPtr, Encoder fn) {
        byte[] buf = new byte[256];
        IntByReference outLen = new IntByReference(buf.length);
        int rc = fn.encode(structPtr, buf, outLen);
        if (rc != 0) throw new RuntimeException("encode failed: " + rc);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    private static void decode(Pointer structPtr, byte[] data, Decoder fn) {
        int rc = fn.decode(structPtr, data, data.length);
        if (rc != 0) throw new RuntimeException("decode failed: " + rc);
    }

    @FunctionalInterface private interface Encoder { int encode(Pointer v, byte[] buf, IntByReference outLen); }
    @FunctionalInterface private interface Decoder { int decode(Pointer v, byte[] buf, int len); }

    public static byte[] encodeBoolean(Pointer p) { return encode(p, LIB::cms_boolean_encode); }
    public static void decodeBoolean(Pointer p, byte[] d) { decode(p, d, LIB::cms_boolean_decode); }
    public static byte[] encodeInt8(Pointer p) { return encode(p, LIB::cms_int8_encode); }
    public static void decodeInt8(Pointer p, byte[] d) { decode(p, d, LIB::cms_int8_decode); }
    public static byte[] encodeInt8U(Pointer p) { return encode(p, LIB::cms_int8u_encode); }
    public static void decodeInt8U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int8u_decode); }
    public static byte[] encodeInt16(Pointer p) { return encode(p, LIB::cms_int16_encode); }
    public static void decodeInt16(Pointer p, byte[] d) { decode(p, d, LIB::cms_int16_decode); }
    public static byte[] encodeInt16U(Pointer p) { return encode(p, LIB::cms_int16u_encode); }
    public static void decodeInt16U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int16u_decode); }
    public static byte[] encodeInt24U(Pointer p) { return encode(p, LIB::cms_int24u_encode); }
    public static void decodeInt24U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int24u_decode); }
    public static byte[] encodeInt32(Pointer p) { return encode(p, LIB::cms_int32_encode); }
    public static void decodeInt32(Pointer p, byte[] d) { decode(p, d, LIB::cms_int32_decode); }
    public static byte[] encodeInt32U(Pointer p) { return encode(p, LIB::cms_int32u_encode); }
    public static void decodeInt32U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int32u_decode); }
    public static byte[] encodeInt64(Pointer p) { return encode(p, LIB::cms_int64_encode); }
    public static void decodeInt64(Pointer p, byte[] d) { decode(p, d, LIB::cms_int64_decode); }
    public static byte[] encodeInt64U(Pointer p) { return encode(p, LIB::cms_int64u_encode); }
    public static void decodeInt64U(Pointer p, byte[] d) { decode(p, d, LIB::cms_int64u_decode); }
    public static byte[] encodeFloat32(Pointer p) { return encode(p, LIB::cms_float32_encode); }
    public static void decodeFloat32(Pointer p, byte[] d) { decode(p, d, LIB::cms_float32_decode); }
    public static byte[] encodeFloat64(Pointer p) { return encode(p, LIB::cms_float64_encode); }
    public static void decodeFloat64(Pointer p, byte[] d) { decode(p, d, LIB::cms_float64_decode); }
    public static byte[] encodeEnumerated(Pointer p) { return encode(p, LIB::cms_enumerated_encode); }
    public static void decodeEnumerated(Pointer p, byte[] d) { decode(p, d, LIB::cms_enumerated_decode); }

    // string / alias types
    public static byte[] encodeObjectName(Pointer p) { return encode(p, LIB::cms_object_name_encode); }
    public static void decodeObjectName(Pointer p, byte[] d) { decode(p, d, LIB::cms_object_name_decode); }
    public static byte[] encodeObjectReference(Pointer p) { return encode(p, LIB::cms_object_reference_encode); }
    public static void decodeObjectReference(Pointer p, byte[] d) { decode(p, d, LIB::cms_object_reference_decode); }
    public static byte[] encodeSubReference(Pointer p) { return encode(p, LIB::cms_sub_reference_encode); }
    public static void decodeSubReference(Pointer p, byte[] d) { decode(p, d, LIB::cms_sub_reference_decode); }
    public static byte[] encodeEntryId(Pointer p) { return encode(p, LIB::cms_entry_id_encode); }
    public static void decodeEntryId(Pointer p, byte[] d) { decode(p, d, LIB::cms_entry_id_decode); }
    public static byte[] encodeFunctionalConstraint(Pointer p) { return encode(p, LIB::cms_functional_constraint_encode); }
    public static void decodeFunctionalConstraint(Pointer p, byte[] d) { decode(p, d, LIB::cms_functional_constraint_decode); }

    // time
    public static byte[] encodeTimeQuality(Pointer p) { return encode(p, LIB::cms_time_quality_encode); }
    public static void decodeTimeQuality(Pointer p, byte[] d) { decode(p, d, LIB::cms_time_quality_decode); }
    public static byte[] encodeUtcTime(Pointer p) { return encode(p, LIB::cms_utc_time_encode); }
    public static void decodeUtcTime(Pointer p, byte[] d) { decode(p, d, LIB::cms_utc_time_decode); }
    public static byte[] encodeBinaryTime(Pointer p) { return encode(p, LIB::cms_binary_time_encode); }
    public static void decodeBinaryTime(Pointer p, byte[] d) { decode(p, d, LIB::cms_binary_time_decode); }

    // common containers
    public static byte[] encodeQuality(Pointer p) { return encode(p, LIB::cms_quality_encode); }
    public static void decodeQuality(Pointer p, byte[] d) { decode(p, d, LIB::cms_quality_decode); }
    public static byte[] encodePhyComAddr(Pointer p) { return encode(p, LIB::cms_phy_com_addr_encode); }
    public static void decodePhyComAddr(Pointer p, byte[] d) { decode(p, d, LIB::cms_phy_com_addr_decode); }
    public static byte[] encodeFileEntry(Pointer p) { return encode(p, LIB::cms_file_entry_encode); }
    public static void decodeFileEntry(Pointer p, byte[] d) { decode(p, d, LIB::cms_file_entry_decode); }

    // control
    public static byte[] encodeOriginator(Pointer p) { return encode(p, LIB::cms_originator_encode); }
    public static void decodeOriginator(Pointer p, byte[] d) { decode(p, d, LIB::cms_originator_decode); }
    public static byte[] encodeCheck(Pointer p) { return encode(p, LIB::cms_check_encode); }
    public static void decodeCheck(Pointer p, byte[] d) { decode(p, d, LIB::cms_check_decode); }

    // block BIT STRING containers
    public static byte[] encodeTriggerConditions(Pointer p) { return encode(p, LIB::cms_trigger_conditions_encode); }
    public static void decodeTriggerConditions(Pointer p, byte[] d) { decode(p, d, LIB::cms_trigger_conditions_decode); }
    public static byte[] encodeReasonCode(Pointer p) { return encode(p, LIB::cms_reason_code_encode); }
    public static void decodeReasonCode(Pointer p, byte[] d) { decode(p, d, LIB::cms_reason_code_decode); }
    public static byte[] encodeRcbOptFlds(Pointer p) { return encode(p, LIB::cms_rcb_opt_flds_encode); }
    public static void decodeRcbOptFlds(Pointer p, byte[] d) { decode(p, d, LIB::cms_rcb_opt_flds_decode); }
    public static byte[] encodeLcbOptFlds(Pointer p) { return encode(p, LIB::cms_lcb_opt_flds_encode); }
    public static void decodeLcbOptFlds(Pointer p, byte[] d) { decode(p, d, LIB::cms_lcb_opt_flds_decode); }
    public static byte[] encodeMsvcbOptFlds(Pointer p) { return encode(p, LIB::cms_msvcb_opt_flds_encode); }
    public static void decodeMsvcbOptFlds(Pointer p, byte[] d) { decode(p, d, LIB::cms_msvcb_opt_flds_decode); }

    // block SEQUENCE containers
    public static byte[] encodeSgcb(Pointer p) { return encode(p, LIB::cms_sgcb_encode); }
    public static void decodeSgcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_sgcb_decode); }
    public static byte[] encodeBrcb(Pointer p) { return encode(p, LIB::cms_brcb_encode); }
    public static void decodeBrcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_brcb_decode); }
    public static byte[] encodeUrcb(Pointer p) { return encode(p, LIB::cms_urcb_encode); }
    public static void decodeUrcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_urcb_decode); }
    public static byte[] encodeLcb(Pointer p) { return encode(p, LIB::cms_lcb_encode); }
    public static void decodeLcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_lcb_decode); }
    public static byte[] encodeGoCb(Pointer p) { return encode(p, LIB::cms_go_cb_encode); }
    public static void decodeGoCb(Pointer p, byte[] d) { decode(p, d, LIB::cms_go_cb_decode); }
    public static byte[] encodeMsvcb(Pointer p) { return encode(p, LIB::cms_msvcb_encode); }
    public static void decodeMsvcb(Pointer p, byte[] d) { decode(p, d, LIB::cms_msvcb_decode); }

    // choice types
    public static byte[] encodeData(Pointer p) { return encode(p, LIB::cms_data_encode); }
    public static void decodeData(Pointer p, byte[] d) { decode(p, d, LIB::cms_data_decode); }
    public static byte[] encodeDataDefinition(Pointer p) { return encode(p, LIB::cms_data_definition_encode); }
    public static void decodeDataDefinition(Pointer p, byte[] d) { decode(p, d, LIB::cms_data_definition_decode); }
}
