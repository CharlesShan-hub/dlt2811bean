#ifndef DATA_BASIC_BOOLEAN_H
#define DATA_BASIC_BOOLEAN_H

#include "cms_core.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_boolean_encode(int value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_boolean_decode(const uint8_t *in_buf, int in_len, int *value);
int cms_boolean_encode_stream(per_stream_t *s, int value);
int cms_boolean_decode_stream(per_stream_t *s, int *value);

#ifdef __cplusplus
}
#endif

#endif
