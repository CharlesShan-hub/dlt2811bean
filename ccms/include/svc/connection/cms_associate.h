#ifndef CMS_ASSOCIATE_H
#define CMS_ASSOCIATE_H

#include "svc/cms_svc.h"
#include "svc/other/cms_association_id.h"
#include "svc/connection/cms_authentication_parameter.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Associate-RequestPDU ::= SEQUENCE {
 *     reqId                       Int16U,
 *     serverAccessPointReference  [0] IMPLICIT VisibleString129 OPTIONAL,
 *     authenticationParameter     [1] IMPLICIT AuthenticationParameter OPTIONAL
 * }
 *
 * VisibleString129 ::= VisibleString (SIZE(0..129))
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_boolean_t *sap_ref_present;
    cms_uint8_array_t *sap_ref;
    cms_boolean_t *auth_param_present;
    cms_authentication_parameter_t *auth_param;
} cms_associate_request_t;

/*
 * ============================================================
 * Associate-ResponsePDU ::= SEQUENCE {
 *     reqId                       Int16U,
 *     associationId               [0] IMPLICIT OCTET STRING (SIZE(0..64)),
 *     serviceError                [1] IMPLICIT ServiceError,
 *     authenticationParameter     [2] IMPLICIT AuthenticationParameter OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_association_id_t *assoc_id;
    cms_service_error_t *service_error;
    cms_boolean_t *auth_param_present;
    cms_authentication_parameter_t *auth_param;
} cms_associate_response_t;

/*
 * ============================================================
 * Associate-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_associate_error_t;

CMS_EXPORT int cms_associate_request_encode(const cms_associate_request_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_associate_request_decode(cms_associate_request_t *pdu, const uint8_t *in_buf, int in_len);

CMS_EXPORT int cms_associate_response_encode(const cms_associate_response_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_associate_response_decode(cms_associate_response_t *pdu, const uint8_t *in_buf, int in_len);

CMS_EXPORT int cms_associate_error_encode(const cms_associate_error_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_associate_error_decode(cms_associate_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
