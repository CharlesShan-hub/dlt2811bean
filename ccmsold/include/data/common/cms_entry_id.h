#ifndef DATA_COMMON_CMS_ENTRY_ID_H
#define DATA_COMMON_CMS_ENTRY_ID_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * EntryID ::= OCTET STRING (SIZE(8))
 * ============================================================
 */
typedef cms_uint8_array_t cms_entry_id_t;
#define CMS_ENTRY_ID_LEN 8

CMS_EXPORT int cms_entry_id_encode(const cms_entry_id_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_entry_id_decode(cms_entry_id_t *v, const uint8_t *in_buf, int in_len);
int cms_entry_id_encode_stream(per_stream_t *s, const cms_entry_id_t *v);
int cms_entry_id_decode_stream(per_stream_t *s, cms_entry_id_t *v);

#ifdef __cplusplus
}
#endif

#endif
