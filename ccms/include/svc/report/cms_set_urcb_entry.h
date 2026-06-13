#ifndef CMS_SET_URCB_ENTRY_H
#define CMS_SET_URCB_ENTRY_H

#include "cms_types.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetURCBEntry ::= SEQUENCE {
 *     reference   [0] IMPLICIT ObjectReference,
 *     rptID       [1] IMPLICIT VisibleString129 OPTIONAL,
 *     rptEna      [2] IMPLICIT BOOLEAN OPTIONAL,
 *     datSet      [3] IMPLICIT ObjectReference OPTIONAL,
 *     optFlds     [5] IMPLICIT RCBOptFlds OPTIONAL,
 *     bufTm       [6] IMPLICIT INT32U OPTIONAL,
 *     trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,
 *     intgPd      [9] IMPLICIT INT32U OPTIONAL,
 *     gi          [10] IMPLICIT BOOLEAN OPTIONAL,
 *     resv        [13] IMPLICIT BOOLEAN OPTIONAL
 * }
 *
 * Used by SetURCBValues request.
 * ============================================================
 */
typedef struct {
    cms_object_reference_t    *reference;
    cms_boolean_t             *rpt_id_present;
    cms_uint8_array_t         *rpt_id;
    cms_boolean_t             *rpt_ena_present;
    cms_boolean_t             *rpt_ena;
    cms_boolean_t             *dat_set_present;
    cms_object_reference_t    *dat_set;
    cms_boolean_t             *opt_flds_present;
    cms_rcb_opt_flds_t        *opt_flds;
    cms_boolean_t             *buf_tm_present;
    cms_int32u_t              *buf_tm;
    cms_boolean_t             *trg_ops_present;
    cms_trigger_conditions_t  *trg_ops;
    cms_boolean_t             *intg_pd_present;
    cms_int32u_t              *intg_pd;
    cms_boolean_t             *gi_present;
    cms_boolean_t             *gi;
    cms_boolean_t             *resv_present;
    cms_boolean_t             *resv;
} cms_set_urcb_entry_t;

#ifdef __cplusplus
}
#endif

#endif
