#ifndef CMS_TIME_BINARY_TIME_H
#define CMS_TIME_BINARY_TIME_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int16u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * BinaryTime ::= OCTET STRING (SIZE(6))  —  7.2.2
 *
 * C struct 保留语义字段，编解码时拼成 6 字节 OCTET STRING。
 * PER: 6 bytes aligned (fixed OCTET STRING)
 *
 * 字节布局:
 *   [0..3] msOfDay          (Int32U, big-endian, 0..86399999)
 *   [4..5] daysSince1984    (Int16U, big-endian)
 */
typedef struct {
    cms_int32u_t *msOfDay;
    cms_int16u_t *daysSince1984;
} cms_binary_time_t;

int cms_binary_time_encode_stream(per_stream_t *s, const void *ptr);
int cms_binary_time_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_binary_time_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_binary_time_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
