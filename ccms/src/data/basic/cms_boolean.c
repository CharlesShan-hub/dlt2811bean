#include "data/basic/cms_boolean.h"

/* ---- internal stream version ---- */

int cms_boolean_encode_stream(per_stream_t *s, const cms_boolean_t *v){ 
    return per_stream_write_bit(s, v->value ? 1 : 0); 
}

int cms_boolean_decode_stream(per_stream_t *s, cms_boolean_t *v){ 
    return per_stream_read_bit(s, &v->value); 
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_boolean_encode(const cms_boolean_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_boolean_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}

CMS_EXPORT int cms_boolean_decode(cms_boolean_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_boolean_decode_stream(&r, v); 
}
