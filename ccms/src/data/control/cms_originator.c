#include "data/control/cms_originator.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_enumerated.h"
#include "data/basic/cms_string.h"

/* ---- internal stream version ---- */

int cms_originator_encode_stream(per_stream_t *s, const cms_originator_t *v){
    int rc = cms_enumerated_encode_stream(s, &v->or_cat);
    if (rc) return rc;
    cms_octet_string_var_t _s = { v->or_ident.value, v->or_ident.len, CMS_ORIGINATOR_OR_IDENT_MAX_LEN };
    return cms_octet_string_var_encode_stream(s, &_s);
}

int cms_originator_decode_stream(per_stream_t *s, cms_originator_t *v){
    int rc = cms_enumerated_decode_stream(s, &v->or_cat);
    if (rc) return rc;
    cms_octet_string_var_t _s = { v->or_ident.value, 0, CMS_ORIGINATOR_OR_IDENT_MAX_LEN };
    rc = cms_octet_string_var_decode_stream(s, &_s);
    v->or_ident.len = _s.len;
    return rc;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_originator_encode(const cms_originator_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_originator_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_originator_decode(cms_originator_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_originator_decode_stream(&r, v);
}
