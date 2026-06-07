#ifndef DATA_EXTENDED_CMS_TIME_H
#define DATA_EXTENDED_CMS_TIME_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * TimeQuality (BIT STRING, 8 bits)
 * ============================================================
 */
typedef struct {
    cms_boolean_t leap_seconds_known;      /* bit 0 — 1 = known */
    cms_boolean_t clock_failure;           /* bit 1 — 1 = failure */
    cms_boolean_t clock_not_synchronized;  /* bit 2 — 1 = not synced */
    cms_int32_t   precision;               /* bits 3-7, 0..31; 31 = not specified */
} cms_time_quality_t;

CMS_EXPORT int cms_time_quality_encode(const cms_time_quality_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_quality_decode(cms_time_quality_t *v, const uint8_t *in_buf, int in_len);
int cms_time_quality_encode_stream(per_stream_t *s, const cms_time_quality_t *v);
int cms_time_quality_decode_stream(per_stream_t *s, cms_time_quality_t *v);

/*
 * ============================================================
 * UtcTime (SEQUENCE of Int32U + Int24U + TimeQuality)
 * ============================================================
 */
typedef struct {
    cms_int32u_t         seconds_since_epoch;
    cms_int24u_t         fraction_of_second;
    cms_time_quality_t   time_quality;
} cms_utc_time_t;

CMS_EXPORT int cms_utc_time_encode(const cms_utc_time_t *t, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utc_time_decode(cms_utc_time_t *t, const uint8_t *in_buf, int in_len);
int cms_utc_time_encode_stream(per_stream_t *s, const cms_utc_time_t *t);
int cms_utc_time_decode_stream(per_stream_t *s, cms_utc_time_t *t);

/*
 * ============================================================
 * BinaryTime (OCTET STRING (SIZE(6)): Int32U msOfDay + Int16U daysSince1984)
 * ============================================================
 */
typedef struct {
    cms_int32u_t   msOfDay;          /* milliseconds since midnight (0..86399999) */
    cms_int16u_t   daysSince1984;    /* days since 1984-01-01 */
} cms_binary_time_t;

CMS_EXPORT int cms_binary_time_encode(const cms_binary_time_t *t, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_binary_time_decode(cms_binary_time_t *t, const uint8_t *in_buf, int in_len);
int cms_binary_time_encode_stream(per_stream_t *s, const cms_binary_time_t *t);
int cms_binary_time_decode_stream(per_stream_t *s, cms_binary_time_t *t);

#ifdef __cplusplus
}
#endif

#endif
