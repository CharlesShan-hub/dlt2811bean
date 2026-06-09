#ifndef CMS_GET_LOGICAL_DEVICE_DIRECTORY_H
#define CMS_GET_LOGICAL_DEVICE_DIRECTORY_H

#include "svc/cms_svc.h"
#include "data/basic/cms_boolean.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_sub_reference.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     ldName          [0] IMPLICIT ObjectName OPTIONAL,
 *     referenceAfter  [1] IMPLICIT ObjectReference OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_int16u_t           req_id;
    cms_boolean_t          ld_name_present;
    cms_object_name_t      ld_name;         /* VisibleString (SIZE(0..64)) */
    cms_boolean_t          ref_after_present;
    cms_object_reference_t ref_after;
} cms_get_logical_device_directory_request_t;

/*
 * ============================================================
 * GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     lnReference     [0] IMPLICIT SEQUENCE OF SubReference,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_int16u_t                 req_id;
    cms_sub_reference_array_t    ln_reference;    /* SEQUENCE OF SubReference */
    cms_boolean_t                more_follows;    /* DEFAULT TRUE */
} cms_get_logical_device_directory_response_t;

/*
 * ============================================================
 * GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError
 *
 * PER encoding: reqId + ServiceError
 * ============================================================
 */
typedef struct {
    cms_int16u_t          req_id;
    cms_service_error_t   service_error;
} cms_get_logical_device_directory_error_t;

CMS_EXPORT int cms_get_logical_device_directory_request_encode(
    const cms_get_logical_device_directory_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_logical_device_directory_request_decode(
    cms_get_logical_device_directory_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_logical_device_directory_response_encode(
    const cms_get_logical_device_directory_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_logical_device_directory_response_decode(
    cms_get_logical_device_directory_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_logical_device_directory_error_encode(
    const cms_get_logical_device_directory_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_get_logical_device_directory_error_decode(
    cms_get_logical_device_directory_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
