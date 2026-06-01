#ifndef DATA_COMMON_CMS_TIME_STAMP_H
#define DATA_COMMON_CMS_TIME_STAMP_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * ============================================================
 * TimeStamp
 * ============================================================
 */
CMS_EXPORT int cms_time_stamp_encode(int64_t seconds_since_epoch, int64_t fractional, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_stamp_decode(const uint8_t *in_buf, int in_len, int64_t *seconds_since_epoch, int64_t *fractional);
int cms_time_stamp_encode_stream(per_stream_t *s, int64_t seconds_since_epoch, int64_t fractional);
int cms_time_stamp_decode_stream(per_stream_t *s, int64_t *seconds_since_epoch, int64_t *fractional);
/*
 * ============================================================
 * EntryID
 * ============================================================
 */
CMS_EXPORT int cms_entry_id_encode(const uint8_t value[8], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_entry_id_decode(const uint8_t *in_buf, int in_len, uint8_t value[8]);
int cms_entry_id_encode_stream(per_stream_t *s, const uint8_t value[8]);
int cms_entry_id_decode_stream(per_stream_t *s, uint8_t value[8]);

#ifdef __cplusplus
}
#endif

#endif
