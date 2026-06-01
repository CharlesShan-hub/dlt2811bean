#ifndef DATA_EXTENDED_CMS_TIME_H
#define DATA_EXTENDED_CMS_TIME_H

#include "cms_core.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_utc_time_encode(int64_t timestamp_ms, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utc_time_decode(const uint8_t *in_buf, int in_len, int64_t *timestamp_ms);
int cms_utc_time_encode_stream(per_stream_t *s, int64_t timestamp_ms);
int cms_utc_time_decode_stream(per_stream_t *s, int64_t *timestamp_ms);

CMS_EXPORT int cms_binary_time_encode(int32_t hour, int32_t minute, int32_t second, int32_t millisecond, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_binary_time_decode(const uint8_t *in_buf, int in_len, int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond);
int cms_binary_time_encode_stream(per_stream_t *s, int32_t hour, int32_t minute, int32_t second, int32_t millisecond);
int cms_binary_time_decode_stream(per_stream_t *s, int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond);

CMS_EXPORT int cms_time_quality_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_quality_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_time_quality_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_time_quality_decode_stream(per_stream_t *s, uint8_t value[1]);

#ifdef __cplusplus
}
#endif

#endif
