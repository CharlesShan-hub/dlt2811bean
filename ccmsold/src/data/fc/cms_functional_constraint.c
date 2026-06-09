#include "data/fc/cms_functional_constraint.h"
#include "data/basic/cms_string.h"

/* ---- internal stream version ---- */

int cms_functional_constraint_encode_stream(per_stream_t *s, const cms_functional_constraint_t *v){
    cms_visible_string_fixed_t _s = { v->value, CMS_FUNCTIONAL_CONSTRAINT_LEN };
    return cms_visible_string_fixed_encode_stream(s, &_s);
}

int cms_functional_constraint_decode_stream(per_stream_t *s, cms_functional_constraint_t *v){
    cms_visible_string_fixed_t _s = { v->value, CMS_FUNCTIONAL_CONSTRAINT_LEN };
    int rc = cms_visible_string_fixed_decode_stream(s, &_s);
    if (rc) return rc;
    v->len = CMS_FUNCTIONAL_CONSTRAINT_LEN;
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_functional_constraint_encode(const cms_functional_constraint_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_functional_constraint_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_functional_constraint_decode(cms_functional_constraint_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_functional_constraint_decode_stream(&r, v);
}
