#ifndef CMS_TIME_TIME_QUALITY_H
#define CMS_TIME_TIME_QUALITY_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/string/cms_bit_string.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * TimeQuality ::= BIT STRING {
 *     leap-second-known            (0),
 *     clock-failure                (1),
 *     clock-not-synchronized       (2)
 * } (SIZE(8))  —  7.2.1
 *
 * PER: fixed 8-bit BIT STRING (align + 1 byte)
 */
typedef struct {
    cms_boolean_t *leap_seconds_known;       /* bit 0 */
    cms_boolean_t *clock_failure;            /* bit 1 */
    cms_boolean_t *clock_not_synchronized;   /* bit 2 */
    cms_int32_t   *precision;                /* bits 3-7, 0..31 */
} cms_time_quality_t;

/* Stream-level (internal) */
int cms_time_quality_encode_stream(per_stream_t *s, const void *ptr);
int cms_time_quality_decode_stream(per_stream_t *s, void *ptr);

/* Buffer-level (public API) */
CMS_EXPORT int cms_time_quality_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_time_quality_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
