#ifndef CMS_COMMON_ENTRY_TIME_H
#define CMS_COMMON_ENTRY_TIME_H

#include "cms_types.h"
#include "data/time/cms_binary_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * EntryTime ::= BinaryTime  —  7.3.9
 * PER encoding: same as BinaryTime (OCTET STRING (SIZE(6)))
 */

typedef cms_binary_time_t cms_entry_time_t;

int cms_entry_time_encode_stream(per_stream_t *s, const void *ptr);
int cms_entry_time_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_entry_time_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_entry_time_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
