#include "data/block/cms_smp_mod.h"
#include "data/basic/cms_enumerated.h"

int cms_smp_mod_encode_stream(per_stream_t *s, const cms_smp_mod_t *v){ 
    return cms_enumerated_encode_stream(s, &v->value); 
}

int cms_smp_mod_decode_stream(per_stream_t *s, cms_smp_mod_t *v){ 
    return cms_enumerated_decode_stream(s, &v->value);
}

CMS_EXPORT int cms_smp_mod_encode(const cms_smp_mod_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_smp_mod_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_smp_mod_decode(cms_smp_mod_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_smp_mod_decode_stream(&r, v);
}
