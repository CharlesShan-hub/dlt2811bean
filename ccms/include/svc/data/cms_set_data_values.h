#ifndef CMS_SET_DATA_VALUES_H
#define CMS_SET_DATA_VALUES_H

#include "svc/cms_svc.h"
#include "svc/data/cms_data_ref_value_entry.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetDataValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataRefValueEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *data;          /* SEQUENCE OF DataRefValueEntry */
} cms_set_data_values_request_t;

/*
 * ============================================================
 * SetDataValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * (ANNOTATED: Response has no payload besides reqId)
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_set_data_values_response_t;

/*
 * ============================================================
 * SetDataValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF ServiceError
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_array_t           *result;        /* SEQUENCE OF ServiceError */
} cms_set_data_values_error_t;

CMS_EXPORT int cms_set_data_values_request_encode(
    const cms_set_data_values_request_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_data_values_request_decode(
    cms_set_data_values_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_data_values_response_encode(
    const cms_set_data_values_response_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_data_values_response_decode(
    cms_set_data_values_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_data_values_error_encode(
    const cms_set_data_values_error_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_data_values_error_decode(
    cms_set_data_values_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
