#ifndef CMS_SET_MSVCB_RESULT_H
#define CMS_SET_MSVCB_RESULT_H

#include "cms_types.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetMSVCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     svEna       [1] IMPLICIT ServiceError OPTIONAL,
 *     msvID       [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [3] IMPLICIT ServiceError OPTIONAL,
 *     smpMod      [5] IMPLICIT ServiceError OPTIONAL,
 *     smpRate     [6] IMPLICIT ServiceError OPTIONAL,
 *     optFlds     [7] IMPLICIT ServiceError OPTIONAL
 * }
 *
 * Used by SetMSVCBValues error.
 * ============================================================
 */
typedef struct {
    cms_boolean_t        *error_present;
    cms_service_error_t  *error;
    cms_boolean_t        *sv_ena_err_present;
    cms_service_error_t  *sv_ena_err;
    cms_boolean_t        *msv_id_err_present;
    cms_service_error_t  *msv_id_err;
    cms_boolean_t        *dat_set_err_present;
    cms_service_error_t  *dat_set_err;
    cms_boolean_t        *smp_mod_err_present;
    cms_service_error_t  *smp_mod_err;
    cms_boolean_t        *smp_rate_err_present;
    cms_service_error_t  *smp_rate_err;
    cms_boolean_t        *opt_flds_err_present;
    cms_service_error_t  *opt_flds_err;
} cms_set_msvcb_result_t;

int cms_set_msvcb_result_encode_stream(per_stream_t *s, const cms_set_msvcb_result_t *v);
int cms_set_msvcb_result_decode_stream(per_stream_t *s, cms_set_msvcb_result_t *v);

#ifdef __cplusplus
}
#endif

#endif
