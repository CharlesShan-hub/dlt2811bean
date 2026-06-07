#include "data/block/cms_lcb_opt_flds.h"
#include "data/basic/cms_string.h"

int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const cms_lcb_opt_flds_t *v){
    uint8_t buf[1] = { (uint8_t)(v->value.value << 7) };
    cms_bit_string_fixed_t bs = { buf, 1 };
    return cms_bit_string_fixed_encode_stream(s, &bs);
}

int cms_lcb_opt_flds_decode_stream(per_stream_t *s, cms_lcb_opt_flds_t *v){
    uint8_t buf[1] = {0};
    cms_bit_string_fixed_t bs = { buf, 1 };
    int rc = cms_bit_string_fixed_decode_stream(s, &bs);
    if (rc) return rc;
    v->value.value = (buf[0] >> 7) & 1;
    return CMS_OK;
}

CMS_EXPORT int cms_lcb_opt_flds_encode(const cms_lcb_opt_flds_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l);
    int rc = cms_lcb_opt_flds_encode_stream(&w, v);
    *l = (int)per_stream_bytes_written(&w);
    return rc;
}
CMS_EXPORT int cms_lcb_opt_flds_decode(cms_lcb_opt_flds_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l);
    return cms_lcb_opt_flds_decode_stream(&r, v);
}
