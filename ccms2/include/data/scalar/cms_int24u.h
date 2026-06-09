#ifndef CMS_SCALAR_INT24U_H
#define CMS_SCALAR_INT24U_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/* Int24U ::= INTEGER (0..16777215)  —  7.1.2
 * PER: constrained integer, 24 bits aligned
 * sizeof = 4 (stored in uint32_t)
 */
#define CMS_INT24U_MAX 16777215
typedef struct { uint32_t value; } cms_int24u_t;

int cms_int24u_encode_stream(per_stream_t *s, const void *ptr);
int cms_int24u_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_int24u_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_int24u_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
