#ifndef CMS_BLOCK_TRIGGER_CONDITIONS_H
#define CMS_BLOCK_TRIGGER_CONDITIONS_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * TriggerConditions ::= BIT STRING { ... } (SIZE(6))  —  7.6.2
 * PER: align + 1 byte (6 bits)
 *
 * Bit layout:
 *   bit 0: reserved (always 0, not stored)
 *   bit 1: data-change
 *   bit 2: quality-change
 *   bit 3: data-update
 *   bit 4: integrity
 *   bit 5: general-interrogation
 */

typedef struct {
    cms_boolean_t *data_change;
    cms_boolean_t *quality_change;
    cms_boolean_t *data_update;
    cms_boolean_t *integrity;
    cms_boolean_t *general_interrogation;
} cms_trigger_conditions_t;

int cms_trigger_conditions_encode_stream(per_stream_t *s, const void *ptr);
int cms_trigger_conditions_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_trigger_conditions_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_trigger_conditions_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
