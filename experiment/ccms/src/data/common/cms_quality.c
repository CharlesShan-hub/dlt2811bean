#include "data/common/cms_quality.h"
#include "per/cms_integer.h"
#include "per/cms_bit_string.h"

int cms_quality_encode_stream(per_stream_t *s, const uint8_t value[2])
    { per_encode_bit_string_fixed(s, value, 13); return CMS_OK; }
int cms_quality_decode_stream(per_stream_t *s, uint8_t value[2])
    { per_decode_bit_string_fixed(s, value, 13); return CMS_OK; }
int cms_dbpos_encode_stream(per_stream_t *s, int value)
    { per_encode_small_non_negative(s, value); return CMS_OK; }
int cms_dbpos_decode_stream(per_stream_t *s, int *value)
    { uint32_t t; per_decode_small_non_negative(s, &t); *value = (int)t; return CMS_OK; }
int cms_tcmd_encode_stream(per_stream_t *s, int value)
    { per_encode_small_non_negative(s, value); return CMS_OK; }
int cms_tcmd_decode_stream(per_stream_t *s, int *value)
    { uint32_t t; per_decode_small_non_negative(s, &t); *value = (int)t; return CMS_OK; }
int cms_service_error_encode_stream(per_stream_t *s, int value)
    { per_encode_constrained_int(s, value, 0, 12); return CMS_OK; }
int cms_service_error_decode_stream(per_stream_t *s, int *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 12); *value = (int)t; return CMS_OK; }

CMS_EXPORT int cms_quality_encode(const uint8_t v[2], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_quality_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_quality_decode(const uint8_t *b, int l, uint8_t v[2])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_quality_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_dbpos_encode(int v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_dbpos_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_dbpos_decode(const uint8_t *b, int l, int *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_dbpos_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_tcmd_encode(int v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_tcmd_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_tcmd_decode(const uint8_t *b, int l, int *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_tcmd_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_service_error_encode(int v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_service_error_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_service_error_decode(const uint8_t *b, int l, int *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_service_error_decode_stream(&r, v); return CMS_OK; }
