#include "data/block/cms_smp_mod.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include <string.h>
#include <stdlib.h>

CMS_EXPORT int cms_smp_mod_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 2);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_smp_mod_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 2);
    *value = (int)tmp;
    return CMS_OK;
}