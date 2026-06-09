#ifndef CMS_SCALAR_INT8U_H
#define CMS_SCALAR_INT8U_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int8U ::= INTEGER (0..255)  —  7.1.2 */
typedef struct { uint8_t value; } cms_int8u_t;

int cms_int8u_encode_stream(per_stream_t *s, const void *ptr);
int cms_int8u_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int8u_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int8u_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
