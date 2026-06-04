#include "data/control/cms_originator.h"

/* ---- internal stream version ---- */

int cms_originator_encode_stream(per_stream_t *s, cms_orcat_t or_cat, const cms_octet_string_var_t *or_ident)
{
    per_encode_constrained_int(s, or_cat, 0, 8);
    per_encode_octet_string(s, or_ident->value, or_ident->len, or_ident->max_len);
    return CMS_OK;
}
int cms_originator_decode_stream(per_stream_t *s, cms_orcat_t *or_cat, cms_octet_string_var_t *or_ident)
{
    int64_t t;
    per_decode_constrained_int(s, &t, 0, 8);
    *or_cat = (cms_orcat_t)(int)t;
    size_t l = 0;
    per_decode_octet_string(s, or_ident->value, &l, 64);
    or_ident->len = (int)l;
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_originator_encode(cms_orcat_t oc, const cms_octet_string_var_t *oi, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_originator_encode_stream(&w, oc, oi); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_originator_decode(const uint8_t *b, int l, cms_orcat_t *oc, cms_octet_string_var_t *oi)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_originator_decode_stream(&r, oc, oi); return CMS_OK; }
