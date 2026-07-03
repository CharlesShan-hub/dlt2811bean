#ifndef CMS_ENUM_ENUMERATED_H
#define CMS_ENUM_ENUMERATED_H

#include "cms_types.h"
#include "data/scalar/cms_int8.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ENUMERATED ::= Int8  —  7.1.6
 *
 * 虽然是 Int8 编码，但 ENUMERATED 是独立的 C 类型。
 * 当备选 > 128 时改用 Int16。
 */

typedef struct {
    int value;
} cms_enumerated_t;

int cms_enumerated_encode_stream(per_stream_t *s, const void *ptr);
int cms_enumerated_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_enumerated_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_enumerated_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
