#ifndef CMS_SET_BRCB_ENTRY_H
#define CMS_SET_BRCB_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/common/cms_entry_id.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int16.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetBRCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     rptID       [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds     [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm       [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd      [9] IMPLICIT INT32U OPTIONAL,
 *     gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *     purgeBuf    [11] IMPLICIT BOOLEAN OPTIONAL,
 *     entryID     [12] IMPLICIT EntryID OPTIONAL,
 *     resvTms     [13] IMPLICIT INT16 OPTIONAL
 * }
 *
 * Used by SetBRCBValues request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t *reference;
    cms_boolean_t *rpt_id_present;
    cms_uint8_array_t *rpt_id;
    cms_boolean_t *rpt_ena_present;
    cms_boolean_t *rpt_ena;
    cms_boolean_t *dat_set_present;
    cms_object_reference_t *dat_set;
    cms_boolean_t *opt_flds_present;
    cms_rcb_opt_flds_t *opt_flds;
    cms_boolean_t *buf_tm_present;
    cms_int32u_t *buf_tm;
    cms_boolean_t *trg_ops_present;
    cms_trigger_conditions_t *trg_ops;
    cms_boolean_t *intg_pd_present;
    cms_int32u_t *intg_pd;
    cms_boolean_t *gi_present;
    cms_boolean_t *gi;
    cms_boolean_t *purge_buf_present;
    cms_boolean_t *purge_buf;
    cms_boolean_t *entry_id_present;
    cms_entry_id_t *entry_id;
    cms_boolean_t *resv_tms_present;
    cms_int16_t *resv_tms;
} cms_set_brcb_entry_t;

int cms_set_brcb_entry_encode_stream(per_stream_t *s, const cms_set_brcb_entry_t *v);
int cms_set_brcb_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
