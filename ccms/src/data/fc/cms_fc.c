#include "data/fc/cms_fc.h"
#include "data/basic/cms_string.h"

/* ---- internal stream version ---- */

int cms_fc_encode_stream(per_stream_t *s, const cms_fc_t *v){
    cms_visible_string_fixed_t _s = { v->value, CMS_FC_LEN };
    return cms_visible_string_fixed_encode_stream(s, &_s);
}

int cms_fc_decode_stream(per_stream_t *s, cms_fc_t *v){
    cms_visible_string_fixed_t _s = { v->value, CMS_FC_LEN };
    return cms_visible_string_fixed_decode_stream(s, &_s);
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_fc_encode(const cms_fc_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_fc_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_fc_decode(cms_fc_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_fc_decode_stream(&r, v);
}
