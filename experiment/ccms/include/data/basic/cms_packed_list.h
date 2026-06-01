#ifndef DATA_BASIC_CMS_PACKED_LIST_H
#define DATA_BASIC_CMS_PACKED_LIST_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * PackedList
 * ============================================================
 */
CMS_EXPORT int cms_packed_list_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_packed_list_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap);
int cms_packed_list_encode_stream(per_stream_t *s, const uint8_t *value, int value_len);
int cms_packed_list_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap);

#ifdef __cplusplus
}
#endif

#endif
