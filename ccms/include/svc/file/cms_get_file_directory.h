#ifndef CMS_GET_FILE_DIRECTORY_H
#define CMS_GET_FILE_DIRECTORY_H

#include "svc/cms_svc.h"
#include "svc/file/cms_visible_string255.h"
#include "data/common/cms_file_entry.h"
#include "data/common/cms_service_error.h"
#include "data/common/cms_time_stamp.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetFileDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     pathName        [0] IMPLICIT VisibleString255,
 *     startTime       [1] IMPLICIT TimeStamp OPTIONAL,
 *     stopTime        [2] IMPLICIT TimeStamp OPTIONAL,
 *     fileAfter       [3] IMPLICIT VisibleString255 OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t            *req_id;
    cms_visible_string255_t *path_name;
    cms_boolean_t           *start_time_present;
    cms_time_stamp_t        *start_time;
    cms_boolean_t           *stop_time_present;
    cms_time_stamp_t        *stop_time;
    cms_boolean_t           *file_after_present;
    cms_visible_string255_t *file_after;
} cms_get_file_directory_request_t;

/*
 * ============================================================
 * GetFileDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     fileEntry       [0] IMPLICIT SEQUENCE OF FileEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *file_entry;    /* SEQUENCE OF FileEntry */
    cms_boolean_t   *more_follows;  /* DEFAULT TRUE */
} cms_get_file_directory_response_t;

/*
 * ============================================================
 * GetFileDirectory-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_file_directory_error_t;

CMS_EXPORT int cms_get_file_directory_request_encode(const cms_get_file_directory_request_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_get_file_directory_request_decode(cms_get_file_directory_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_file_directory_response_encode(const cms_get_file_directory_response_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_get_file_directory_response_decode(cms_get_file_directory_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_file_directory_error_encode(const cms_get_file_directory_error_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_get_file_directory_error_decode(cms_get_file_directory_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
