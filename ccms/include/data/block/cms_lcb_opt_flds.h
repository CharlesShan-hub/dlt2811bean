#ifndef DATA_BLOCK_CMS_LCB_OPT_FLDS_H
#define DATA_BLOCK_CMS_LCB_OPT_FLDS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LCBOptFlds ::= BIT STRING (SIZE(1))
 * ============================================================
 */
typedef struct {
    cms_boolean_t value;
} cms_lcb_opt_flds_t;

CMS_EXPORT int cms_lcb_opt_flds_encode(const cms_lcb_opt_flds_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_opt_flds_decode(cms_lcb_opt_flds_t *v, const uint8_t *in_buf, int in_len);
int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const cms_lcb_opt_flds_t *v);
int cms_lcb_opt_flds_decode_stream(per_stream_t *s, cms_lcb_opt_flds_t *v);

#ifdef __cplusplus
}
#endif

#endif
