#include "data/common/cms_quality.h"

/* ---- internal stream version ---- */

int cms_quality_encode_stream(per_stream_t *s, const uint8_t value[2])
    { per_encode_bit_string_fixed(s, value, 13); return CMS_OK; }
int cms_quality_decode_stream(per_stream_t *s, uint8_t value[2])
    { per_decode_bit_string_fixed(s, value, 13); return CMS_OK; }
int cms_dbpos_encode_stream(per_stream_t *s, cms_dbpos_t value)
    { per_encode_constrained_int(s, value, 0, 3); return CMS_OK; }
int cms_dbpos_decode_stream(per_stream_t *s, cms_dbpos_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 3); *value = (cms_dbpos_t)(int)t; return CMS_OK; }
int cms_tcmd_encode_stream(per_stream_t *s, cms_tcmd_t value)
    { per_encode_constrained_int(s, value, 0, 3); return CMS_OK; }
int cms_tcmd_decode_stream(per_stream_t *s, cms_tcmd_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 3); *value = (cms_tcmd_t)(int)t; return CMS_OK; }
int cms_service_error_encode_stream(per_stream_t *s, cms_service_error_t value)
    { per_encode_constrained_int(s, value, 0, 12); return CMS_OK; }
int cms_service_error_decode_stream(per_stream_t *s, cms_service_error_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 12); *value = (cms_service_error_t)(int)t; return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_quality_encode(const uint8_t v[2], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_quality_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_quality_decode(const uint8_t *b, int l, uint8_t v[2])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_quality_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_dbpos_encode(cms_dbpos_t v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_dbpos_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_dbpos_decode(const uint8_t *b, int l, cms_dbpos_t *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_dbpos_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_tcmd_encode(cms_tcmd_t v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_tcmd_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_tcmd_decode(const uint8_t *b, int l, cms_tcmd_t *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_tcmd_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_service_error_encode(cms_service_error_t v, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_service_error_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_service_error_decode(const uint8_t *b, int l, cms_service_error_t *v)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_service_error_decode_stream(&r, v); return CMS_OK; }
