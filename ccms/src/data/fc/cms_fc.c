#include "data/fc/cms_fc.h"

/* ---- internal stream version ---- */

int cms_fc_encode_stream(per_stream_t *s, const cms_visible_string_fixed_t *v)
    { per_encode_visible_string_fixed(s, v->value, 2); return CMS_OK; }
int cms_fc_decode_stream(per_stream_t *s, cms_visible_string_fixed_t *v)
    { per_decode_visible_string_fixed(s, v->value, 2); return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_fc_encode(const cms_visible_string_fixed_t *v, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_fc_encode_stream(&w, v); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_fc_decode(const uint8_t *in_buf, int in_len, cms_visible_string_fixed_t *v)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_fc_decode_stream(&r, v); return CMS_OK; }
