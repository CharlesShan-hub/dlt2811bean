#ifndef CMS_ENUM_CODED_ENUM_H
#define CMS_ENUM_CODED_ENUM_H

#include "cms_core.h"
#include "cms_types.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * CODEDENUM ::= BIT STRING (SIZE(0..n))  —  7.1.7
 * 编码方式与 BitString 完全一致。
 * 结构复用 cms2_uint8_array_t { value*, len }，len 表示 bit 数。
 */

typedef cms2_uint8_array_t cms_coded_enum_t;

/* 直接转发到 cms_bit_string_* */
static inline int cms_coded_enum_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_nbits) {
    return cms_bit_string_encode_stream(s, ptr, max_nbits);
}
static inline int cms_coded_enum_decode_stream(per_stream_t *s, void *ptr, uint32_t max_nbits) {
    return cms_bit_string_decode_stream(s, ptr, max_nbits);
}
static inline int cms_coded_enum_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    return cms_bit_string_encode(ptr, out_buf, out_len);
}
static inline int cms_coded_enum_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_bit_string_decode(ptr, in_buf, in_len);
}

#ifdef __cplusplus
}
#endif

#endif
