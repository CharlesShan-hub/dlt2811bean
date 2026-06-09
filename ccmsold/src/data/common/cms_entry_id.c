#include "data/common/cms_entry_id.h"
#include "data/basic/cms_string.h"

int cms_entry_id_encode_stream(per_stream_t *s, const cms_entry_id_t *v){
    cms_octet_string_fixed_t _s = { v->value, CMS_ENTRY_ID_LEN };
    return cms_octet_string_fixed_encode_stream(s, &_s);
}
int cms_entry_id_decode_stream(per_stream_t *s, cms_entry_id_t *v){
    cms_octet_string_fixed_t _s = { v->value, CMS_ENTRY_ID_LEN };
    int rc = cms_octet_string_fixed_decode_stream(s, &_s);
    if (rc) return rc;
    v->len = CMS_ENTRY_ID_LEN;
    return CMS_OK;
}

CMS_EXPORT int cms_entry_id_encode(const cms_entry_id_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_entry_id_encode_stream(&w, v); 
    *l = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_entry_id_decode(cms_entry_id_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_entry_id_decode_stream(&r, v); 
}
