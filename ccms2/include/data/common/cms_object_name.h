#ifndef CMS_COMMON_OBJECT_NAME_H
#define CMS_COMMON_OBJECT_NAME_H

#include "cms_types.h"
#include "data/string/cms_uint8_array.h"
#include "data/string/cms_visible_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ObjectName ::= VisibleString (SIZE(0..64))  —  7.3.1
 * 结构复用 cms_uint8_array_t { uint8_t* value; int32_t len; }
 */

#define CMS_OBJECT_NAME_MAX_LEN 64

typedef cms_uint8_array_t cms_object_name_t;

int cms_object_name_encode_stream(per_stream_t *s, const void *ptr);
int cms_object_name_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_object_name_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_name_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
