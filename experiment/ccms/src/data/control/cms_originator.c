#include "data/control/cms_originator.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>
#include <stdlib.h>

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