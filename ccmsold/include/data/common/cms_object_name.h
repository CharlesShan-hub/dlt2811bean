#ifndef DATA_COMMON_CMS_OBJECT_NAME_H
#define DATA_COMMON_CMS_OBJECT_NAME_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ObjectName ::= VisibleString (SIZE(0..64))
 * ============================================================
 */
typedef cms_uint8_array_t cms_object_name_t;
#define CMS_OBJECT_NAME_MAX_LEN 64

CMS_EXPORT int cms_object_name_encode(const cms_object_name_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_name_decode(cms_object_name_t *v, const uint8_t *in_buf, int in_len);
int cms_object_name_encode_stream(per_stream_t *s, const cms_object_name_t *v);
int cms_object_name_decode_stream(per_stream_t *s, cms_object_name_t *v);

#ifdef __cplusplus
}
#endif

#endif
