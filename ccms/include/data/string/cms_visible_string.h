#ifndef CMS_STRING_VISIBLE_STRING_H
#define CMS_STRING_VISIBLE_STRING_H

#include "cms_types.h"
#include "data/string/cms_uint8_array.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * VisibleString ::= VisibleString (SIZE(0..n))
 * PER: constrained int length + align (if n*8 > 16) + 8-bit chars
 *
 * SIZE(n) — fixed size, no length prefix, just align + n chars
 *
 * 结构定义复用 cms_uint8_array_t { uint8_t* value; int32_t len; }
 */

/* Encode a cms_uint8_array_t* as VisibleString with given max_len */
int cms_visible_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_len);
int cms_visible_string_decode_stream(per_stream_t *s, void *ptr, uint32_t max_len);

/* Fixed-size VisibleString — SIZE(N), no length prefix */
int cms_visible_string_encode_stream_fixed(per_stream_t *s, const void *ptr, uint32_t fixed_len);
int cms_visible_string_decode_stream_fixed(per_stream_t *s, void *ptr, uint32_t fixed_len);

/* Buffer-level API (max_len = 129 by default, use for generic VisibleString) */
CMS_EXPORT int cms_visible_string_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_visible_string_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
