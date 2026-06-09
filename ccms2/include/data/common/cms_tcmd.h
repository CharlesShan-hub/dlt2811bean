#ifndef CMS_COMMON_TCMD_H
#define CMS_COMMON_TCMD_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Tcmd ::= BIT STRING (SIZE(2))  —  7.3.7
 * PER: constrained integer (0..3), 2 bits
 */

#define CMS_TCMD_RESERVED  0
#define CMS_TCMD_SELECT    1
#define CMS_TCMD_OPERATE   2
#define CMS_TCMD_CANCEL    3

typedef struct { cms_int32_t value; } cms_tcmd_t;

int cms_tcmd_encode_stream(per_stream_t *s, const void *ptr);
int cms_tcmd_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_tcmd_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_tcmd_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
