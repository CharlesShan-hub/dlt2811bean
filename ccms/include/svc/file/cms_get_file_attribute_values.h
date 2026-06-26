#ifndef CMS_GET_FILE_ATTRIBUTE_VALUES_H
#define CMS_GET_FILE_ATTRIBUTE_VALUES_H

#include "svc/cms_svc.h"
#include "svc/file/cms_visible_string255.h"
#include "data/common/cms_file_entry.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetFileAttributeValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     filename        [0] IMPLICIT VisibleString255
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t            *req_id;
    cms_visible_string255_t *filename;
} cms_get_file_attribute_values_request_t;

/*
 * ============================================================
 * GetFileAttributeValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     fileEntry       [0] IMPLICIT FileEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_file_entry_t *file_entry;
} cms_get_file_attribute_values_response_t;

/*
 * ============================================================
 * GetFileAttributeValues-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_file_attribute_values_error_t;

CMS_EXPORT int cms_get_file_attribute_values_request_encode(const cms_get_file_attribute_values_request_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_file_attribute_values_request_decode(cms_get_file_attribute_values_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_file_attribute_values_response_encode(const cms_get_file_attribute_values_response_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_file_attribute_values_response_decode(cms_get_file_attribute_values_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_file_attribute_values_error_encode(const cms_get_file_attribute_values_error_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_file_attribute_values_error_decode(cms_get_file_attribute_values_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
