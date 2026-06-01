#ifndef DATA_BLOCK_CMS_TRIGGER_CONDITIONS_H
#define DATA_BLOCK_CMS_TRIGGER_CONDITIONS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * ============================================================
 * TriggerConditions
 * ============================================================
 */
typedef enum {
    CMS_TRIGGER_RESERVED              = 0, /* bit 0 */
    CMS_TRIGGER_DATA_CHANGE           = 1, /* bit 1 */
    CMS_TRIGGER_QUALITY_CHANGE        = 2, /* bit 2 */
    CMS_TRIGGER_DATA_UPDATE           = 3, /* bit 3 */
    CMS_TRIGGER_INTEGRITY             = 4, /* bit 4 */
    CMS_TRIGGER_GENERAL_INTERROGATION = 5  /* bit 5 */
} cms_trigger_condition_t;

CMS_EXPORT int cms_trigger_conditions_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_trigger_conditions_decode(const uint8_t *in_buf, int in_len, uint8_t value[1]);
int cms_trigger_conditions_encode_stream(per_stream_t *s, const uint8_t value[1]);
int cms_trigger_conditions_decode_stream(per_stream_t *s, uint8_t value[1]);

#ifdef __cplusplus
}
#endif

#endif
