#ifndef DATA_BLOCK_CMS_REASON_CODE_H
#define DATA_BLOCK_CMS_REASON_CODE_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReasonCode ::= BIT STRING {
 *     reserved              (0),
 *     data-change           (1),
 *     quality-change        (2),
 *     data-update           (3),
 *     integrity             (4),
 *     general-interrogation (5),
 *     application-trigger   (6)
 * } (SIZE(7))
 * ============================================================
 */
typedef struct {
    cms_boolean_t data_change;
    cms_boolean_t quality_change;
    cms_boolean_t data_update;
    cms_boolean_t integrity;
    cms_boolean_t general_interrogation;
    cms_boolean_t application_trigger;
} cms_reason_code_t;

CMS_EXPORT int cms_reason_code_encode(const cms_reason_code_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_reason_code_decode(cms_reason_code_t *v, const uint8_t *in_buf, int in_len);
int cms_reason_code_encode_stream(per_stream_t *s, const cms_reason_code_t *v);
int cms_reason_code_decode_stream(per_stream_t *s, cms_reason_code_t *v);

#ifdef __cplusplus
}
#endif

#endif
