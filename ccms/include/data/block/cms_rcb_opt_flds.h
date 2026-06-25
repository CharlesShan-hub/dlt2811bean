#ifndef CMS_BLOCK_RCB_OPT_FLDS_H
#define CMS_BLOCK_RCB_OPT_FLDS_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * RcbOptFlds ::= BIT STRING { ... } (SIZE(10))  —  7.6.4
 * PER: align + 2 bytes (10 bits)
 *
 * Bit layout:
 *   bit 0: reserved (always 0, not stored)
 *   bit 1: sequence-number
 *   bit 2: report-time-stamp
 *   bit 3: reason-for-inclusion
 *   bit 4: data-set-name
 *   bit 5: data-reference
 *   bit 6: buffer-overflow
 *   bit 7: entryID
 *   bit 8: conf-revision
 *   bit 9: segmentation
 */

typedef struct {
    cms_boolean_t *sequence_number;
    cms_boolean_t *report_time_stamp;
    cms_boolean_t *reason_for_inclusion;
    cms_boolean_t *data_set_name;
    cms_boolean_t *data_reference;
    cms_boolean_t *buffer_overflow;
    cms_boolean_t *entry_id;
    cms_boolean_t *conf_revision;
    cms_boolean_t *segmentation;
} cms_rcb_opt_flds_t;

int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr);
int cms_rcb_opt_flds_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_rcb_opt_flds_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_rcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
