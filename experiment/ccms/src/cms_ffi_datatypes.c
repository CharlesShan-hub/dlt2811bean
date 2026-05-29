#include "ccms/cms_ffi_datatypes.h"
#include "ccms/cmsper.h"
#include <string.h>
#include <stdlib.h>

/* forward declarations for Data value encode/decode helpers */
static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len);
static void decode_data_value(per_stream_t *r, int choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap);

/* ==================== 7.1.1 BOOLEAN ==================== */

int cms_ffi_encode_BOOLEAN(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_boolean(&w, value ? 1 : 0);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_BOOLEAN(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    bool b;
    per_decode_boolean(&r, &b);
    *value = b ? 1 : 0;
    return CMS_DT_OK;
}

/* ==================== 7.1.2 Integer Types ==================== */

int cms_ffi_encode_Int8(int8_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, -128, 127);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int8(const uint8_t *in_buf, int in_len, int8_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, -128, 127);
    *value = (int8_t)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Int8U(uint8_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 255);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int8U(const uint8_t *in_buf, int in_len, uint8_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 255);
    *value = (uint8_t)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Int16(int16_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, -32768, 32767);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int16(const uint8_t *in_buf, int in_len, int16_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, -32768, 32767);
    *value = (int16_t)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Int16U(uint16_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int16U(const uint8_t *in_buf, int in_len, uint16_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *value = (uint16_t)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Int32(int32_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, -2147483648, 2147483647);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int32(const uint8_t *in_buf, int in_len, int32_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, -2147483648, 2147483647);
    *value = (int32_t)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Int32U(uint32_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 4294967295);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int32U(const uint8_t *in_buf, int in_len, uint32_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 4294967295);
    *value = (uint32_t)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Int64(int64_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_unconstrained_int(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int64(const uint8_t *in_buf, int in_len, int64_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_unconstrained_int(&r, value);
    return CMS_DT_OK;
}

int cms_ffi_encode_Int64U(uint64_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_unconstrained_int(&w, (int64_t)value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Int64U(const uint8_t *in_buf, int in_len, uint64_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_unconstrained_int(&r, &tmp);
    *value = (uint64_t)tmp;
    return CMS_DT_OK;
}

/* ==================== 7.1.4 Floating-Point Types ==================== */
/* Float32/Float64 are typedef double in ASN.1, no standalone encode/decode.
   They are encoded as part of SEQUENCE/CHOICE via gen_cms functions. */

int cms_ffi_encode_Float32(float value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    uint32_t bits;
    memcpy(&bits, &value, sizeof(bits));
    uint8_t bytes[4];
    bytes[0] = (uint8_t)(bits >> 24);
    bytes[1] = (uint8_t)(bits >> 16);
    bytes[2] = (uint8_t)(bits >> 8);
    bytes[3] = (uint8_t)(bits);
    per_encode_octet_string_fixed(&w, bytes, 4);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Float32(const uint8_t *in_buf, int in_len, float *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint8_t bytes[4];
    per_decode_octet_string_fixed(&r, bytes, 4);
    uint32_t bits = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                  | ((uint32_t)bytes[2] << 8) | (uint32_t)bytes[3];
    memcpy(value, &bits, sizeof(bits));
    return CMS_DT_OK;
}

int cms_ffi_encode_Float64(double value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    uint64_t bits;
    memcpy(&bits, &value, sizeof(bits));
    uint8_t bytes[8];
    bytes[0] = (uint8_t)(bits >> 56);
    bytes[1] = (uint8_t)(bits >> 48);
    bytes[2] = (uint8_t)(bits >> 40);
    bytes[3] = (uint8_t)(bits >> 32);
    bytes[4] = (uint8_t)(bits >> 24);
    bytes[5] = (uint8_t)(bits >> 16);
    bytes[6] = (uint8_t)(bits >> 8);
    bytes[7] = (uint8_t)(bits);
    per_encode_octet_string_fixed(&w, bytes, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Float64(const uint8_t *in_buf, int in_len, double *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint8_t bytes[8];
    per_decode_octet_string_fixed(&r, bytes, 8);
    uint64_t bits = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                  | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                  | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                  | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
    memcpy(value, &bits, sizeof(bits));
    return CMS_DT_OK;
}

/* ==================== 7.1.5 String Types ==================== */

int cms_ffi_encode_VisibleString(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 255);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_VisibleString(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_DT_OK;
}

int cms_ffi_encode_UTF8String(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_utf8_string(&w, value, 255);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_UTF8String(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_utf8_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_DT_OK;
}

int cms_ffi_encode_OctetString(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string(&w, value, value_len, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_OctetString(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    size_t out_len = (size_t)*value_cap;
    per_decode_octet_string(&r, value, &out_len, 65535);
    *value_cap = (int)out_len;
    return CMS_DT_OK;
}

/* ==================== 7.3.1 ObjectName ==================== */

int cms_ffi_encode_ObjectName(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 64);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_ObjectName(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_DT_OK;
}

/* ==================== 7.3.2 ObjectReference ==================== */

int cms_ffi_encode_ObjectReference(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 129);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_ObjectReference(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_DT_OK;
}

/* ==================== 7.4 FC ==================== */

int cms_ffi_encode_FC(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 16);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_FC(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 16);
    return CMS_DT_OK;
}

/* ==================== 7.3.3 SubReference ==================== */

int cms_ffi_encode_SubReference(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 129);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_SubReference(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_DT_OK;
}

/* ==================== 7.3.8 EntryID ==================== */

int cms_ffi_encode_EntryID(const uint8_t value[8], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, value, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_EntryID(const uint8_t *in_buf, int in_len, uint8_t value[8])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, value, 8);
    return CMS_DT_OK;
}

/* ==================== 7.1.8 BitString / PackedList ==================== */

int cms_ffi_encode_BitString(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string(&w, value, value_len * 8, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_BitString(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int out_nbits = *value_cap * 8;
    per_decode_bit_string(&r, value, &out_nbits, 65535);
    *value_cap = (out_nbits + 7) / 8;
    return CMS_DT_OK;
 }

/* ==================== 7.7 Data (CHOICE of 24 alternatives) ==================== */

int cms_ffi_encode_Data(
    int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_small_non_negative(&w, choice);

    encode_data_value(&w, choice, int_val, float_val,
                      str_val, bytes_val, bytes_len);

    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Data(
    const uint8_t *in_buf, int in_len,
    int *choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint32_t _idx;
    per_decode_small_non_negative(&r, &_idx);
    *choice = (int)_idx;

    decode_data_value(&r, *choice, int_val, float_val,
                      str_val, str_cap, bytes_val, bytes_cap);

    return CMS_DT_OK;
}

/* ==================== 7.8 DataDefinition ==================== */

int cms_ffi_encode_DataDefinition(
    int choice,
    int64_t int_val,
    const char *str_val,
    const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_small_non_negative(&w, choice);

    switch (choice) {
    case 0: /* error -> ServiceError */
        per_encode_constrained_int(&w, (int)int_val, 0, 12);
        break;
    case 1: /* array -> SEQUENCE { numberOfElement Int32, elementType DataDefinition } */
        /* For simplicity, encode numberOfElement only (recursive DataDefinition not supported at FFI level) */
        per_encode_constrained_int(&w, int_val, -2147483648, 2147483647);
        break;
    case 2: /* structure -> SEQUENCE OF SEQUENCE { name, fc OPTIONAL, type } */
        /* Encode count then each element */
        {
            uint32_t count = (uint32_t)int_val;
            per_encode_length(&w, count);
            /* Individual elements not encoded at FFI level */
        }
        break;
    case 3: /* boolean -> NULL */
    case 4: /* int8 -> NULL */
    case 5: /* int16 -> NULL */
    case 6: /* int32 -> NULL */
    case 7: /* int64 -> NULL */
    case 8: /* int8u -> NULL */
    case 9: /* int16u -> NULL */
    case 10: /* int32u -> NULL */
    case 11: /* int64u -> NULL */
    case 12: /* float32 -> NULL */
    case 13: /* float64 -> NULL */
    case 18: /* utc-time -> NULL */
    case 19: /* binary-time -> NULL */
    case 20: /* quality -> NULL */
    case 21: /* dbpos -> NULL */
    case 22: /* tcmd -> NULL */
    case 23: /* check -> NULL */
        /* NULL - no payload */
        break;
    case 14: /* bit-string -> INTEGER (length) */
    case 15: /* octet-string -> INTEGER (length) */
    case 16: /* visible-string -> INTEGER (length) */
    case 17: /* unicode-string -> INTEGER (length) */
        per_encode_constrained_int(&w, int_val, 0, 65535);
        break;
    }

    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_DataDefinition(
    const uint8_t *in_buf, int in_len,
    int *choice,
    int64_t *int_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint32_t _idx;
    per_decode_small_non_negative(&r, &_idx);
    *choice = (int)_idx;

    switch (*choice) {
    case 0: { int64_t t; per_decode_constrained_int(&r, &t, 0, 12); *int_val = t; break; }
    case 1: { int64_t t; per_decode_constrained_int(&r, &t, -2147483648, 2147483647); *int_val = t; break; }
    case 2: { uint32_t count; per_decode_length(&r, &count); *int_val = count; break; }
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
        break;
    case 14:
    case 15:
    case 16:
    case 17: { int64_t t; per_decode_constrained_int(&r, &t, 0, 65535); *int_val = t; break; }
    }

    return CMS_DT_OK;
}

static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len)
{
    switch (choice) {
    case 0: per_encode_constrained_int(w, int_val, 0, 12); break;
    case 1:
    case 2: {
        uint32_t count = 0;
        per_encode_length(w, count);
        break;
    }
    case 3: per_encode_boolean(w, (int)int_val); break;
    case 4: per_encode_constrained_int(w, int_val, -128, 127); break;
    case 5: per_encode_constrained_int(w, int_val, -32768, 32767); break;
    case 6: per_encode_constrained_int(w, int_val, -2147483648, 2147483647); break;
    case 7: per_encode_unconstrained_int(w, int_val); break;
    case 8: per_encode_constrained_int(w, int_val, 0, 255); break;
    case 9: per_encode_constrained_int(w, int_val, 0, 65535); break;
    case 10: per_encode_constrained_int(w, int_val, 0, 4294967295); break;
    case 11: per_encode_unconstrained_int(w, int_val); break;
    case 12: {
        uint32_t bits;
        float fv = (float)float_val;
        memcpy(&bits, &fv, sizeof(bits));
        uint8_t fbytes[4];
        fbytes[0] = (uint8_t)(bits >> 24);
        fbytes[1] = (uint8_t)(bits >> 16);
        fbytes[2] = (uint8_t)(bits >> 8);
        fbytes[3] = (uint8_t)(bits);
        per_encode_octet_string_fixed(w, fbytes, 4);
        break;
    }
    case 13: {
        uint64_t bits;
        memcpy(&bits, &float_val, sizeof(bits));
        uint8_t dbytes[8];
        dbytes[0] = (uint8_t)(bits >> 56);
        dbytes[1] = (uint8_t)(bits >> 48);
        dbytes[2] = (uint8_t)(bits >> 40);
        dbytes[3] = (uint8_t)(bits >> 32);
        dbytes[4] = (uint8_t)(bits >> 24);
        dbytes[5] = (uint8_t)(bits >> 16);
        dbytes[6] = (uint8_t)(bits >> 8);
        dbytes[7] = (uint8_t)(bits);
        per_encode_octet_string_fixed(w, dbytes, 8);
        break;
    }
    case 14: per_encode_bit_string(w, bytes_val, bytes_len * 8, 65535); break;
    case 15: per_encode_octet_string(w, bytes_val, bytes_len, 65535); break;
    case 16: per_encode_visible_string(w, str_val, 255); break;
    case 17: per_encode_utf8_string(w, str_val, 255); break;
    case 18: {
        uint8_t bytes[8];
        uint64_t ms = (uint64_t)int_val;
        bytes[0] = (uint8_t)(ms >> 56);
        bytes[1] = (uint8_t)(ms >> 48);
        bytes[2] = (uint8_t)(ms >> 40);
        bytes[3] = (uint8_t)(ms >> 32);
        bytes[4] = (uint8_t)(ms >> 24);
        bytes[5] = (uint8_t)(ms >> 16);
        bytes[6] = (uint8_t)(ms >> 8);
        bytes[7] = (uint8_t)(ms);
        per_encode_octet_string_fixed(w, bytes, 8);
        break;
    }
    case 19: {
        per_encode_constrained_int(w, int_val, 0, 86400000);
        per_encode_constrained_int(w, 0, 0, 65535);
        break;
    }
    case 20: per_encode_bit_string_fixed(w, bytes_val, 13); break;
    case 21: per_encode_small_non_negative(w, (uint32_t)int_val); break;
    case 22: per_encode_small_non_negative(w, (uint32_t)int_val); break;
    case 23: per_encode_bit_string_fixed(w, bytes_val, 16); break;
    }
}

static void decode_data_value(per_stream_t *r, int choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    switch (choice) {
    case 0: { int64_t t; per_decode_constrained_int(r, &t, 0, 12); *int_val = t; break; }
    case 1:
    case 2: { uint32_t count; per_decode_length(r, &count); break; }
    case 3: { bool b; per_decode_boolean(r, &b); *int_val = b ? 1 : 0; break; }
    case 4: { int64_t t; per_decode_constrained_int(r, &t, -128, 127); *int_val = t; break; }
    case 5: { int64_t t; per_decode_constrained_int(r, &t, -32768, 32767); *int_val = t; break; }
    case 6: { int64_t t; per_decode_constrained_int(r, &t, -2147483648, 2147483647); *int_val = t; break; }
    case 7: per_decode_unconstrained_int(r, int_val); break;
    case 8: { int64_t t; per_decode_constrained_int(r, &t, 0, 255); *int_val = t; break; }
    case 9: { int64_t t; per_decode_constrained_int(r, &t, 0, 65535); *int_val = t; break; }
    case 10: { int64_t t; per_decode_constrained_int(r, &t, 0, 4294967295); *int_val = t; break; }
    case 11: per_decode_unconstrained_int(r, int_val); break;
    case 12: {
        uint8_t fbytes[4];
        per_decode_octet_string_fixed(r, fbytes, 4);
        uint32_t bits = ((uint32_t)fbytes[0] << 24) | ((uint32_t)fbytes[1] << 16)
                       | ((uint32_t)fbytes[2] << 8) | (uint32_t)fbytes[3];
        float fv;
        memcpy(&fv, &bits, sizeof(fv));
        *float_val = fv;
        break;
    }
    case 13: {
        uint8_t dbytes[8];
        per_decode_octet_string_fixed(r, dbytes, 8);
        uint64_t bits = ((uint64_t)dbytes[0] << 56) | ((uint64_t)dbytes[1] << 48)
                       | ((uint64_t)dbytes[2] << 40) | ((uint64_t)dbytes[3] << 32)
                       | ((uint64_t)dbytes[4] << 24) | ((uint64_t)dbytes[5] << 16)
                       | ((uint64_t)dbytes[6] << 8)  | (uint64_t)dbytes[7];
        double dv;
        memcpy(&dv, &bits, sizeof(dv));
        *float_val = dv;
        break;
    }
    case 14: {
        int out_nbits = *bytes_cap * 8;
        per_decode_bit_string(r, bytes_val, &out_nbits, 65535);
        *bytes_cap = (out_nbits + 7) / 8;
        break;
    }
    case 15: { size_t ol = (size_t)*bytes_cap; per_decode_octet_string(r, bytes_val, &ol, 65535); *bytes_cap = (int)ol; break; }
    case 16: per_decode_visible_string(r, str_val, (uint32_t)*str_cap); *str_cap = (int)strlen(str_val); break;
    case 17: per_decode_utf8_string(r, str_val, (uint32_t)*str_cap); *str_cap = (int)strlen(str_val); break;
    case 18: {
        uint8_t bytes[8];
        per_decode_octet_string_fixed(r, bytes, 8);
        uint64_t ms = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                    | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                    | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                    | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
        *int_val = (int64_t)ms;
        break;
    }
    case 19: {
        int64_t t;
        per_decode_constrained_int(r, &t, 0, 86400000);
        *int_val = t;
        int64_t _dummy;
        per_decode_constrained_int(r, &_dummy, 0, 65535);
        break;
    }
    case 20: per_decode_bit_string_fixed(r, bytes_val, 13); break;
    case 21: { uint32_t u; per_decode_small_non_negative(r, &u); *int_val = u; break; }
    case 22: { uint32_t u; per_decode_small_non_negative(r, &u); *int_val = u; break; }
    case 23: per_decode_bit_string_fixed(r, bytes_val, 16); break;
    }
}



/* ==================== 7.3.11 ServiceError ==================== */

int cms_ffi_encode_ServiceError(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 12);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_ServiceError(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 12);
    *value = (int)tmp;
    return CMS_DT_OK;
}

/* ==================== 7.5.4 AddCause ==================== */

int cms_ffi_encode_AddCause(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 16);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_AddCause(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 16);
    *value = (int)tmp;
    return CMS_DT_OK;
}

/* ==================== 7.6.7 SmpMod ==================== */

int cms_ffi_encode_SmpMod(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 2);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_SmpMod(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 2);
    *value = (int)tmp;
    return CMS_DT_OK;
}

/* ==================== 7.2.2 BinaryTime / EntryTime ==================== */

int cms_ffi_encode_BinaryTime(
    int32_t hour, int32_t minute, int32_t second, int32_t millisecond,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    int64_t msOfDay = (int64_t)hour * 3600000 + (int64_t)minute * 60000
                    + (int64_t)second * 1000 + millisecond;
    per_encode_constrained_int(&w, msOfDay, 0, 86400000);
    per_encode_constrained_int(&w, 0, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_BinaryTime(
    const uint8_t *in_buf, int in_len,
    int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    int64_t ms;
    per_decode_constrained_int(&r, &ms, 0, 86400000);
    int64_t _dummy;
    per_decode_constrained_int(&r, &_dummy, 0, 65535);

    *hour = (int32_t)(ms / 3600000); ms %= 3600000;
    *minute = (int32_t)(ms / 60000); ms %= 60000;
    *second = (int32_t)(ms / 1000);
    *millisecond = (int32_t)(ms % 1000);
    return CMS_DT_OK;
}

/* ==================== 7.2.1 UtcTime ==================== */

int cms_ffi_encode_UtcTime(
    int64_t timestamp_ms,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    uint8_t bytes[8];
    uint64_t ms = (uint64_t)timestamp_ms;
    bytes[0] = (uint8_t)(ms >> 56);
    bytes[1] = (uint8_t)(ms >> 48);
    bytes[2] = (uint8_t)(ms >> 40);
    bytes[3] = (uint8_t)(ms >> 32);
    bytes[4] = (uint8_t)(ms >> 24);
    bytes[5] = (uint8_t)(ms >> 16);
    bytes[6] = (uint8_t)(ms >> 8);
    bytes[7] = (uint8_t)(ms);
    per_encode_octet_string_fixed(&w, bytes, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_UtcTime(
    const uint8_t *in_buf, int in_len,
    int64_t *timestamp_ms)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint8_t bytes[8];
    per_decode_octet_string_fixed(&r, bytes, 8);
    uint64_t ms = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
    *timestamp_ms = (int64_t)ms;
    return CMS_DT_OK;
}

/* ==================== 7.3.4 TimeStamp ==================== */

int cms_ffi_encode_TimeStamp(
    int64_t seconds_since_epoch, int64_t fractional,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_constrained_int(&w, seconds_since_epoch, -9223372036854775807LL - 1, 9223372036854775807LL);
    per_encode_constrained_int(&w, fractional, -2147483648, 2147483647);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_TimeStamp(
    const uint8_t *in_buf, int in_len,
    int64_t *seconds_since_epoch, int64_t *fractional)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    per_decode_constrained_int(&r, seconds_since_epoch, -9223372036854775807LL - 1, 9223372036854775807LL);
    per_decode_constrained_int(&r, fractional, -2147483648, 2147483647);
    return CMS_DT_OK;
}

/* ==================== 7.5.2 Originator ==================== */

int cms_ffi_encode_Originator(
    int or_cat,
    const uint8_t *or_ident, int or_ident_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_constrained_int(&w, or_cat, 0, 8);
    per_encode_octet_string(&w, or_ident, or_ident_len, 64);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Originator(
    const uint8_t *in_buf, int in_len,
    int *or_cat,
    uint8_t *or_ident, int *or_ident_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    int64_t _tmp;
    per_decode_constrained_int(&r, &_tmp, 0, 8);
    *or_cat = (int)_tmp;

    size_t out_len = (size_t)*or_ident_cap;
    per_decode_octet_string(&r, or_ident, &out_len, 64);
    *or_ident_cap = (int)out_len;

    return CMS_DT_OK;
}

/* ==================== 7.3.12 PhyComAddr ==================== */

int cms_ffi_encode_PhyComAddr(
    const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, addr, 6);
    per_encode_constrained_int(&w, priority, 0, 255);
    per_encode_constrained_int(&w, vid, 0, 65535);
    per_encode_constrained_int(&w, appid, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_PhyComAddr(
    const uint8_t *in_buf, int in_len,
    uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, addr, 6);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 255);
    *priority = (uint8_t)tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *vid = (uint16_t)tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *appid = (uint16_t)tmp;
    return CMS_DT_OK;
}

/* ==================== 7.3.6 Quality / 7.3.5 Dbpos / 7.3.7 Tcmd / 7.5.3 Check ==================== */

int cms_ffi_encode_Quality(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 13);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Quality(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 13);
    return CMS_DT_OK;
}

int cms_ffi_encode_Dbpos(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Dbpos(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t tmp;
    per_decode_small_non_negative(&r, &tmp);
    *value = (int)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Tcmd(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Tcmd(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t tmp;
    per_decode_small_non_negative(&r, &tmp);
    *value = (int)tmp;
    return CMS_DT_OK;
}

int cms_ffi_encode_Check(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 16);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Check(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 16);
    return CMS_DT_OK;
}

/* ==================== 7.6.5 LcbOptFlds ==================== */

int cms_ffi_encode_LcbOptFlds(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 1);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_LcbOptFlds(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 1);
    return CMS_DT_OK;
}

/* ==================== 7.6.6 MsvcbOptFlds ==================== */

int cms_ffi_encode_MsvcbOptFlds(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 5);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_MsvcbOptFlds(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 5);
    return CMS_DT_OK;
}

/* ==================== 7.6.4 RcbOptFlds ==================== */

int cms_ffi_encode_RcbOptFlds(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 10);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_RcbOptFlds(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 10);
    return CMS_DT_OK;
}

/* ==================== 7.6.3 ReasonCode ==================== */

int cms_ffi_encode_ReasonCode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 7);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_ReasonCode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 7);
    return CMS_DT_OK;
}

/* ==================== 7.2.1 TimeQuality ==================== */

int cms_ffi_encode_TimeQuality(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 3);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_TimeQuality(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 3);
    return CMS_DT_OK;
}

/* ==================== 7.6.2 TriggerConditions ==================== */

int cms_ffi_encode_TriggerConditions(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 6);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_TriggerConditions(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 6);
    return CMS_DT_OK;
}

int cms_ffi_encode_PackedList(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string(&w, value, value_len * 8, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_PackedList(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int out_nbits = *value_cap * 8;
    per_decode_bit_string(&r, value, &out_nbits, 65535);
    *value_cap = (out_nbits + 7) / 8;
    return CMS_DT_OK;
}
