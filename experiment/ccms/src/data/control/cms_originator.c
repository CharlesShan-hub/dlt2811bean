#include "data/control/cms_originator.h"

/* ---- internal stream version ---- */

int cms_originator_encode_stream(per_stream_t *s, int or_cat, const uint8_t *or_ident, int or_ident_len)
{
    per_encode_constrained_int(s, or_cat, 0, 8);
    per_encode_octet_string(s, or_ident, or_ident_len, 64);
    return CMS_OK;
}
int cms_originator_decode_stream(per_stream_t *s, int *or_cat, uint8_t *or_ident, int *or_ident_cap)
{
    int64_t t;
    per_decode_constrained_int(s, &t, 0, 8);
    *or_cat = (int)t;
    size_t l = (size_t)*or_ident_cap;
    per_decode_octet_string(s, or_ident, &l, 64);
    *or_ident_cap = (int)l;
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_originator_encode(int oc, const uint8_t *oi, int oil, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_originator_encode_stream(&w, oc, oi, oil); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_originator_decode(const uint8_t *b, int l, int *oc, uint8_t *oi, int *oil)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_originator_decode_stream(&r, oc, oi, oil); return CMS_OK; }
