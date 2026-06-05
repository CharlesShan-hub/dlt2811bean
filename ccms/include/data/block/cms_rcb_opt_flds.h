#ifndef DATA_BLOCK_CMS_RCB_OPT_FLDS_H
#define DATA_BLOCK_CMS_RCB_OPT_FLDS_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RCBOptFlds ::= BIT STRING {
 *     reserved            (0),
 *     sequence-number     (1),
 *     report-time-stamp   (2),
 *     reason-for-inclusion(3),
 *     data-set-name       (4),
 *     data-reference      (5),
 *     buffer-overflow     (6),
 *     entryID             (7),
 *     conf-revision       (8),
 *     segmentation        (9)
 * } (SIZE(10))
 * ============================================================
 */
typedef struct {
    cms_boolean_t sequence_number;
    cms_boolean_t report_time_stamp;
    cms_boolean_t reason_for_inclusion;
    cms_boolean_t data_set_name;
    cms_boolean_t data_reference;
    cms_boolean_t buffer_overflow;
    cms_boolean_t entry_id;
    cms_boolean_t conf_revision;
    cms_boolean_t segmentation;
} cms_rcb_opt_flds_t;

CMS_EXPORT int cms_rcb_opt_flds_encode(const cms_rcb_opt_flds_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_rcb_opt_flds_decode(cms_rcb_opt_flds_t *v, const uint8_t *in_buf, int in_len);
int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const cms_rcb_opt_flds_t *v);
int cms_rcb_opt_flds_decode_stream(per_stream_t *s, cms_rcb_opt_flds_t *v);

#ifdef __cplusplus
}
#endif

#endif
