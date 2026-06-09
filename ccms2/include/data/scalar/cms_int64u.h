#ifndef CMS_SCALAR_INT64U_H
#define CMS_SCALAR_INT64U_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int64U ::= INTEGER (0..2^64-1)  —  7.1.2 */
typedef struct { uint64_t value; } cms_int64u_t;

int cms_int64u_encode_stream(per_stream_t *s, const void *ptr);
int cms_int64u_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int64u_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int64u_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
