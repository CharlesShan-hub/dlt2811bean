#ifndef CMS_SET_GO_CB_RESULT_H
#define CMS_SET_GO_CB_RESULT_H

#include "cms_types.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetGoCBResult ::= SEQUENCE {
 *     error       [0] IMPLICIT ServiceError OPTIONAL,
 *     goEna       [1] IMPLICIT ServiceError OPTIONAL,
 *     goID        [2] IMPLICIT ServiceError OPTIONAL,
 *     datSet      [3] IMPLICIT ServiceError OPTIONAL
 * }
 *
 * Used by SetGoCBValues error.
 * ============================================================
 */
typedef struct {
    cms_boolean_t *error_present;
    cms_service_error_t *error;
    cms_boolean_t *go_ena_err_present;
    cms_service_error_t *go_ena_err;
    cms_boolean_t *go_id_err_present;
    cms_service_error_t *go_id_err;
    cms_boolean_t *dat_set_err_present;
    cms_service_error_t *dat_set_err;
} cms_set_go_cb_result_t;

int cms_set_go_cb_result_encode_stream(per_stream_t *s, const cms_set_go_cb_result_t *v);
int cms_set_go_cb_result_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
