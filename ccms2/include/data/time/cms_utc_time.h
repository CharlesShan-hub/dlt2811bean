#ifndef CMS_TIME_UTC_TIME_H
#define CMS_TIME_UTC_TIME_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int24u.h"
#include "data/time/cms_time_quality.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * UtcTime ::= OCTET STRING (SIZE(8))  —  7.2.1
 *
 * C struct 保留语义字段，编解码时拼成 8 字节 OCTET STRING。
 * PER: 8 bytes aligned (fixed OCTET STRING)
 *
 * 字节布局:
 *   [0..3] seconds_since_epoch   (Int32U, big-endian)
 *   [4..6] fraction_of_second    (Int24U, big-endian)
 *   [7]    time_quality          (packed 8-bit BIT STRING)
 */
typedef struct {
    cms_int32u_t       *seconds_since_epoch;
    cms_int24u_t       *fraction_of_second;
    cms_time_quality_t *time_quality;
} cms_utc_time_t;

int cms_utc_time_encode_stream(per_stream_t *s, const void *ptr);
int cms_utc_time_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_utc_time_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utc_time_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
