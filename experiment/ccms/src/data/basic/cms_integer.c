#include "data/basic/cms_integer.h"
#include "per/cms_integer.h"
#include "per/cms_stream.h"

/* ---- stream versions ---- */

int cms_int8_encode_stream(per_stream_t *s, int8_t value)
    { per_encode_constrained_int(s, value, -128, 127); return CMS_OK; }
int cms_int8_decode_stream(per_stream_t *s, int8_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, -128, 127); *value = (int8_t)t; return CMS_OK; }

int cms_int8u_encode_stream(per_stream_t *s, uint8_t value)
    { per_encode_constrained_int(s, value, 0, 255); return CMS_OK; }
int cms_int8u_decode_stream(per_stream_t *s, uint8_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 255); *value = (uint8_t)t; return CMS_OK; }

int cms_int16_encode_stream(per_stream_t *s, int16_t value)
    { per_encode_constrained_int(s, value, -32768, 32767); return CMS_OK; }
int cms_int16_decode_stream(per_stream_t *s, int16_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, -32768, 32767); *value = (int16_t)t; return CMS_OK; }

int cms_int16u_encode_stream(per_stream_t *s, uint16_t value)
    { per_encode_constrained_int(s, value, 0, 65535); return CMS_OK; }
int cms_int16u_decode_stream(per_stream_t *s, uint16_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 65535); *value = (uint16_t)t; return CMS_OK; }

int cms_int24u_encode_stream(per_stream_t *s, uint32_t value)
    { per_encode_constrained_int(s, value, 0, 16777215); return CMS_OK; }
int cms_int24u_decode_stream(per_stream_t *s, uint32_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 16777215); *value = (uint32_t)t; return CMS_OK; }

int cms_int32_encode_stream(per_stream_t *s, int32_t value)
    { per_encode_constrained_int(s, value, -2147483648, 2147483647); return CMS_OK; }
int cms_int32_decode_stream(per_stream_t *s, int32_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, -2147483648, 2147483647); *value = (int32_t)t; return CMS_OK; }

int cms_int32u_encode_stream(per_stream_t *s, uint32_t value)
    { per_encode_constrained_int(s, value, 0, 4294967295); return CMS_OK; }
int cms_int32u_decode_stream(per_stream_t *s, uint32_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 4294967295); *value = (uint32_t)t; return CMS_OK; }

int cms_int64_encode_stream(per_stream_t *s, int64_t value)
    { per_encode_unconstrained_int(s, value); return CMS_OK; }
int cms_int64_decode_stream(per_stream_t *s, int64_t *value)
    { per_decode_unconstrained_int(s, value); return CMS_OK; }

int cms_int64u_encode_stream(per_stream_t *s, uint64_t value)
    { per_encode_unconstrained_int(s, (int64_t)value); return CMS_OK; }
int cms_int64u_decode_stream(per_stream_t *s, uint64_t *value)
    { int64_t t; per_decode_unconstrained_int(s, &t); *value = (uint64_t)t; return CMS_OK; }

/* ---- public buffer wrappers ---- */

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
