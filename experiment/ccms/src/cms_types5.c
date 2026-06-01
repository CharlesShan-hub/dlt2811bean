#include "cms_types5.h"
#include "per_stream.h"
#include "per_integer.h"
#include "per_string.h"
#include "per_bit_string.h"
#include <string.h>
#include <stdlib.h>

/* 7.5.2 Originator */
CMS_EXPORT int cms_originator_encode(
    int or_cat,
    const uint8_t *or_ident, int or_ident_len,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    per_encode_constrained_int(&w, or_cat, 0, 8);
    per_encode_octet_string(&w, or_ident, or_ident_len, 64);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_originator_decode(
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

    return CMS_OK;
}

CMS_EXPORT int cms_check_encode(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 16);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_check_decode(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 16);
    return CMS_OK;
}

/* 7.5.4 AddCause */
CMS_EXPORT int cms_add_cause_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 16);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_add_cause_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 16);
    *value = (int)tmp;
    return CMS_OK;
}
