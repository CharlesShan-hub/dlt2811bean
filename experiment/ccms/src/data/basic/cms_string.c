#include "data/basic/cms_string.h"
#include "per/cms_string.h"
#include "per/cms_bit_string.h"
#include "per/cms_stream.h"
#include <string.h>
#include <stdlib.h>

/* ---- 7.1.5 VisibleString ---- */
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

/* ---- 7.1.5 UTF8String ---- */
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

/* ---- 7.1.5 OctetString ---- */
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

/* ---- 7.1.5 BitString ---- */
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