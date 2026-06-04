#include "data/control/cms_originator.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_enumerated.h"

/* ---- internal stream version ---- */

int cms_originator_encode_stream(per_stream_t *s, cms_orcat_t or_cat, const cms_octet_string_var_t *or_ident)
{
    cms_int32_t v = { or_cat };
    int rc = cms_enumerated_encode_stream(s, &v);
    if (rc) return rc;
    return per_encode_octet_string(s, or_ident->value, or_ident->len, 64);
}
int cms_originator_decode_stream(per_stream_t *s, cms_orcat_t *or_cat, cms_octet_string_var_t *or_ident)
{
    cms_int32_t v;
    int rc = cms_enumerated_decode_stream(s, &v);
    if (rc) return rc;
    *or_cat = (cms_orcat_t)v.value;
    size_t l = 0;
    rc = per_decode_octet_string(s, or_ident->value, &l, 64);
    or_ident->len = (int)l;
    return rc;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_originator_encode(cms_orcat_t oc, const cms_octet_string_var_t *oi, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_originator_encode_stream(&w, oc, oi); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_originator_decode(cms_orcat_t *oc, cms_octet_string_var_t *oi, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_originator_decode_stream(&r, oc, oi); }
