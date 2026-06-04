#ifndef DATA_BLOCK_CMS_REASON_CODE_H
#define DATA_BLOCK_CMS_REASON_CODE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReasonCode (BIT STRING, 7 bits)
 * ============================================================
 */
typedef uint8_t cms_reason_code_t[1];

CMS_EXPORT int cms_reason_code_encode(const cms_reason_code_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_reason_code_decode(const uint8_t *in_buf, int in_len, cms_reason_code_t value);
int cms_reason_code_encode_stream(per_stream_t *s, const cms_reason_code_t value);
int cms_reason_code_decode_stream(per_stream_t *s, cms_reason_code_t value);

#ifdef __cplusplus
}
#endif

#endif
