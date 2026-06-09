#ifndef CMS_COMMON_TIME_STAMP_H
#define CMS_COMMON_TIME_STAMP_H

#include "cms_types.h"
#include "data/time/cms_utc_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * TimeStamp ::= UtcTime  —  7.3.4
 * PER encoding: same as UtcTime (OCTET STRING (SIZE(8)))
 */

typedef cms_utc_time_t cms_time_stamp_t;

int cms_time_stamp_encode_stream(per_stream_t *s, const void *ptr);
int cms_time_stamp_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_time_stamp_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_time_stamp_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
