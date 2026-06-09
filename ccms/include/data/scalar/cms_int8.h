#ifndef CMS_SCALAR_INT8_H
#define CMS_SCALAR_INT8_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int8 ::= INTEGER (-128..127)  —  7.1.2 */
typedef struct { int8_t value; } cms_int8_t;

int cms_int8_encode_stream(per_stream_t *s, const void *ptr);
int cms_int8_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int8_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int8_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
