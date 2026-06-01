#ifndef DATA_BLOCK_CMS_TRIGGER_CONDITIONS_H
#define DATA_BLOCK_CMS_TRIGGER_CONDITIONS_H

#include "cms_core.h"
#include "per/cms_stream.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_trigger_conditions_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_trigger_conditions_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_trigger_conditions_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_trigger_conditions_decode_stream(per_stream_t *s, uint8_t value[1]);

#ifdef __cplusplus
}
#endif

#endif
