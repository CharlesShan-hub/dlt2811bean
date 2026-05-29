#ifndef CMS_FFI_DATATYPES_H
#define CMS_FFI_DATATYPES_H

#include <stdint.h>
#include <stddef.h>

#ifdef _MSC_VER
  #define CMS_DT_EXPORT __declspec(dllexport)
#else
  #define CMS_DT_EXPORT __attribute__((visibility("default")))
#endif

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_DT_OK      0
#define CMS_DT_ERR    -1
#define CMS_DT_ERR_BUF_TOO_SMALL  -2

/* ==================== 7.1.1 BOOLEAN ==================== */
CMS_DT_EXPORT int cms_ffi_encode_BOOLEAN(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_BOOLEAN(
    const uint8_t *in_buf, int in_len,
    int *value
);

/* ==================== 7.1.2 Integer Types ==================== */
CMS_DT_EXPORT int cms_ffi_encode_Int8(
    int8_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int8(
    const uint8_t *in_buf, int in_len,
    int8_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int8U(
    uint8_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int8U(
    const uint8_t *in_buf, int in_len,
    uint8_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int16(
    int16_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int16(
    const uint8_t *in_buf, int in_len,
    int16_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int16U(
    uint16_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int16U(
    const uint8_t *in_buf, int in_len,
    uint16_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int32(
    int32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int32(
    const uint8_t *in_buf, int in_len,
    int32_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int32U(
    uint32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int32U(
    const uint8_t *in_buf, int in_len,
    uint32_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int64(
    int64_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int64(
    const uint8_t *in_buf, int in_len,
    int64_t *value
);

CMS_DT_EXPORT int cms_ffi_encode_Int64U(
    uint64_t value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Int64U(
    const uint8_t *in_buf, int in_len,
    uint64_t *value
);

/* ==================== 7.1.4 Floating-Point Types ==================== */
CMS_DT_EXPORT int cms_ffi_encode_Float32(
    float value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Float32(
    const uint8_t *in_buf, int in_len,
    float *value
);

CMS_DT_EXPORT int cms_ffi_encode_Float64(
    double value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Float64(
    const uint8_t *in_buf, int in_len,
    double *value
);

/* ==================== 7.1.5 String Types ==================== */
CMS_DT_EXPORT int cms_ffi_encode_VisibleString(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_VisibleString(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_DT_EXPORT int cms_ffi_encode_UTF8String(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_UTF8String(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_DT_EXPORT int cms_ffi_encode_OctetString(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_OctetString(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

/* ==================== 7.2.1 ObjectName ==================== */
CMS_DT_EXPORT int cms_ffi_encode_ObjectName(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_ObjectName(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

/* ==================== 7.2.2 ObjectReference ==================== */
CMS_DT_EXPORT int cms_ffi_encode_ObjectReference(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_ObjectReference(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

/* ==================== 7.2.3 FC ==================== */
CMS_DT_EXPORT int cms_ffi_encode_FC(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_FC(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

/* ==================== 7.2.4 SubReference ==================== */
CMS_DT_EXPORT int cms_ffi_encode_SubReference(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_SubReference(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

/* ==================== 7.2.5 EntryID ==================== */
CMS_DT_EXPORT int cms_ffi_encode_EntryID(
    const uint8_t value[8],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_EntryID(
    const uint8_t *in_buf, int in_len,
    uint8_t value[8]
);

/* ==================== 7.2.6 BitString ==================== */
CMS_DT_EXPORT int cms_ffi_encode_BitString(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_BitString(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

/* ==================== 7.3.1 BinaryTime ==================== */
CMS_DT_EXPORT int cms_ffi_encode_BinaryTime(
    int32_t hour, int32_t minute, int32_t second, int32_t millisecond,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_BinaryTime(
    const uint8_t *in_buf, int in_len,
    int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond
);

/* ==================== 7.3.2 UtcTime ==================== */
CMS_DT_EXPORT int cms_ffi_encode_UtcTime(
    int64_t timestamp_ms,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_UtcTime(
    const uint8_t *in_buf, int in_len,
    int64_t *timestamp_ms
);

/* ==================== 7.3.3 TimeStamp ==================== */
CMS_DT_EXPORT int cms_ffi_encode_TimeStamp(
    int64_t seconds_since_epoch, int64_t fractional,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_TimeStamp(
    const uint8_t *in_buf, int in_len,
    int64_t *seconds_since_epoch, int64_t *fractional
);

/* ==================== 7.4 Originator ==================== */
CMS_DT_EXPORT int cms_ffi_encode_Originator(
    int or_cat,
    const uint8_t *or_ident, int or_ident_len,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Originator(
    const uint8_t *in_buf, int in_len,
    int *or_cat,
    uint8_t *or_ident, int *or_ident_cap
);

/* ==================== 7.5 PhyComAddr ==================== */
CMS_DT_EXPORT int cms_ffi_encode_PhyComAddr(
    const uint8_t value[6],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_PhyComAddr(
    const uint8_t *in_buf, int in_len,
    uint8_t value[6]
);

/* ==================== 7.6 CODED ENUM types ==================== */
/* Quality (BIT STRING SIZE(13)) */
CMS_DT_EXPORT int cms_ffi_encode_Quality(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Quality(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

/* Dbpos (ENUMERATED) */
CMS_DT_EXPORT int cms_ffi_encode_Dbpos(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Dbpos(
    const uint8_t *in_buf, int in_len,
    int *value
);

/* Tcmd (ENUMERATED) */
CMS_DT_EXPORT int cms_ffi_encode_Tcmd(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Tcmd(
    const uint8_t *in_buf, int in_len,
    int *value
);

/* Check (BIT STRING SIZE(16)) */
CMS_DT_EXPORT int cms_ffi_encode_Check(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Check(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

/* LcbOptFlds (BIT STRING SIZE(6)) */
CMS_DT_EXPORT int cms_ffi_encode_LcbOptFlds(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_LcbOptFlds(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

/* MsvcbOptFlds (BIT STRING SIZE(8)) */
CMS_DT_EXPORT int cms_ffi_encode_MsvcbOptFlds(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_MsvcbOptFlds(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

/* RcbOptFlds (BIT STRING SIZE(10)) */
CMS_DT_EXPORT int cms_ffi_encode_RcbOptFlds(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_RcbOptFlds(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

/* ReasonCode (BIT STRING SIZE(6)) */
CMS_DT_EXPORT int cms_ffi_encode_ReasonCode(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_ReasonCode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

/* TimeQuality (BIT STRING SIZE(8)) */
CMS_DT_EXPORT int cms_ffi_encode_TimeQuality(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_TimeQuality(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

/* TriggerConditions (BIT STRING SIZE(6)) */
CMS_DT_EXPORT int cms_ffi_encode_TriggerConditions(
    const uint8_t value[1],
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_TriggerConditions(
    const uint8_t *in_buf, int in_len,
    uint8_t value[1]
);

/* PackedList (BIT STRING SIZE(0..65535)) */
CMS_DT_EXPORT int cms_ffi_encode_PackedList(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_PackedList(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

/* ==================== 7.7 Data ==================== */
/* choice: 0=serviceError, 1=array, 2=structure, 3=boolean,
   4=int8, 5=int16, 6=int32, 7=int64, 8=int8u, 9=int16u, 10=int32u, 11=int64u,
   12=float32, 13=float64, 14=bitString, 15=octetString, 16=visibleString,
   17=utf8String, 18=utcTime, 19=binaryTime, 20=quality, 21=dbpos, 22=tcmd, 23=check */
CMS_DT_EXPORT int cms_ffi_encode_Data(
    int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_Data(
    const uint8_t *in_buf, int in_len,
    int *choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap
);

/* ==================== 7.8 DataDefinition ==================== */
CMS_DT_EXPORT int cms_ffi_encode_DataDefinition(
    const char *data_name, const char *data_type,
    const uint8_t fc[2],
    int data_choice, int64_t data_int, double data_float,
    const char *data_str, const uint8_t *data_bytes, int data_bytes_len,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_DataDefinition(
    const uint8_t *in_buf, int in_len,
    char *data_name, int *data_name_cap,
    char *data_type, int *data_type_cap,
    uint8_t fc[2],
    int *data_choice, int64_t *data_int, double *data_float,
    char *data_str, int *data_str_cap,
    uint8_t *data_bytes, int *data_bytes_cap
);

/* ==================== 7.9 ServiceError ==================== */
CMS_DT_EXPORT int cms_ffi_encode_ServiceError(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_ServiceError(
    const uint8_t *in_buf, int in_len,
    int *value
);

/* ==================== 7.10 AddCause / OrCat / SmpMod ==================== */
CMS_DT_EXPORT int cms_ffi_encode_AddCause(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_AddCause(
    const uint8_t *in_buf, int in_len,
    int *value
);

CMS_DT_EXPORT int cms_ffi_encode_OrCat(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_OrCat(
    const uint8_t *in_buf, int in_len,
    int *value
);

CMS_DT_EXPORT int cms_ffi_encode_SmpMod(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_DT_EXPORT int cms_ffi_decode_SmpMod(
    const uint8_t *in_buf, int in_len,
    int *value
);

#ifdef __cplusplus
}
#endif

#endif /* CMS_FFI_DATATYPES_H */