#ifndef CMS_DELETE_FILE_H
#define CMS_DELETE_FILE_H

#include "svc/cms_svc.h"
#include "svc/file/cms_visible_string255.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * DeleteFile-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_visible_string255_t *filename;
} cms_delete_file_request_t;

/*
 * ============================================================
 * DeleteFile-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_delete_file_response_t;

/*
 * ============================================================
 * DeleteFile-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_delete_file_error_t;

CMS_EXPORT int cms_delete_file_request_encode(const cms_delete_file_request_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_delete_file_request_decode(cms_delete_file_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_delete_file_response_encode(const cms_delete_file_response_t *pdu, uint8_t **out_buf,
                                               size_t *out_len);
CMS_EXPORT int cms_delete_file_response_decode(cms_delete_file_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_delete_file_error_encode(const cms_delete_file_error_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_delete_file_error_decode(cms_delete_file_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
