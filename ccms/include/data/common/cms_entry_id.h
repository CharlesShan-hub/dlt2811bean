#ifndef CMS_COMMON_ENTRY_ID_H
#define CMS_COMMON_ENTRY_ID_H

#include "cms_types.h"
#include "data/string/cms_octet_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * EntryID ::= OCTET STRING (SIZE(8))  —  7.3.8
 * 固定 8 字节，结构复用 cms_uint8_array_t。
 * 内部 value 指向 8 字节数据，len=8。
 */

#define CMS_ENTRY_ID_LEN 8

typedef cms_uint8_array_t cms_entry_id_t;

int cms_entry_id_encode_stream(per_stream_t *s, const void *ptr);
int cms_entry_id_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_entry_id_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_entry_id_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
