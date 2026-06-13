#ifndef CMS_GET_DATA_DEFINITION_H
#define CMS_GET_DATA_DEFINITION_H

#include "svc/cms_svc.h"
#include "svc/data/cms_data_ref_entry.h"
#include "svc/data/cms_data_def_result_entry.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetDataDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataRefEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *data;          /* SEQUENCE OF DataRefEntry */
} cms_get_data_definition_request_t;

/*
 * ============================================================
 * GetDataDefinition-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF DataDefResultEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *data;          /* SEQUENCE OF DataDefResultEntry */
    cms_boolean_t   *more_follows;  /* DEFAULT TRUE */
} cms_get_data_definition_response_t;

/*
 * ============================================================
 * GetDataDefinition-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_data_definition_error_t;

CMS_EXPORT int cms_get_data_definition_request_encode(
    const cms_get_data_definition_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_definition_request_decode(
    cms_get_data_definition_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_data_definition_response_encode(
    const cms_get_data_definition_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_definition_response_decode(
    cms_get_data_definition_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_data_definition_error_encode(
    const cms_get_data_definition_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_definition_error_decode(
    cms_get_data_definition_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
