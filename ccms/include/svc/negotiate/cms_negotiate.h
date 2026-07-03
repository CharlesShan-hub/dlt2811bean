#ifndef CMS_NEGOTIATE_H
#define CMS_NEGOTIATE_H

#include "svc/cms_svc.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * AssociateNegotiate-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     apduSize        [0] IMPLICIT INT16U,
 *     asduSize        [1] IMPLICIT INT32U,
 *     protocolVersion [2] IMPLICIT INT32U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_int16u_t *apdu_size;
    cms_int32u_t *asdu_size;
    cms_int32u_t *protocol_version;
} cms_negotiate_request_t;

/*
 * ============================================================
 * AssociateNegotiate-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     apduSize        [0] IMPLICIT INT16U,
 *     asduSize        [1] IMPLICIT INT32U,
 *     protocolVersion [2] IMPLICIT INT32U,
 *     modelVersion    [3] IMPLICIT VisibleString
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_int16u_t *apdu_size;
    cms_int32u_t *asdu_size;
    cms_int32u_t *protocol_version;
    cms_uint8_array_t *model_version; /* VisibleString */
} cms_negotiate_response_t;

/*
 * ============================================================
 * AssociateNegotiate-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_negotiate_error_t;

CMS_EXPORT int cms_negotiate_request_encode(const cms_negotiate_request_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_negotiate_request_decode(cms_negotiate_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_negotiate_response_encode(const cms_negotiate_response_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_negotiate_response_decode(cms_negotiate_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_negotiate_error_encode(const cms_negotiate_error_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_negotiate_error_decode(cms_negotiate_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
