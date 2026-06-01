#ifndef DATA_BLOCK_CMS_REASON_CODE_H
#define DATA_BLOCK_CMS_REASON_CODE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_reason_code_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_reason_code_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_reason_code_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_reason_code_decode_stream(per_stream_t *s, uint8_t value[1]);

#ifdef __cplusplus
}
#endif

#endif
