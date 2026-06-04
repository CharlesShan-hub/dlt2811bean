#ifndef DATA_BASIC_FLOAT_H
#define DATA_BASIC_FLOAT_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Float32
 * ============================================================
 */
typedef struct { float value; } cms_float32_t;

CMS_EXPORT int cms_float32_encode(const cms_float32_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_float32_decode(cms_float32_t *v, const uint8_t *in_buf, int in_len);
int cms_float32_encode_stream(per_stream_t *s, const cms_float32_t *v);
int cms_float32_decode_stream(per_stream_t *s, cms_float32_t *v);

/*
 * ============================================================
 * Float64
 * ============================================================
 */
typedef struct { double value; } cms_float64_t;

CMS_EXPORT int cms_float64_encode(const cms_float64_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_float64_decode(cms_float64_t *v, const uint8_t *in_buf, int in_len);
int cms_float64_encode_stream(per_stream_t *s, const cms_float64_t *v);
int cms_float64_decode_stream(per_stream_t *s, cms_float64_t *v);

#ifdef __cplusplus
}
#endif

#endif
