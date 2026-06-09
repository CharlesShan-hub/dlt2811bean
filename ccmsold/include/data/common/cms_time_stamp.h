#ifndef DATA_COMMON_CMS_TIME_STAMP_H
#define DATA_COMMON_CMS_TIME_STAMP_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * ============================================================
 * TimeStamp (alias for UtcTime)
 * ============================================================
 */
typedef cms_utc_time_t cms_time_stamp_t;

CMS_EXPORT int cms_time_stamp_encode(const cms_time_stamp_t *t, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_stamp_decode(cms_time_stamp_t *t, const uint8_t *in_buf, int in_len);
int cms_time_stamp_encode_stream(per_stream_t *s, const cms_time_stamp_t *t);
int cms_time_stamp_decode_stream(per_stream_t *s, cms_time_stamp_t *t);

#ifdef __cplusplus
}
#endif

#endif
