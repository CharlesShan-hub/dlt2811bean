#ifndef DATA_BLOCK_CMS_TRIGGER_CONDITIONS_H
#define DATA_BLOCK_CMS_TRIGGER_CONDITIONS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * TriggerConditions (BIT STRING, 6 bits)
 * ============================================================
 */
typedef uint8_t cms_trigger_conditions_t[1];

CMS_EXPORT int cms_trigger_conditions_encode(const cms_trigger_conditions_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_trigger_conditions_decode(const uint8_t *in_buf, int in_len, cms_trigger_conditions_t value);
int cms_trigger_conditions_encode_stream(per_stream_t *s, const cms_trigger_conditions_t value);
int cms_trigger_conditions_decode_stream(per_stream_t *s, cms_trigger_conditions_t value);

#ifdef __cplusplus
}
#endif

#endif
