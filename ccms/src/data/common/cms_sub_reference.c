#include "data/common/cms_sub_reference.h"
#include "data/basic/cms_string.h"

int cms_sub_reference_encode_stream(per_stream_t *s, const cms_sub_reference_t *v){ 
    cms_visible_string_var_t _s = { (uint8_t *)v->value, CMS_SUB_REFERENCE_MAX_LEN }; 
    return cms_visible_string_var_encode_stream(s, &_s); 
}
int cms_sub_reference_decode_stream(per_stream_t *s, cms_sub_reference_t *v){ 
    cms_visible_string_var_t _s = { v->value, CMS_SUB_REFERENCE_MAX_LEN }; 
    return cms_visible_string_var_decode_stream(s, &_s); 
}

CMS_EXPORT int cms_sub_reference_encode(const cms_sub_reference_t *v, uint8_t *b, int *l){ 
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_sub_reference_encode_stream(&w, v); 
    *l = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_sub_reference_decode(cms_sub_reference_t *v, const uint8_t *b, int l){ 
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_sub_reference_decode_stream(&r, v); 
}
