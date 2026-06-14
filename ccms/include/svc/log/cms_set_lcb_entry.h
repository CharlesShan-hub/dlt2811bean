#ifndef CMS_SET_LCB_ENTRY_H
#define CMS_SET_LCB_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_lcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetLCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     logEna      [1] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet      [2] IMPLICIT ObjectReference OPTIONAL,
 *     trgOps      [3] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd      [4] IMPLICIT INT32U OPTIONAL,
 *     logRef      [5] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds     [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm       [7] IMPLICIT INT32U OPTIONAL
 * }
 *
 * Used by SetLCBValues request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t    *reference;
    cms_boolean_t             *log_ena_present;
    cms_boolean_t             *log_ena;
    cms_boolean_t             *dat_set_present;
    cms_object_reference_t    *dat_set;
    cms_boolean_t             *trg_ops_present;
    cms_trigger_conditions_t  *trg_ops;
    cms_boolean_t             *intg_pd_present;
    cms_int32u_t              *intg_pd;
    cms_boolean_t             *log_ref_present;
    cms_object_reference_t    *log_ref;
    cms_boolean_t             *opt_flds_present;
    cms_lcb_opt_flds_t        *opt_flds;
    cms_boolean_t             *buf_tm_present;
    cms_int32u_t              *buf_tm;
} cms_set_lcb_entry_t;

int cms_set_lcb_entry_encode_stream(per_stream_t *s, const cms_set_lcb_entry_t *v);
int cms_set_lcb_entry_decode_stream(per_stream_t *s, cms_set_lcb_entry_t *v);

#ifdef __cplusplus
}
#endif

#endif
