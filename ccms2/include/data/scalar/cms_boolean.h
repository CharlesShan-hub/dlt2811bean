#ifndef CMS_SCALAR_BOOLEAN_H
#define CMS_SCALAR_BOOLEAN_H

#include "cms_types.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Boolean ::= INTEGER (0..1)  —  7.1.1
 * PER: constrained integer, 1 bit
 * sizeof = 4
 */
typedef struct { int value; } cms_boolean_t;

/* Stream-level (internal, for parent SEQUENCE/CHOICE) */
int cms_boolean_encode_stream(per_stream_t *s, const void *ptr);
int cms_boolean_decode_stream(per_stream_t *s, void *ptr);

/* Buffer-level (public API) */
CMS_EXPORT int cms_boolean_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_boolean_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
