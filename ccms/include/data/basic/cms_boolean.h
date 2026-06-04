#ifndef DATA_BASIC_BOOLEAN_H
#define DATA_BASIC_BOOLEAN_H

#include "cms_core.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Boolean
 * ============================================================
 */
typedef struct { int value; } cms_boolean_t;

CMS_EXPORT int cms_boolean_encode(const cms_boolean_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_boolean_decode(cms_boolean_t *v, const uint8_t *in_buf, int in_len);
int cms_boolean_encode_stream(per_stream_t *s, const cms_boolean_t *v);
int cms_boolean_decode_stream(per_stream_t *s, cms_boolean_t *v);

#ifdef __cplusplus
}
#endif

#endif
