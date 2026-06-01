#ifndef DATA_BLOCK_CMS_REASON_CODE_H
#define DATA_BLOCK_CMS_REASON_CODE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReasonCode
 * ============================================================
 */
typedef enum {
    CMS_REASON_RESERVED                = 0, /* bit 0 */
    CMS_REASON_DATA_CHANGE             = 1, /* bit 1 */
    CMS_REASON_QUALITY_CHANGE          = 2, /* bit 2 */
    CMS_REASON_DATA_UPDATE             = 3, /* bit 3 */
    CMS_REASON_INTEGRITY               = 4, /* bit 4 */
    CMS_REASON_GENERAL_INTERROGATION   = 5, /* bit 5 */
    CMS_REASON_APPLICATION_TRIGGER     = 6  /* bit 6 */
} cms_reason_code_t;

CMS_EXPORT int cms_reason_code_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_reason_code_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_reason_code_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_reason_code_decode_stream(per_stream_t *s, uint8_t value[1]);

#ifdef __cplusplus
}
#endif

#endif
