#ifndef CMS_GET_LOG_STATUS_VALUES_H
#define CMS_GET_LOG_STATUS_VALUES_H

#include "svc/cms_svc.h"
#include "svc/log/cms_log_status_value_choice.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetLogStatusValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     logReference    [0] IMPLICIT SEQUENCE OF ObjectReference
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_array_t            *log_reference;  /* SEQUENCE OF ObjectReference */
} cms_get_log_status_values_request_t;

/*
 * ============================================================
 * GetLogStatusValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     log             [0] IMPLICIT SEQUENCE OF LogStatusValueChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *log;           /* SEQUENCE OF LogStatusValueChoice */
    cms_boolean_t   *more_follows;  /* DEFAULT TRUE */
} cms_get_log_status_values_response_t;

/*
 * ============================================================
 * GetLogStatusValues-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_log_status_values_error_t;

CMS_EXPORT int cms_get_log_status_values_request_encode(
    const cms_get_log_status_values_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_log_status_values_request_decode(
    cms_get_log_status_values_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_log_status_values_response_encode(
    const cms_get_log_status_values_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_log_status_values_response_decode(
    cms_get_log_status_values_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_log_status_values_error_encode(
    const cms_get_log_status_values_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_log_status_values_error_decode(
    cms_get_log_status_values_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
