#include "data/block/cms_smp_mod.h"
#include "per/cms_integer.h"
#include "per/cms_stream.h"

int cms_smp_mod_encode_stream(per_stream_t *s, int value)
    { per_encode_constrained_int(s, value, 0, 2); return CMS_OK; }
int cms_smp_mod_decode_stream(per_stream_t *s, int *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 2); *value = (int)t; return CMS_OK; }

CMS_EXPORT int cms_smp_mod_encode(int value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_smp_mod_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_smp_mod_decode(const uint8_t *in_buf, int in_len, int *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_smp_mod_decode_stream(&r, value); return CMS_OK; }
