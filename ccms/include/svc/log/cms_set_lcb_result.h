#ifndef CMS_SET_LCB_RESULT_H
#define CMS_SET_LCB_RESULT_H

#include "cms_types.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetLCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     logEna      [1] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [2] IMPLICIT ServiceError OPTIONAL,
 *     trgOps      [3] IMPLICIT ServiceError OPTIONAL,
 *     intgPd      [4] IMPLICIT ServiceError OPTIONAL,
 *     logRef      [5] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [6] IMPLICIT ServiceError OPTIONAL,
 *     bufTm       [7] IMPLICIT ServiceError OPTIONAL
 * }
 *
 * Used by SetLCBValues error.
 * ============================================================
 */
typedef struct {
    cms_boolean_t        *error_present;
    cms_service_error_t  *error;
    cms_boolean_t        *log_ena_err_present;
    cms_service_error_t  *log_ena_err;
    cms_boolean_t        *dat_set_err_present;
    cms_service_error_t  *dat_set_err;
    cms_boolean_t        *trg_ops_err_present;
    cms_service_error_t  *trg_ops_err;
    cms_boolean_t        *intg_pd_err_present;
    cms_service_error_t  *intg_pd_err;
    cms_boolean_t        *log_ref_err_present;
    cms_service_error_t  *log_ref_err;
    cms_boolean_t        *opt_flds_err_present;
    cms_service_error_t  *opt_flds_err;
    cms_boolean_t        *buf_tm_err_present;
    cms_service_error_t  *buf_tm_err;
} cms_set_lcb_result_t;

int cms_set_lcb_result_encode_stream(per_stream_t *s, const cms_set_lcb_result_t *v);
int cms_set_lcb_result_decode_stream(per_stream_t *s, cms_set_lcb_result_t *v);

#ifdef __cplusplus
}
#endif

#endif
