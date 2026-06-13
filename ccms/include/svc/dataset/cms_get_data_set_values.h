#ifndef CMS_GET_DATA_SET_VALUES_H
#define CMS_GET_DATA_SET_VALUES_H

#include "svc/cms_svc.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetDataSetValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *dataset_reference;
    cms_boolean_t          *ref_after_present;
    cms_object_reference_t *ref_after;
} cms_get_data_set_values_request_t;

/*
 * ============================================================
 * GetDataSetValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     value           [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *value;         /* SEQUENCE OF Data */
    cms_boolean_t   *more_follows;  /* DEFAULT TRUE */
} cms_get_data_set_values_response_t;

/*
 * ============================================================
 * GetDataSetValues-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_data_set_values_error_t;

CMS_EXPORT int cms_get_data_set_values_request_encode(
    const cms_get_data_set_values_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_set_values_request_decode(
    cms_get_data_set_values_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_data_set_values_response_encode(
    const cms_get_data_set_values_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_set_values_response_decode(
    cms_get_data_set_values_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_data_set_values_error_encode(
    const cms_get_data_set_values_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_set_values_error_decode(
    cms_get_data_set_values_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
