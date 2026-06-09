#ifndef DATA_BLOCK_CMS_MSVCB_OPT_FLDS_H
#define DATA_BLOCK_CMS_MSVCB_OPT_FLDS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * MSVCBOptFlds ::= BIT STRING {
 *     refresh-time   (0),
 *     reserved       (1),
 *     sample-rate    (2),
 *     data-set-name  (3),
 *     security       (4)
 * } (SIZE(5))
 * ============================================================
 */
typedef struct {
    cms_boolean_t refresh_time;
    cms_boolean_t sample_rate;
    cms_boolean_t data_set_name;
    cms_boolean_t security;
} cms_msvcb_opt_flds_t;

CMS_EXPORT int cms_msvcb_opt_flds_encode(const cms_msvcb_opt_flds_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_opt_flds_decode(cms_msvcb_opt_flds_t *v, const uint8_t *in_buf, int in_len);
int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const cms_msvcb_opt_flds_t *v);
int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, cms_msvcb_opt_flds_t *v);

#ifdef __cplusplus
}
#endif

#endif
