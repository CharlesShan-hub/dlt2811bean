#include "data/basic/cms_string.h"
#include <string.h>

/* ---- struct-based stream version ---- */

int cms_visible_string_fixed_encode_stream(per_stream_t *s, const cms_visible_string_fixed_t *v){
    return per_encode_visible_string_fixed(s, v->value, v->fixed_len); 
}
int cms_visible_string_fixed_decode_stream(per_stream_t *s, cms_visible_string_fixed_t *v){
    return per_decode_visible_string_fixed(s, v->value, v->fixed_len); 
}

int cms_visible_string_var_encode_stream(per_stream_t *s, const cms_visible_string_var_t *v){
    return per_encode_visible_string(s, v->value, v->max_len); 
}
int cms_visible_string_var_decode_stream(per_stream_t *s, cms_visible_string_var_t *v){
    return per_decode_visible_string(s, v->value, v->max_len); 
}

int cms_utf8_string_fixed_encode_stream(per_stream_t *s, const cms_utf8_string_fixed_t *v){
    return per_encode_utf8_string_fixed(s, v->value, v->fixed_len); 
}
int cms_utf8_string_fixed_decode_stream(per_stream_t *s, cms_utf8_string_fixed_t *v){
    return per_decode_utf8_string_fixed(s, v->value, v->fixed_len); 
}

int cms_utf8_string_var_encode_stream(per_stream_t *s, const cms_utf8_string_var_t *v){
    return per_encode_utf8_string(s, v->value, v->max_len); 
}
int cms_utf8_string_var_decode_stream(per_stream_t *s, cms_utf8_string_var_t *v){
    return per_decode_utf8_string(s, v->value, v->max_len); 
}

int cms_octet_string_fixed_encode_stream(per_stream_t *s, const cms_octet_string_fixed_t *v){
    return per_encode_octet_string_fixed(s, v->value, v->fixed_len); 
}
int cms_octet_string_fixed_decode_stream(per_stream_t *s, cms_octet_string_fixed_t *v){
    return per_decode_octet_string_fixed(s, v->value, v->fixed_len); 
}

int cms_octet_string_var_encode_stream(per_stream_t *s, const cms_octet_string_var_t *v){
    return per_encode_octet_string(s, v->value, v->len, v->max_len); 
}
int cms_octet_string_var_decode_stream(per_stream_t *s, cms_octet_string_var_t *v){
    size_t l = 0; 
    int rc = per_decode_octet_string(s, v->value, &l, v->max_len); 
    v->len = (int)l; 
    return rc; 
}

int cms_bit_string_fixed_encode_stream(per_stream_t *s, const cms_bit_string_fixed_t *v){
    return per_encode_bit_string_fixed(s, v->value, v->nbits); 
}
int cms_bit_string_fixed_decode_stream(per_stream_t *s, cms_bit_string_fixed_t *v){
    return per_decode_bit_string_fixed(s, v->value, v->nbits); 
}

int cms_bit_string_var_encode_stream(per_stream_t *s, const cms_bit_string_var_t *v){
    return per_encode_bit_string(s, v->value, v->nbits, v->max_len); 
}
int cms_bit_string_var_decode_stream(per_stream_t *s, cms_bit_string_var_t *v){
    int dn = 0; 
    int rc = per_decode_bit_string(s, v->value, &dn, v->max_len); 
    v->nbits = dn; 
    return rc; 
}

/* ---- public buffer version (struct-based) ---- */

CMS_EXPORT int cms_visible_string_fixed_encode(const cms_visible_string_fixed_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_visible_string_fixed_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_visible_string_fixed_decode(cms_visible_string_fixed_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_visible_string_fixed_decode_stream(&r, v); 
}

CMS_EXPORT int cms_visible_string_var_encode(const cms_visible_string_var_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_visible_string_var_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_visible_string_var_decode(cms_visible_string_var_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_visible_string_var_decode_stream(&r, v); 
}

CMS_EXPORT int cms_utf8_string_fixed_encode(const cms_utf8_string_fixed_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_utf8_string_fixed_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_utf8_string_fixed_decode(cms_utf8_string_fixed_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_utf8_string_fixed_decode_stream(&r, v); 
}

CMS_EXPORT int cms_utf8_string_var_encode(const cms_utf8_string_var_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_utf8_string_var_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_utf8_string_var_decode(cms_utf8_string_var_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_utf8_string_var_decode_stream(&r, v); 
}

CMS_EXPORT int cms_octet_string_fixed_encode(const cms_octet_string_fixed_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_octet_string_fixed_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_octet_string_fixed_decode(cms_octet_string_fixed_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_octet_string_fixed_decode_stream(&r, v); 
}

CMS_EXPORT int cms_octet_string_var_encode(const cms_octet_string_var_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_octet_string_var_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_octet_string_var_decode(cms_octet_string_var_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_octet_string_var_decode_stream(&r, v); 
}

CMS_EXPORT int cms_bit_string_fixed_encode(const cms_bit_string_fixed_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_bit_string_fixed_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_bit_string_fixed_decode(cms_bit_string_fixed_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_bit_string_fixed_decode_stream(&r, v); 
}

CMS_EXPORT int cms_bit_string_var_encode(const cms_bit_string_var_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_bit_string_var_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); 
    return rc; 
}
CMS_EXPORT int cms_bit_string_var_decode(cms_bit_string_var_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_bit_string_var_decode_stream(&r, v); 
}
