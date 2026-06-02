#ifndef DATA_EXTENDED_CMS_TIME_H
#define DATA_EXTENDED_CMS_TIME_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * UtcTime (SEQUENCE of Int32U + Int24U + TimeQuality)
 * ============================================================
 */
typedef struct {
    uint32_t                     seconds_since_epoch;
    uint32_t                     fraction_of_second;
    uint8_t                      time_quality;
} cms_utc_time_t;

CMS_EXPORT int cms_utc_time_encode(const cms_utc_time_t *t, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utc_time_decode(const uint8_t *in_buf, int in_len, cms_utc_time_t *t);
int cms_utc_time_encode_stream(per_stream_t *s, const cms_utc_time_t *t);
int cms_utc_time_decode_stream(per_stream_t *s, cms_utc_time_t *t);

/* Convenience: encode ms directly (no need for cms_utc_time_t locals) */
static inline int cms_utc_time_encode_ms_stream(per_stream_t *s, int64_t ms) {
    cms_utc_time_t _t;
    uint64_t u = (uint64_t)ms;
    _t.seconds_since_epoch = (uint32_t)(u / 1000);
    _t.fraction_of_second = (uint32_t)(((u % 1000) * 16777216) / 1000);
    _t.time_quality = 0;
    return cms_utc_time_encode_stream(s, &_t);
}
static inline int64_t cms_utc_time_to_ms_from_stream(per_stream_t *s) {
    cms_utc_time_t _t;
    cms_utc_time_decode_stream(s, &_t);
    return (int64_t)_t.seconds_since_epoch * 1000
         + (int64_t)(((uint64_t)_t.fraction_of_second * 1000) / 16777216);
}

/* UtcTime ms helpers: convert between cms_utc_time_t and int64_t milliseconds */
static inline void cms_utc_time_from_ms(cms_utc_time_t *t, int64_t ms) {
    uint64_t u = (uint64_t)ms;
    t->seconds_since_epoch = (uint32_t)(u / 1000);
    t->fraction_of_second = (uint32_t)(((u % 1000) * 16777216) / 1000);
    t->time_quality = 0;
}
static inline int64_t cms_utc_time_to_ms(const cms_utc_time_t *t) {
    return (int64_t)t->seconds_since_epoch * 1000
         + (int64_t)(((uint64_t)t->fraction_of_second * 1000) / 16777216);
}

/*
 * ============================================================
 * BinaryTime (OCTET STRING (SIZE(6)): Int32U msOfDay + Int16U daysSince1984)
 * ============================================================
 */
CMS_EXPORT int cms_binary_time_encode(uint32_t msOfDay, uint16_t daysSince1984, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_binary_time_decode(const uint8_t *in_buf, int in_len, uint32_t *msOfDay, uint16_t *daysSince1984);
int cms_binary_time_encode_stream(per_stream_t *s, uint32_t msOfDay, uint16_t daysSince1984);
int cms_binary_time_decode_stream(per_stream_t *s, uint32_t *msOfDay, uint16_t *daysSince1984);

/*
 * ============================================================
 * TimeQuality (8 bits: bits 0-2 flags, bits 3-7 precision)
 * ============================================================
 */
typedef enum {
      LEAP_SECOND_KNOWN = 0,
      CLOCK_FAILURE     = 1,
      CLOCK_NOT_SYNCED  = 2
  } cms_time_quality_flag_t;

typedef enum {
    TIME_PRECISION_0_BIT  = 0,
    TIME_PRECISION_1_BIT  = 1,
    TIME_PRECISION_2_BIT  = 2,
    TIME_PRECISION_3_BIT  = 3,
    TIME_PRECISION_ILLEGAL_START = 25,
    TIME_PRECISION_ILLEGAL_END   = 30,
    TIME_PRECISION_NOT_SPECIFIED = 31
} cms_time_quality_precision_t;

#define CMS_TIME_QUALITY_PRECISION_SHIFT 3
#define CMS_TIME_QUALITY_PRECISION_MASK  0x1F

/*
 * ============================================================
 * TimeQuality semantic struct
 * ============================================================
 */
typedef struct {
    cms_time_quality_flag_t      tagf;
    cms_time_quality_precision_t precision;
    uint64_t                     fraction;
} cms_time_quality_t;

CMS_EXPORT int cms_time_quality_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_quality_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_time_quality_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_time_quality_decode_stream(per_stream_t *s, uint8_t value[1]);

#ifdef __cplusplus
}
#endif

#endif
