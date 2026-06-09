#ifndef CMS_SCALAR_INT32U_H
#define CMS_SCALAR_INT32U_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int32U ::= INTEGER (0..4294967295)  —  7.1.2 */
typedef struct { uint32_t value; } cms_int32u_t;

int cms_int32u_encode_stream(per_stream_t *s, const void *ptr);
int cms_int32u_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int32u_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int32u_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
