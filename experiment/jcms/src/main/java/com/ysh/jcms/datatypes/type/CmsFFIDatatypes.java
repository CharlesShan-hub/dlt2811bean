package com.ysh.jcms.datatypes.type;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public interface CmsFFIDatatypes extends Library {

    CmsFFIDatatypes INSTANCE = Native.load("ccms", CmsFFIDatatypes.class);

    /* ======================= §7.1.1 BOOLEAN ======================== */

    int cms_boolean_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_boolean_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ================== §7.1.2~7.1.3 Integer Types ================== */

    int cms_int8_encode(byte value, byte[] outBuf, IntByReference outLen);
    int cms_int8_decode(byte[] inBuf, int inLen, ByteByReference value);

    int cms_int8u_encode(short value, byte[] outBuf, IntByReference outLen);
    int cms_int8u_decode(byte[] inBuf, int inLen, IntByReference value);

    int cms_int16_encode(short value, byte[] outBuf, IntByReference outLen);
    int cms_int16_decode(byte[] inBuf, int inLen, IntByReference value);

    int cms_int16u_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_int16u_decode(byte[] inBuf, int inLen, IntByReference value);

    int cms_int24u_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_int24u_decode(byte[] inBuf, int inLen, IntByReference value);

    int cms_int32_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_int32_decode(byte[] inBuf, int inLen, IntByReference value);

    int cms_int32u_encode(long value, byte[] outBuf, IntByReference outLen);
    int cms_int32u_decode(byte[] inBuf, int inLen, LongByReference value);

    int cms_int64_encode(long value, byte[] outBuf, IntByReference outLen);
    int cms_int64_decode(byte[] inBuf, int inLen, LongByReference value);

    int cms_int64u_encode(long value, byte[] outBuf, IntByReference outLen);
    int cms_int64u_decode(byte[] inBuf, int inLen, LongByReference value);

    /* ==================== §7.1.4 Floating-Point Types ==================== */

    int cms_float32_encode(float value, byte[] outBuf, IntByReference outLen);
    int cms_float32_decode(byte[] inBuf, int inLen, float[] value);

    int cms_float64_encode(double value, byte[] outBuf, IntByReference outLen);
    int cms_float64_decode(byte[] inBuf, int inLen, double[] value);

    /* ==================== §7.1.5 String Types ==================== */

    int cms_visible_string_encode(String value, int maxLen, byte[] outBuf, IntByReference outLen);
    int cms_visible_string_decode(byte[] inBuf, int inLen, int maxLen, byte[] value, IntByReference valueCap);

    int cms_utf8_string_encode(byte[] value, int maxLen, byte[] outBuf, IntByReference outLen);
    int cms_utf8_string_decode(byte[] inBuf, int inLen, int maxLen, byte[] value, IntByReference valueCap);

    int cms_octet_string_encode(byte[] value, int valueLen, int maxLen, byte[] outBuf, IntByReference outLen);
    int cms_octet_string_decode(byte[] inBuf, int inLen, int maxLen, byte[] value, IntByReference valueCap);

    int cms_bit_string_encode(byte[] value, int valueLen, int maxLen, byte[] outBuf, IntByReference outLen);
    int cms_bit_string_decode(byte[] inBuf, int inLen, int maxLen, byte[] value, IntByReference valueCap);

    /* ==================== §7.1.8 BitString / PackedList ==================== */

    int cms_packed_list_encode(byte[] value, int valueLen, int maxLen, byte[] outBuf, IntByReference outLen);
    int cms_packed_list_decode(byte[] inBuf, int inLen, int maxLen, byte[] value, IntByReference valueCap);

    /* ==================== §7.2.1 UtcTime ==================== */

    int cms_utc_time_encode(Structure t, byte[] outBuf, IntByReference outLen);
    int cms_utc_time_decode(byte[] inBuf, int inLen, Structure t);

    /* ==================== §7.2.2 BinaryTime ==================== */

    int cms_binary_time_encode(int msOfDay, int daysSince1984, byte[] outBuf, IntByReference outLen);
    int cms_binary_time_decode(byte[] inBuf, int inLen, IntByReference msOfDay, IntByReference daysSince1984);

    /* ==================== §7.2.3 TimeQuality ==================== */

    int cms_time_quality_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_time_quality_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.3.1 ObjectName ==================== */

    int cms_object_name_encode(String value, byte[] outBuf, IntByReference outLen);
    int cms_object_name_decode(byte[] inBuf, int inLen, byte[] value, IntByReference valueCap);

    /* ==================== §7.3.2 ObjectReference ==================== */

    int cms_object_reference_encode(String value, byte[] outBuf, IntByReference outLen);
    int cms_object_reference_decode(byte[] inBuf, int inLen, byte[] value, IntByReference valueCap);

    /* ==================== §7.3.3 SubReference ==================== */

    int cms_sub_reference_encode(String value, byte[] outBuf, IntByReference outLen);
    int cms_sub_reference_decode(byte[] inBuf, int inLen, byte[] value, IntByReference valueCap);

    /* ==================== §7.3.4 TimeStamp ==================== */

    int cms_time_stamp_encode(Structure t, byte[] outBuf, IntByReference outLen);
    int cms_time_stamp_decode(byte[] inBuf, int inLen, Structure t);

    /* ==================== §7.3.5 Dbpos ==================== */

    int cms_dbpos_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_dbpos_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== §7.3.6 Quality ==================== */

    int cms_quality_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_quality_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.3.7 Tcmd ==================== */

    int cms_tcmd_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_tcmd_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== §7.3.8 EntryID ==================== */

    int cms_entry_id_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_entry_id_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.3.11 ServiceError ==================== */

    int cms_service_error_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_service_error_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== §7.3.12 PhyComAddr ==================== */

    int cms_phy_com_addr_encode(byte[] addr, int priority, int vid, int appid, byte[] outBuf, IntByReference outLen);
    int cms_phy_com_addr_decode(byte[] inBuf, int inLen, byte[] addr, IntByReference priority, IntByReference vid, IntByReference appid);

    /* ==================== §7.4 FC ==================== */

    int cms_fc_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_fc_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.5.2 Originator ==================== */

    int cms_originator_encode(int orCat, byte[] orIdent, int orIdentLen, byte[] outBuf, IntByReference outLen);
    int cms_originator_decode(byte[] inBuf, int inLen, IntByReference orCat, byte[] orIdent, IntByReference orIdentCap);

    /* ==================== §7.5.3 Check ==================== */

    int cms_check_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_check_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.5.4 AddCause ==================== */

    int cms_add_cause_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_add_cause_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== §7.6.2 TriggerConditions ==================== */

    int cms_trigger_conditions_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_trigger_conditions_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.6.3 ReasonCode ==================== */

    int cms_reason_code_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_reason_code_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.6.4 RcbOptFlds ==================== */

    int cms_rcb_opt_flds_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_rcb_opt_flds_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.6.5 LcbOptFlds ==================== */

    int cms_lcb_opt_flds_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_lcb_opt_flds_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.6.6 MsvcbOptFlds ==================== */

    int cms_msvcb_opt_flds_encode(byte[] value, byte[] outBuf, IntByReference outLen);
    int cms_msvcb_opt_flds_decode(byte[] inBuf, int inLen, byte[] value);

    /* ==================== §7.6.7 SmpMod ==================== */

    int cms_smp_mod_encode(int value, byte[] outBuf, IntByReference outLen);
    int cms_smp_mod_decode(byte[] inBuf, int inLen, IntByReference value);

    /* ==================== §7.7 Data ==================== */

    int cms_data_encode(int choice, long intVal, double floatVal, String strVal, byte[] bytesVal, int bytesLen, byte[] outBuf, IntByReference outLen);
    int cms_data_decode(byte[] inBuf, int inLen, IntByReference choice, LongByReference intVal, double[] floatVal, byte[] strVal, IntByReference strCap, byte[] bytesVal, IntByReference bytesCap);

    /* ==================== §7.8 DataDefinition ==================== */

    int cms_data_definition_encode(int choice, long intVal, String strVal, byte[] bytesVal, int bytesLen, byte[] outBuf, IntByReference outLen);
    int cms_data_definition_decode(byte[] inBuf, int inLen, IntByReference choice, LongByReference intVal, byte[] strVal, IntByReference strCap, byte[] bytesVal, IntByReference bytesCap);

    /* ==================== Control Blocks ==================== */

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
}
