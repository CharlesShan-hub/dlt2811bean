#include "data/basic/cms_float.h"
#include <string.h>

/* ---- internal stream version ---- */

int cms_float32_encode_stream(per_stream_t *s, const cms_float32_t *v){
    uint32_t bits;
    memcpy(&bits, &v->value, sizeof(bits));
    uint8_t bytes[4];
    bytes[0] = (uint8_t)(bits >> 24);
    bytes[1] = (uint8_t)(bits >> 16);
    bytes[2] = (uint8_t)(bits >> 8);
    bytes[3] = (uint8_t)(bits);
    return per_encode_octet_string_fixed(s, bytes, 4);
}

int cms_float32_decode_stream(per_stream_t *s, cms_float32_t *v){
    uint8_t bytes[4];
    int rc = per_decode_octet_string_fixed(s, bytes, 4);
    if (rc) return rc;
    uint32_t bits = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                  | ((uint32_t)bytes[2] << 8) | (uint32_t)bytes[3];
    memcpy(&v->value, &bits, sizeof(bits));
    return CMS_OK;
}

int cms_float64_encode_stream(per_stream_t *s, const cms_float64_t *v){
    uint64_t bits;
    memcpy(&bits, &v->value, sizeof(bits));
    uint8_t bytes[8];
    bytes[0] = (uint8_t)(bits >> 56);
    bytes[1] = (uint8_t)(bits >> 48);
    bytes[2] = (uint8_t)(bits >> 40);
    bytes[3] = (uint8_t)(bits >> 32);
    bytes[4] = (uint8_t)(bits >> 24);
    bytes[5] = (uint8_t)(bits >> 16);
    bytes[6] = (uint8_t)(bits >> 8);
    bytes[7] = (uint8_t)(bits);
    return per_encode_octet_string_fixed(s, bytes, 8);
}

int cms_float64_decode_stream(per_stream_t *s, cms_float64_t *v){
    uint8_t bytes[8];
    int rc = per_decode_octet_string_fixed(s, bytes, 8);
    if (rc) return rc;
    uint64_t bits = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                  | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                  | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                  | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
    memcpy(&v->value, &bits, sizeof(bits));
    return CMS_OK;
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_float32_encode(const cms_float32_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_float32_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_float32_decode(cms_float32_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_float32_decode_stream(&r, v); 
}
CMS_EXPORT int cms_float64_encode(const cms_float64_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_float64_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_float64_decode(cms_float64_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_float64_decode_stream(&r, v); 
}
