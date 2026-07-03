#ifndef CMS_DELETE_DATA_SET_H
#define CMS_DELETE_DATA_SET_H

#include "svc/cms_svc.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DeleteDataSet-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     datasetReference    [0] IMPLICIT ObjectReference
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_object_reference_t *dataset_reference;
} cms_delete_data_set_request_t;

/*
 * ============================================================
 * DeleteDataSet-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * (ANNOTATED: Response has no payload besides reqId)
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_delete_data_set_response_t;

/*
 * ============================================================
 * DeleteDataSet-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_delete_data_set_error_t;

CMS_EXPORT int cms_delete_data_set_request_encode(const cms_delete_data_set_request_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_delete_data_set_request_decode(cms_delete_data_set_request_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

CMS_EXPORT int cms_delete_data_set_response_encode(const cms_delete_data_set_response_t *pdu, uint8_t **out_buf,
                                                   size_t *out_len);

CMS_EXPORT int cms_delete_data_set_response_decode(cms_delete_data_set_response_t *pdu, const uint8_t *in_buf,
                                                   int in_len);

CMS_EXPORT int cms_delete_data_set_error_encode(const cms_delete_data_set_error_t *pdu, uint8_t **out_buf,
                                                size_t *out_len);

CMS_EXPORT int cms_delete_data_set_error_decode(cms_delete_data_set_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
