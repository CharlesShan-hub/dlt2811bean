#ifndef CMS_SCALAR_FLOAT64_H
#define CMS_SCALAR_FLOAT64_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Float64 ::= OCTET STRING (SIZE(8))  —  7.1.4 */
typedef struct { uint8_t value[8]; } cms_float64_t;

int cms_float64_encode_stream(per_stream_t *s, const void *ptr);
int cms_float64_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_float64_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_float64_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
