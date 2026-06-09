#ifndef CMS_BLOCK_LCB_OPT_FLDS_H
#define CMS_BLOCK_LCB_OPT_FLDS_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * LcbOptFlds ::= BIT STRING (SIZE(1))  —  7.6.5
 * PER: align + 1 byte (1 bit)
 */

typedef struct {
    cms_boolean_t *value;
} cms_lcb_opt_flds_t;

int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr);
int cms_lcb_opt_flds_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_lcb_opt_flds_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
