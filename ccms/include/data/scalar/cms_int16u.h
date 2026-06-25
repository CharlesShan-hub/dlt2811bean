#ifndef CMS_SCALAR_INT16U_H
#define CMS_SCALAR_INT16U_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int16U ::= INTEGER (0..65535)  —  7.1.2 */
typedef struct { uint16_t value; } cms_int16u_t;

int cms_int16u_encode_stream(per_stream_t *s, const void *ptr);
int cms_int16u_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int16u_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_int16u_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
