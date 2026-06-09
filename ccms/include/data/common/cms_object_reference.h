#ifndef CMS_COMMON_OBJECT_REFERENCE_H
#define CMS_COMMON_OBJECT_REFERENCE_H

#include "cms_types.h"
#include "data/string/cms_uint8_array.h"
#include "data/string/cms_visible_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ObjectReference ::= VisibleString (SIZE(0..129))  —  7.3.2
 * 结构复用 cms_uint8_array_t { uint8_t* value; int32_t len; }
 */

#define CMS_OBJECT_REFERENCE_MAX_LEN 129

typedef cms_uint8_array_t cms_object_reference_t;

int cms_object_reference_encode_stream(per_stream_t *s, const void *ptr);
int cms_object_reference_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_object_reference_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_object_reference_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
