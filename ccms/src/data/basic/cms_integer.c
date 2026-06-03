#include "data/basic/cms_integer.h"

/* ---- internal stream version ---- */

int cms_int8_encode_stream(per_stream_t *s, int8_t value)
    { per_encode_constrained_int(s, value, INT8_MIN, INT8_MAX); return CMS_OK; }
int cms_int8_decode_stream(per_stream_t *s, int8_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, INT8_MIN, INT8_MAX); *value = (int8_t)t; return CMS_OK; }

int cms_int8u_encode_stream(per_stream_t *s, uint8_t value)
    { per_encode_constrained_int(s, value, 0, UINT8_MAX); return CMS_OK; }
int cms_int8u_decode_stream(per_stream_t *s, uint8_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, UINT8_MAX); *value = (uint8_t)t; return CMS_OK; }

int cms_int16_encode_stream(per_stream_t *s, int16_t value)
    { per_encode_constrained_int(s, value, INT16_MIN, INT16_MAX); return CMS_OK; }
int cms_int16_decode_stream(per_stream_t *s, int16_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, INT16_MIN, INT16_MAX); *value = (int16_t)t; return CMS_OK; }

int cms_int16u_encode_stream(per_stream_t *s, uint16_t value)
    { per_encode_constrained_int(s, value, 0, UINT16_MAX); return CMS_OK; }
int cms_int16u_decode_stream(per_stream_t *s, uint16_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, UINT16_MAX); *value = (uint16_t)t; return CMS_OK; }

int cms_int24u_encode_stream(per_stream_t *s, uint32_t value)
    { per_encode_constrained_int(s, value, 0, INT24U_MAX); return CMS_OK; }
int cms_int24u_decode_stream(per_stream_t *s, uint32_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, INT24U_MAX); *value = (uint32_t)t; return CMS_OK; }

int cms_int32_encode_stream(per_stream_t *s, int32_t value)
    { per_encode_constrained_int(s, value, INT32_MIN, INT32_MAX); return CMS_OK; }
int cms_int32_decode_stream(per_stream_t *s, int32_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, INT32_MIN, INT32_MAX); *value = (int32_t)t; return CMS_OK; }

int cms_int32u_encode_stream(per_stream_t *s, uint32_t value)
    { per_encode_constrained_int(s, value, 0, UINT32_MAX); return CMS_OK; }
int cms_int32u_decode_stream(per_stream_t *s, uint32_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, UINT32_MAX); *value = (uint32_t)t; return CMS_OK; }

int cms_int64_encode_stream(per_stream_t *s, int64_t value)
    { per_encode_unconstrained_int(s, value); return CMS_OK; }
int cms_int64_decode_stream(per_stream_t *s, int64_t *value)
    { per_decode_unconstrained_int(s, value); return CMS_OK; }

int cms_int64u_encode_stream(per_stream_t *s, uint64_t value)
    { per_encode_unconstrained_int(s, (int64_t)value); return CMS_OK; }
int cms_int64u_decode_stream(per_stream_t *s, uint64_t *value)
    { int64_t t; per_decode_unconstrained_int(s, &t); *value = (uint64_t)t; return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_int8_encode(int8_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int8_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int8_decode(const uint8_t *in_buf, int in_len, int8_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int8_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int8u_encode(uint8_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int8u_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int8u_decode(const uint8_t *in_buf, int in_len, uint8_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int8u_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int16_encode(int16_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int16_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int16_decode(const uint8_t *in_buf, int in_len, int16_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int16_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int16u_encode(uint16_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int16u_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int16u_decode(const uint8_t *in_buf, int in_len, uint16_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int16u_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int24u_encode(uint32_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int24u_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int24u_decode(const uint8_t *in_buf, int in_len, uint32_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int24u_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int32_encode(int32_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int32_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int32_decode(const uint8_t *in_buf, int in_len, int32_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int32_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int32u_encode(uint32_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int32u_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int32u_decode(const uint8_t *in_buf, int in_len, uint32_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int32u_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int64_encode(int64_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int64_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int64_decode(const uint8_t *in_buf, int in_len, int64_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int64_decode_stream(&r, value); return CMS_OK; }

CMS_EXPORT int cms_int64u_encode(uint64_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_int64u_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_int64u_decode(const uint8_t *in_buf, int in_len, uint64_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_int64u_decode_stream(&r, value); return CMS_OK; }
