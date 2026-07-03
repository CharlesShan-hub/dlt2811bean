#ifndef CMS_CREATE_DATA_SET_H
#define CMS_CREATE_DATA_SET_H

#include "svc/cms_svc.h"
#include "svc/dataset/cms_data_ref_fc_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * CreateDataSet-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference,
 *     referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
 *     memberData          [2] IMPLICIT SEQUENCE OF DataRefFcEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_object_reference_t *dataset_reference;
    cms_boolean_t *ref_after_present;
    cms_object_reference_t *ref_after;
    cms_array_t *member_data; /* SEQUENCE OF DataRefFcEntry */
} cms_create_data_set_request_t;

/*
 * ============================================================
 * CreateDataSet-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * (ANNOTATED: Response has no payload besides reqId)
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_create_data_set_response_t;

/*
 * ============================================================
 * CreateDataSet-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_create_data_set_error_t;

CMS_EXPORT int cms_create_data_set_request_encode(const cms_create_data_set_request_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_create_data_set_request_decode(cms_create_data_set_request_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

CMS_EXPORT int cms_create_data_set_response_encode(const cms_create_data_set_response_t *pdu, uint8_t **out_buf,
                                                   size_t *out_len);

CMS_EXPORT int cms_create_data_set_response_decode(cms_create_data_set_response_t *pdu, const uint8_t *in_buf,
                                                   int in_len);

CMS_EXPORT int cms_create_data_set_error_encode(const cms_create_data_set_error_t *pdu, uint8_t **out_buf,
                                                size_t *out_len);

CMS_EXPORT int cms_create_data_set_error_decode(cms_create_data_set_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
