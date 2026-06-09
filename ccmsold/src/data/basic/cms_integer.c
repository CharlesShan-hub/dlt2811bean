#include "data/basic/cms_integer.h"

/* ---- internal stream version ---- */

int cms_int8_encode_stream(per_stream_t *s, const cms_int8_t *v){ 
    return per_encode_constrained_int(s, v->value, INT8_MIN, INT8_MAX); 
}
int cms_int8_decode_stream(per_stream_t *s, cms_int8_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, INT8_MIN, INT8_MAX); v->value = (int8_t)t; return rc; 
}

int cms_int8u_encode_stream(per_stream_t *s, const cms_int8u_t *v){ 
    return per_encode_constrained_int(s, v->value, 0, UINT8_MAX); 
}
int cms_int8u_decode_stream(per_stream_t *s, cms_int8u_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, 0, UINT8_MAX); v->value = (uint8_t)t; return rc; 
}

int cms_int16_encode_stream(per_stream_t *s, const cms_int16_t *v){ 
    return per_encode_constrained_int(s, v->value, INT16_MIN, INT16_MAX); 
}
int cms_int16_decode_stream(per_stream_t *s, cms_int16_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, INT16_MIN, INT16_MAX); v->value = (int16_t)t; return rc; 
}

int cms_int16u_encode_stream(per_stream_t *s, const cms_int16u_t *v){ 
    return per_encode_constrained_int(s, v->value, 0, UINT16_MAX); 
}
int cms_int16u_decode_stream(per_stream_t *s, cms_int16u_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, 0, UINT16_MAX); v->value = (uint16_t)t; return rc; 
}

int cms_int24u_encode_stream(per_stream_t *s, const cms_int24u_t *v){ 
    return per_encode_constrained_int(s, v->value, 0, INT24U_MAX); 
}
int cms_int24u_decode_stream(per_stream_t *s, cms_int24u_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, 0, INT24U_MAX); v->value = (uint32_t)t; return rc; 
}

int cms_int32_encode_stream(per_stream_t *s, const cms_int32_t *v){ 
    return per_encode_constrained_int(s, v->value, INT32_MIN, INT32_MAX); 
}
int cms_int32_decode_stream(per_stream_t *s, cms_int32_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, INT32_MIN, INT32_MAX); v->value = (int32_t)t; return rc; 
}

int cms_int32u_encode_stream(per_stream_t *s, const cms_int32u_t *v){ 
    return per_encode_constrained_int(s, v->value, 0, UINT32_MAX); 
}
int cms_int32u_decode_stream(per_stream_t *s, cms_int32u_t *v){ 
    int64_t t; int rc = per_decode_constrained_int(s, &t, 0, UINT32_MAX); v->value = (uint32_t)t; return rc; 
}

int cms_int64_encode_stream(per_stream_t *s, const cms_int64_t *v){ 
    return per_encode_unconstrained_int(s, v->value); 
}
int cms_int64_decode_stream(per_stream_t *s, cms_int64_t *v){ 
    return per_decode_unconstrained_int(s, &v->value); 
}

int cms_int64u_encode_stream(per_stream_t *s, const cms_int64u_t *v){ 
    return per_encode_unconstrained_int(s, (int64_t)v->value); 
}
int cms_int64u_decode_stream(per_stream_t *s, cms_int64u_t *v){ 
    int64_t t; int rc = per_decode_unconstrained_int(s, &t); v->value = (uint64_t)t; return rc; 
}

/* ---- public buffer version ---- */

CMS_EXPORT int cms_int8_encode(const cms_int8_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int8_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int8_decode(cms_int8_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int8_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int8u_encode(const cms_int8u_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int8u_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int8u_decode(cms_int8u_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int8u_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int16_encode(const cms_int16_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int16_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int16_decode(cms_int16_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int16_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int16u_encode(const cms_int16u_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int16u_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int16u_decode(cms_int16u_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int16u_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int24u_encode(const cms_int24u_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int24u_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int24u_decode(cms_int24u_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int24u_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int32_encode(const cms_int32_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int32_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int32_decode(cms_int32_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int32_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int32u_encode(const cms_int32u_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int32u_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int32u_decode(cms_int32u_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int32u_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int64_encode(const cms_int64_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int64_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int64_decode(cms_int64_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int64_decode_stream(&r, v); 
}

CMS_EXPORT int cms_int64u_encode(const cms_int64u_t *v, uint8_t *out_buf, int *out_len){ 
    per_stream_t w = per_stream_new_write(out_buf, (size_t)*out_len); 
    int rc = cms_int64u_encode_stream(&w, v); 
    *out_len = (int)per_stream_bytes_written(&w); return rc; 
}
CMS_EXPORT int cms_int64u_decode(cms_int64u_t *v, const uint8_t *in_buf, int in_len){ 
    per_stream_t r = per_stream_new_read(in_buf, (size_t)in_len); 
    return cms_int64u_decode_stream(&r, v); 
}
