#ifndef CMS_SET_DATA_SET_VALUES_H
#define CMS_SET_DATA_SET_VALUES_H

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
 * SetDataSetValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     value               [2] IMPLICIT SEQUENCE OF Data
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *dataset_reference;
    cms_boolean_t          *ref_after_present;
    cms_object_reference_t *ref_after;
    cms_array_t            *value;         /* SEQUENCE OF Data */
} cms_set_data_set_values_request_t;

/*
 * ============================================================
 * SetDataSetValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * (ANNOTATED: Response has no payload besides reqId)
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_set_data_set_values_response_t;

/*
 * ============================================================
 * SetDataSetValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF ServiceError
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_array_t           *result;        /* SEQUENCE OF ServiceError */
} cms_set_data_set_values_error_t;

CMS_EXPORT int cms_set_data_set_values_request_encode(
    const cms_set_data_set_values_request_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_data_set_values_request_decode(
    cms_set_data_set_values_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_data_set_values_response_encode(
    const cms_set_data_set_values_response_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_data_set_values_response_decode(
    cms_set_data_set_values_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_data_set_values_error_encode(
    const cms_set_data_set_values_error_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_data_set_values_error_decode(
    cms_set_data_set_values_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
