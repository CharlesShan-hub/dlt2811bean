#ifndef CMS_SET_FILE_H
#define CMS_SET_FILE_H

#include "svc/cms_svc.h"
#include "svc/file/cms_visible_string255.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetFile-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255,
 *     startPosition   [1] IMPLICIT INT32U,
 *     fileData        [2] IMPLICIT OCTET STRING,
 *     endOfFile       [3] IMPLICIT BOOLEAN DEFAULT FALSE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_visible_string255_t *filename;
    cms_int32u_t *start_position;
    cms_uint8_array_t *file_data;
    cms_boolean_t *end_of_file; /* DEFAULT FALSE */
} cms_set_file_request_t;

/*
 * ============================================================
 * SetFile-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_set_file_response_t;

/*
 * ============================================================
 * SetFile-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_set_file_error_t;

CMS_EXPORT int cms_set_file_request_encode(const cms_set_file_request_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_set_file_request_decode(cms_set_file_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_set_file_response_encode(const cms_set_file_response_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_set_file_response_decode(cms_set_file_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_set_file_error_encode(const cms_set_file_error_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_set_file_error_decode(cms_set_file_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
