#ifndef DATA_BASIC_INTEGER_H
#define DATA_BASIC_INTEGER_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Int8
 * ============================================================
 */
typedef struct { int8_t value; } cms_int8_t;

CMS_EXPORT int cms_int8_encode(const cms_int8_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int8_decode(cms_int8_t *v, const uint8_t *in_buf, int in_len);
int cms_int8_encode_stream(per_stream_t *s, const cms_int8_t *v);
int cms_int8_decode_stream(per_stream_t *s, cms_int8_t *v);

/*
 * ============================================================
 * Int8U
 * ============================================================
 */
typedef struct { uint8_t value; } cms_int8u_t;

CMS_EXPORT int cms_int8u_encode(const cms_int8u_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int8u_decode(cms_int8u_t *v, const uint8_t *in_buf, int in_len);
int cms_int8u_encode_stream(per_stream_t *s, const cms_int8u_t *v);
int cms_int8u_decode_stream(per_stream_t *s, cms_int8u_t *v);

/*
 * ============================================================
 * Int16
 * ============================================================
 */
typedef struct { int16_t value; } cms_int16_t;

CMS_EXPORT int cms_int16_encode(const cms_int16_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int16_decode(cms_int16_t *v, const uint8_t *in_buf, int in_len);
int cms_int16_encode_stream(per_stream_t *s, const cms_int16_t *v);
int cms_int16_decode_stream(per_stream_t *s, cms_int16_t *v);

/*
 * ============================================================
 * Int16U
 * ============================================================
 */
typedef struct { uint16_t value; } cms_int16u_t;

CMS_EXPORT int cms_int16u_encode(const cms_int16u_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int16u_decode(cms_int16u_t *v, const uint8_t *in_buf, int in_len);
int cms_int16u_encode_stream(per_stream_t *s, const cms_int16u_t *v);
int cms_int16u_decode_stream(per_stream_t *s, cms_int16u_t *v);

/*
 * ============================================================
 * Int24U
 * ============================================================
 */
#define INT24U_MAX 16777215
typedef struct { uint32_t value; } cms_int24u_t;

CMS_EXPORT int cms_int24u_encode(const cms_int24u_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int24u_decode(cms_int24u_t *v, const uint8_t *in_buf, int in_len);
int cms_int24u_encode_stream(per_stream_t *s, const cms_int24u_t *v);
int cms_int24u_decode_stream(per_stream_t *s, cms_int24u_t *v);

/*
 * ============================================================
 * Int32
 * ============================================================
 */
typedef struct { int32_t value; } cms_int32_t;

CMS_EXPORT int cms_int32_encode(const cms_int32_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int32_decode(cms_int32_t *v, const uint8_t *in_buf, int in_len);
int cms_int32_encode_stream(per_stream_t *s, const cms_int32_t *v);
int cms_int32_decode_stream(per_stream_t *s, cms_int32_t *v);

/*
 * ============================================================
 * Int32U
 * ============================================================
 */
typedef struct { uint32_t value; } cms_int32u_t;

CMS_EXPORT int cms_int32u_encode(const cms_int32u_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int32u_decode(cms_int32u_t *v, const uint8_t *in_buf, int in_len);
int cms_int32u_encode_stream(per_stream_t *s, const cms_int32u_t *v);
int cms_int32u_decode_stream(per_stream_t *s, cms_int32u_t *v);

/*
 * ============================================================
 * Int64
 * ============================================================
 */
typedef struct { int64_t value; } cms_int64_t;

CMS_EXPORT int cms_int64_encode(const cms_int64_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int64_decode(cms_int64_t *v, const uint8_t *in_buf, int in_len);
int cms_int64_encode_stream(per_stream_t *s, const cms_int64_t *v);
int cms_int64_decode_stream(per_stream_t *s, cms_int64_t *v);

/*
 * ============================================================
 * Int64U
 * ============================================================
 */
typedef struct { uint64_t value; } cms_int64u_t;

CMS_EXPORT int cms_int64u_encode(const cms_int64u_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int64u_decode(cms_int64u_t *v, const uint8_t *in_buf, int in_len);
int cms_int64u_encode_stream(per_stream_t *s, const cms_int64u_t *v);
int cms_int64u_decode_stream(per_stream_t *s, cms_int64u_t *v);

#ifdef __cplusplus
}
#endif

#endif
