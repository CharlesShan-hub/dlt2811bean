#ifndef CMS_CONTROL_CHECK_H
#define CMS_CONTROL_CHECK_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Check ::= BIT STRING (SIZE(2))  —  7.5.3
 * PER: align + 1 byte (2 bits)
 *
 * Bit layout:
 *   bit 0: syncheck
 *   bit 1: interlock_check
 *
 * All-pointer layout (sizeof = 2 * 8 = 16):
 *   [0] syncheck         → cms_boolean_t*
 *   [8] interlock_check  → cms_boolean_t*
 */

typedef struct {
    cms_boolean_t *syncheck;
    cms_boolean_t *interlock_check;
} cms_check_t;

int cms_check_encode_stream(per_stream_t *s, const void *ptr);
int cms_check_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_check_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_check_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
