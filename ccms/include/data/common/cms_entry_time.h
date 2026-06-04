#ifndef DATA_COMMON_CMS_ENTRY_TIME_H
#define DATA_COMMON_CMS_ENTRY_TIME_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * EntryTime (alias for BinaryTime)
 *
 * ASN.1 definition:
 *   EntryTime ::= BinaryTime
 * ============================================================
 */
CMS_EXPORT int cms_entry_time_encode(const cms_binary_time_t *t, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_entry_time_decode(cms_binary_time_t *t, const uint8_t *in_buf, int in_len);
int cms_entry_time_encode_stream(per_stream_t *s, const cms_binary_time_t *t);
int cms_entry_time_decode_stream(per_stream_t *s, cms_binary_time_t *t);

#ifdef __cplusplus
}
#endif

#endif
