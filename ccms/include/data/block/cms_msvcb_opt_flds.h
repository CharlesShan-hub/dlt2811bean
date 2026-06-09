#ifndef CMS_BLOCK_MSVCB_OPT_FLDS_H
#define CMS_BLOCK_MSVCB_OPT_FLDS_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * MsvcbOptFlds ::= BIT STRING { ... } (SIZE(5))  —  7.6.6
 * PER: align + 1 byte (5 bits)
 *
 * Bit layout:
 *   bit 0: refresh-time
 *   bit 1: reserved (always 0, not stored)
 *   bit 2: sample-rate
 *   bit 3: data-set-name
 *   bit 4: security
 */

typedef struct {
    cms_boolean_t *refresh_time;
    cms_boolean_t *sample_rate;
    cms_boolean_t *data_set_name;
    cms_boolean_t *security;
} cms_msvcb_opt_flds_t;

int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr);
int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_msvcb_opt_flds_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
