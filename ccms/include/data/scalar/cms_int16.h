#ifndef CMS_SCALAR_INT16_H
#define CMS_SCALAR_INT16_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int16 ::= INTEGER (-32768..32767)  —  7.1.2 */
typedef struct { int16_t value; } cms_int16_t;

int cms_int16_encode_stream(per_stream_t *s, const void *ptr);
int cms_int16_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int16_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_int16_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
