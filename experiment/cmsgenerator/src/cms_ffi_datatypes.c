#include "cms_ffi_datatypes.h"
#include "gen_cms.h"
#include "cmsper/cmsper.h"
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

/* ==================== 7.2.1 ObjectName ==================== */

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

/* ==================== 7.2.2 ObjectReference ==================== */

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

/* ==================== 7.2.3 FC ==================== */

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

/* ==================== 7.2.4 SubReference ==================== */

int cms_ffi_encode_SubReference(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 64);
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

/* ==================== 7.2.5 EntryID ==================== */

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

/* ==================== 7.2.6 BitString ==================== */

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

/* ==================== 7.7 Data ==================== */

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

static void encode_data_value(per_stream_t *w, int choice,
    int64_t int_val, double float_val,
    const char *str_val, const uint8_t *bytes_val, int bytes_len)
{
    switch (choice) {
    case 0: encode_ServiceError(w, (int)int_val); break;
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
        UtcTime utc;
        memset(&utc, 0, sizeof(utc));
        utc.secondsSinceEpoch = int_val / 1000;
        utc.fractional = (int_val % 1000) * 1000000;
        utc._has_fractional = 1;
        encode_UtcTime(w, &utc);
        break;
    }
    case 19: {
        BinaryTime bt;
        memset(&bt, 0, sizeof(bt));
        bt.msOfDay = int_val;
        bt.daysSince1984 = 0;
        encode_BinaryTime(w, &bt);
        break;
    }
    case 20: per_encode_bit_string_fixed(w, bytes_val, 13); break;
    case 21: encode_Dbpos(w, (int)int_val); break;
    case 22: encode_Tcmd(w, (int)int_val); break;
    case 23: per_encode_bit_string_fixed(w, bytes_val, 16); break;
    }
}

static void decode_data_value(per_stream_t *r, int choice,
    int64_t *int_val, double *float_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap)
{
    switch (choice) {
    case 0: { int v; decode_ServiceError(r, &v); *int_val = v; break; }
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
        UtcTime utc;
        memset(&utc, 0, sizeof(utc));
        decode_UtcTime(r, &utc);
        *int_val = utc.secondsSinceEpoch * 1000 + utc.fractional / 1000000;
        break;
    }
    case 19: {
        BinaryTime bt;
        memset(&bt, 0, sizeof(bt));
        decode_BinaryTime(r, &bt);
        *int_val = bt.msOfDay;
        break;
    }
    case 20: per_decode_bit_string_fixed(r, bytes_val, 13); break;
    case 21: decode_Dbpos(r, (int *)int_val); break;
    case 22: decode_Tcmd(r, (int *)int_val); break;
    case 23: per_decode_bit_string_fixed(r, bytes_val, 16); break;
    }
}

int cms_ffi_encode_DataDefinition(
    const char *data_name, const char *data_type,
    const uint8_t fc[2],
    int data_choice, int64_t data_int, double data_float,
    const char *data_str, const uint8_t *data_bytes, int data_bytes_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_visible_string(&w, data_name, 255);
    per_encode_visible_string(&w, data_type, 255);
    per_encode_bit_string_fixed(&w, fc, 16);
    per_encode_small_non_negative(&w, data_choice);
    encode_data_value(&w, data_choice, data_int, data_float,
                      data_str, data_bytes, data_bytes_len);

    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_DataDefinition(
    const uint8_t *in_buf, int in_len,
    char *data_name, int *data_name_cap,
    char *data_type, int *data_type_cap,
    uint8_t fc[2],
    int *data_choice, int64_t *data_int, double *data_float,
    char *data_str, int *data_str_cap,
    uint8_t *data_bytes, int *data_bytes_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    per_decode_visible_string(&r, data_name, (uint32_t)*data_name_cap);
    *data_name_cap = (int)strlen(data_name);

    per_decode_visible_string(&r, data_type, (uint32_t)*data_type_cap);
    *data_type_cap = (int)strlen(data_type);

    per_decode_bit_string_fixed(&r, fc, 16);

    uint32_t _idx;
    per_decode_small_non_negative(&r, &_idx);
    *data_choice = (int)_idx;

    decode_data_value(&r, *data_choice, data_int, data_float,
                      data_str, data_str_cap,
                      data_bytes, data_bytes_cap);

    return CMS_DT_OK;
}

/* ==================== 7.9 ServiceError ==================== */

int cms_ffi_encode_ServiceError(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    encode_ServiceError(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_ServiceError(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    decode_ServiceError(&r, value);
    return CMS_DT_OK;
}

/* ==================== 7.10 AddCause / OrCat / SmpMod ==================== */

int cms_ffi_encode_AddCause(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    encode_AddCause(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_AddCause(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    decode_AddCause(&r, value);
    return CMS_DT_OK;
}

int cms_ffi_encode_OrCat(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    encode_OrCat(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_OrCat(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    decode_OrCat(&r, value);
    return CMS_DT_OK;
}

int cms_ffi_encode_SmpMod(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    encode_SmpMod(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_SmpMod(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    decode_SmpMod(&r, value);
    return CMS_DT_OK;
}

/* ==================== 7.3.1 BinaryTime ==================== */

int cms_ffi_encode_BinaryTime(
    int32_t hour, int32_t minute, int32_t second, int32_t millisecond,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    BinaryTime bt;
    memset(&bt, 0, sizeof(bt));
    bt.msOfDay = (int64_t)hour * 3600000 + (int64_t)minute * 60000
               + (int64_t)second * 1000 + millisecond;
    bt.daysSince1984 = 0;

    encode_BinaryTime(&w, &bt);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_BinaryTime(
    const uint8_t *in_buf, int in_len,
    int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    BinaryTime bt;
    memset(&bt, 0, sizeof(bt));
    decode_BinaryTime(&r, &bt);

    int64_t ms = bt.msOfDay;
    *hour = (int32_t)(ms / 3600000); ms %= 3600000;
    *minute = (int32_t)(ms / 60000); ms %= 60000;
    *second = (int32_t)(ms / 1000);
    *millisecond = (int32_t)(ms % 1000);
    return CMS_DT_OK;
}

/* ==================== 7.3.2 UtcTime ==================== */

int cms_ffi_encode_UtcTime(
    int64_t timestamp_ms,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    UtcTime utc;
    memset(&utc, 0, sizeof(utc));
    utc.secondsSinceEpoch = timestamp_ms / 1000;
    utc.fractional = (timestamp_ms % 1000) * 1000000;
    utc._has_fractional = 1;

    encode_UtcTime(&w, &utc);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_UtcTime(
    const uint8_t *in_buf, int in_len,
    int64_t *timestamp_ms)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    UtcTime utc;
    memset(&utc, 0, sizeof(utc));
    decode_UtcTime(&r, &utc);

    *timestamp_ms = utc.secondsSinceEpoch * 1000 + utc.fractional / 1000000;
    return CMS_DT_OK;
}

/* ==================== 7.3.3 TimeStamp ==================== */

int cms_ffi_encode_TimeStamp(
    int64_t seconds_since_epoch, int64_t fractional,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    TimeStamp ts;
    memset(&ts, 0, sizeof(ts));
    ts.secondsSinceEpoch = seconds_since_epoch;
    ts.fractional = fractional;

    encode_TimeStamp(&w, &ts);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_TimeStamp(
    const uint8_t *in_buf, int in_len,
    int64_t *seconds_since_epoch, int64_t *fractional)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    TimeStamp ts;
    memset(&ts, 0, sizeof(ts));
    decode_TimeStamp(&r, &ts);

    *seconds_since_epoch = ts.secondsSinceEpoch;
    *fractional = ts.fractional;
    return CMS_DT_OK;
}

/* ==================== 7.4 Originator ==================== */

int cms_ffi_encode_Originator(
    int or_cat,
    const uint8_t *or_ident, int or_ident_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    Originator orig;
    memset(&orig, 0, sizeof(orig));
    orig.orCat = (OrCat)or_cat;
    orig.orIdent = (uint8_t *)or_ident;
    orig.orIdent_len = or_ident_len;

    encode_Originator(&w, &orig);
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

    /* decode_Originator only decodes orCat, not orIdent */
    int64_t _tmp;
    per_decode_constrained_int(&r, &_tmp, 0, 8);
    *or_cat = (int)_tmp;

    /* manually decode orIdent (OCTET STRING) */
    size_t out_len = (size_t)*or_ident_cap;
    per_decode_octet_string(&r, or_ident, &out_len, 65535);
    *or_ident_cap = (int)out_len;

    return CMS_DT_OK;
}

/* ==================== 7.5 PhyComAddr ==================== */

int cms_ffi_encode_PhyComAddr(const uint8_t value[6], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, value, 6);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_PhyComAddr(const uint8_t *in_buf, int in_len, uint8_t value[6])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, value, 6);
    return CMS_DT_OK;
}

/* ==================== 7.6 CODED ENUM types ==================== */

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
    encode_Dbpos(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Dbpos(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    decode_Dbpos(&r, value);
    return CMS_DT_OK;
}

int cms_ffi_encode_Tcmd(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    encode_Tcmd(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_Tcmd(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    decode_Tcmd(&r, value);
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

int cms_ffi_encode_LcbOptFlds(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 6);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_LcbOptFlds(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 6);
    return CMS_DT_OK;
}

int cms_ffi_encode_MsvcbOptFlds(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_MsvcbOptFlds(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 8);
    return CMS_DT_OK;
}

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

int cms_ffi_encode_ReasonCode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 6);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_ReasonCode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 6);
    return CMS_DT_OK;
}

int cms_ffi_encode_TimeQuality(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_DT_OK;
}

int cms_ffi_decode_TimeQuality(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 8);
    return CMS_DT_OK;
}

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