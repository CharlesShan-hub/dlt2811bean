#include "data/common/cms_dbpos.h"
#include "data/basic/cms_coded_enum.h"

int cms_dbpos_encode_stream(per_stream_t *s, const cms_dbpos_t *v){
    uint8_t buf[1] = { (uint8_t)v->value.value };
    cms_bit_string_fixed_t bs = { buf, 2 };
    return cms_coded_enum_encode_stream(s, &bs);
}

int cms_dbpos_decode_stream(per_stream_t *s, cms_dbpos_t *v){
    uint8_t buf[1] = {0};
    cms_bit_string_fixed_t bs = { buf, 2 };
    int rc = cms_coded_enum_decode_stream(s, &bs);
    if (rc) return rc;
    v->value.value = buf[0];
    return CMS_OK;
}

CMS_EXPORT int cms_dbpos_encode(const cms_dbpos_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_dbpos_encode_stream(&w, v); 
    *l = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_dbpos_decode(cms_dbpos_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_dbpos_decode_stream(&r, v); 
}
