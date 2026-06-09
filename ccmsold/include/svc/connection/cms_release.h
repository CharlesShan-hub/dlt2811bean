#ifndef CMS_RELEASE_H
#define CMS_RELEASE_H

#include "svc/cms_svc.h"
#include "svc/other/cms_association_id.h"
#include "data/common/cms_service_error.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Release-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     associationId   [0] IMPLICIT OCTET STRING (SIZE(0..64))
 * }
 * ============================================================
 */
typedef struct {
    cms_int16u_t          req_id;
    cms_association_id_t  assoc_id;
} cms_release_request_t;

/*
 * ============================================================
 * Release-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     associationId   [0] IMPLICIT OCTET STRING (SIZE(0..64)),
 *     serviceError    [1] IMPLICIT ServiceError
 * }
 * ============================================================
 */
typedef struct {
    cms_int16u_t          req_id;
    cms_association_id_t  assoc_id;
    cms_service_error_t   service_error;
} cms_release_response_t;

/*
 * ============================================================
 * Release-ErrorPDU ::= ServiceError
 *
 * PER encoding: reqId + ServiceError
 * ============================================================
 */
typedef struct {
    cms_int16u_t          req_id;
    cms_service_error_t   service_error;
} cms_release_error_t;

CMS_EXPORT int cms_release_request_encode(
    const cms_release_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_request_decode(
    cms_release_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_release_response_encode(
    const cms_release_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_response_decode(
    cms_release_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_release_error_encode(
    const cms_release_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_error_decode(
    cms_release_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
