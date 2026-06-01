#include "data/basic/cms_integer.h"
#include "per/cms_integer.h"
#include "per/cms_stream.h"
#include <string.h>
#include <stdlib.h>

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