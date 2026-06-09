#ifndef CMS_STRING_UTF8_STRING_H
#define CMS_STRING_UTF8_STRING_H

#include "cms_types.h"
#include "data/string/cms_uint8_array.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * UTF8String ::= UTF8String (SIZE(0..n bytes))
 * PER: constrained int length + align + UTF-8 bytes
 *
 * 结构复用 cms_uint8_array_t { uint8_t* value; int32_t len; }
 */

int cms_utf8_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_len);
int cms_utf8_string_decode_stream(per_stream_t *s, void *ptr, uint32_t max_len);

CMS_EXPORT int cms_utf8_string_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utf8_string_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
