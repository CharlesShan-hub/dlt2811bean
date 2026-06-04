#ifndef DATA_COMMON_CMS_TCMD_H
#define DATA_COMMON_CMS_TCMD_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Tcmd ::= BIT STRING (SIZE(2))
 * ============================================================
 */
#define CMS_TCMD_RESERVED  0
#define CMS_TCMD_SELECT    1
#define CMS_TCMD_OPERATE   2
#define CMS_TCMD_CANCEL    3

typedef struct { cms_int32_t value; } cms_tcmd_t;

CMS_EXPORT int cms_tcmd_encode(const cms_tcmd_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_tcmd_decode(cms_tcmd_t *v, const uint8_t *in_buf, int in_len);
int cms_tcmd_encode_stream(per_stream_t *s, const cms_tcmd_t *v);
int cms_tcmd_decode_stream(per_stream_t *s, cms_tcmd_t *v);

#ifdef __cplusplus
}
#endif

#endif
