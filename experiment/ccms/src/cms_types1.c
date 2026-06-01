#include "cms_types1.h"
#include "per_stream.h"
#include "per_boolean.h"
#include "per_integer.h"
#include "per_string.h"
#include "per_bit_string.h"
#include <string.h>
#include <stdlib.h>

/* 7.1.1 BOOLEAN */
CMS_EXPORT int cms_boolean_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_boolean(&w, value ? 1 : 0);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_boolean_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    bool b;
    per_decode_boolean(&r, &b);
    *value = b ? 1 : 0;
    return CMS_OK;
}

/* 7.1.2 Integer Types */
CMS_EXPORT int cms_int8_encode(int8_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, -128, 127);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int8_decode(const uint8_t *in_buf, int in_len, int8_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, -128, 127);
    *value = (int8_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_int8u_encode(uint8_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 255);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int8u_decode(const uint8_t *in_buf, int in_len, uint8_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 255);
    *value = (uint8_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_int16_encode(int16_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, -32768, 32767);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int16_decode(const uint8_t *in_buf, int in_len, int16_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, -32768, 32767);
    *value = (int16_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_int16u_encode(uint16_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int16u_decode(const uint8_t *in_buf, int in_len, uint16_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *value = (uint16_t)tmp;
    return CMS_OK;
}

/* 7.1.2 Int24U */
CMS_EXPORT int cms_int24u_encode(uint32_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 16777215);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int24u_decode(const uint8_t *in_buf, int in_len, uint32_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 16777215);
    *value = (uint32_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_int32_encode(int32_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, -2147483648, 2147483647);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int32_decode(const uint8_t *in_buf, int in_len, int32_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, -2147483648, 2147483647);
    *value = (int32_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_int32u_encode(uint32_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 4294967295);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int32u_decode(const uint8_t *in_buf, int in_len, uint32_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 4294967295);
    *value = (uint32_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_int64_encode(int64_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_unconstrained_int(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int64_decode(const uint8_t *in_buf, int in_len, int64_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_unconstrained_int(&r, value);
    return CMS_OK;
}

CMS_EXPORT int cms_int64u_encode(uint64_t value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_unconstrained_int(&w, (int64_t)value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_int64u_decode(const uint8_t *in_buf, int in_len, uint64_t *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_unconstrained_int(&r, &tmp);
    *value = (uint64_t)tmp;
    return CMS_OK;
}

/* 7.1.4 Float32 / Float64 */
CMS_EXPORT int cms_float32_encode(float value, uint8_t *out_buf, int *out_len)
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
    return CMS_OK;
}

CMS_EXPORT int cms_float32_decode(const uint8_t *in_buf, int in_len, float *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint8_t bytes[4];
    per_decode_octet_string_fixed(&r, bytes, 4);
    uint32_t bits = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                  | ((uint32_t)bytes[2] << 8) | (uint32_t)bytes[3];
    memcpy(value, &bits, sizeof(bits));
    return CMS_OK;
}

CMS_EXPORT int cms_float64_encode(double value, uint8_t *out_buf, int *out_len)
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
    return CMS_OK;
}

CMS_EXPORT int cms_float64_decode(const uint8_t *in_buf, int in_len, double *value)
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
    return CMS_OK;
}

CMS_EXPORT int cms_visible_string_encode(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 255);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_visible_string_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

CMS_EXPORT int cms_utf8_string_encode(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_utf8_string(&w, value, 255);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_utf8_string_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_utf8_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

CMS_EXPORT int cms_octet_string_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string(&w, value, value_len, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_octet_string_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    size_t out_len = (size_t)*value_cap;
    per_decode_octet_string(&r, value, &out_len, 65535);
    *value_cap = (int)out_len;
    return CMS_OK;
}

/* 7.1.8 BitString / PackedList */
CMS_EXPORT int cms_bit_string_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string(&w, value, value_len * 8, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_bit_string_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int out_nbits = *value_cap * 8;
    per_decode_bit_string(&r, value, &out_nbits, 65535);
    *value_cap = (out_nbits + 7) / 8;
    return CMS_OK;
 }

CMS_EXPORT int cms_packed_list_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string(&w, value, value_len * 8, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_packed_list_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int out_nbits = *value_cap * 8;
    per_decode_bit_string(&r, value, &out_nbits, 65535);
    *value_cap = (out_nbits + 7) / 8;
    return CMS_OK;
}
