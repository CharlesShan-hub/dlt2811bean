#ifndef CMS_SET_URCB_RESULT_H
#define CMS_SET_URCB_RESULT_H

#include "cms_types.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetURCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     rptID       [1] IMPLICIT ServiceError OPTIONAL,
 *     rptEna      [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [5] IMPLICIT ServiceError OPTIONAL,
 *     bufTm       [6] IMPLICIT ServiceError OPTIONAL,
 *     trgOps      [8] IMPLICIT ServiceError OPTIONAL,
 *     intgPd      [9] IMPLICIT ServiceError OPTIONAL,
 *     gi          [10] IMPLICIT ServiceError OPTIONAL,
 *     resv        [13] IMPLICIT ServiceError OPTIONAL
 * }
 *
 * Used by SetURCBValues error.
 * ============================================================
 */
typedef struct {
    cms_boolean_t        *error_present;
    cms_service_error_t  *error;
    cms_boolean_t        *rpt_id_err_present;
    cms_service_error_t  *rpt_id_err;
    cms_boolean_t        *rpt_ena_err_present;
    cms_service_error_t  *rpt_ena_err;
    cms_boolean_t        *dat_set_err_present;
    cms_service_error_t  *dat_set_err;
    cms_boolean_t        *opt_flds_err_present;
    cms_service_error_t  *opt_flds_err;
    cms_boolean_t        *buf_tm_err_present;
    cms_service_error_t  *buf_tm_err;
    cms_boolean_t        *trg_ops_err_present;
    cms_service_error_t  *trg_ops_err;
    cms_boolean_t        *intg_pd_err_present;
    cms_service_error_t  *intg_pd_err;
    cms_boolean_t        *gi_err_present;
    cms_service_error_t  *gi_err;
    cms_boolean_t        *resv_err_present;
    cms_service_error_t  *resv_err;
} cms_set_urcb_result_t;

#ifdef __cplusplus
}
#endif

#endif
