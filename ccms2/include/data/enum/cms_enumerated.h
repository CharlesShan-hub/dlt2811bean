#ifndef CMS_ENUM_ENUMERATED_H
#define CMS_ENUM_ENUMERATED_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "data/scalar/cms_int8.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ENUMERATED ::= Int8  —  7.1.6
 * DL/T 2811 约定：枚举值用 Int8 存储。
 * 当备选 > 128 时改用 Int16。
 */

typedef cms_int8_t cms_enumerated_t;

/* 直接转发到 cms_int8_* */
static inline int cms_enumerated_encode_stream(per_stream_t *s, const void *ptr) {
    return cms_int8_encode_stream(s, ptr);
}
static inline int cms_enumerated_decode_stream(per_stream_t *s, void *ptr) {
    return cms_int8_decode_stream(s, ptr);
}
static inline int cms_enumerated_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    return cms_int8_encode(ptr, out_buf, out_len);
}
static inline int cms_enumerated_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    return cms_int8_decode(ptr, in_buf, in_len);
}

#ifdef __cplusplus
}
#endif

#endif
