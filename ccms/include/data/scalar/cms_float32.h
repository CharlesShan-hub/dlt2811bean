#ifndef CMS_SCALAR_FLOAT32_H
#define CMS_SCALAR_FLOAT32_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Float32 ::= OCTET STRING (SIZE(4))  —  7.1.4 */
typedef struct {
    uint8_t value[4];
} cms_float32_t;

int cms_float32_encode_stream(per_stream_t *s, const void *ptr);
int cms_float32_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_float32_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_float32_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
