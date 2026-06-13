#ifndef CMS_GET_DATA_DIRECTORY_H
#define CMS_GET_DATA_DIRECTORY_H

#include "svc/cms_svc.h"
#include "svc/data/cms_sub_ref_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetDataDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     dataReference   [0] IMPLICIT ObjectReference,
 *     referenceAfter  [1] IMPLICIT ObjectReference OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *data_reference;
    cms_boolean_t          *ref_after_present;
    cms_object_reference_t *ref_after;
} cms_get_data_directory_request_t;

/*
 * ============================================================
 * GetDataDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     dataAttribute   [0] IMPLICIT SEQUENCE OF SubRefEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *data_attribute; /* SEQUENCE OF SubRefEntry */
    cms_boolean_t   *more_follows;   /* DEFAULT TRUE */
} cms_get_data_directory_response_t;

/*
 * ============================================================
 * GetDataDirectory-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_data_directory_error_t;

CMS_EXPORT int cms_get_data_directory_request_encode(
    const cms_get_data_directory_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_directory_request_decode(
    cms_get_data_directory_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_data_directory_response_encode(
    const cms_get_data_directory_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_directory_response_decode(
    cms_get_data_directory_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_data_directory_error_encode(
    const cms_get_data_directory_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_data_directory_error_decode(
    cms_get_data_directory_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
