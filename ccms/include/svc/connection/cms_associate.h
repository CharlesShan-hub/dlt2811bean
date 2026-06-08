#ifndef CMS_ASSOCIATE_H
#define CMS_ASSOCIATE_H

#include "svc/cms_svc.h"
#include "svc/other/cms_association_id.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"
#include "per/cms_sequence.h"
#include "data/common/cms_service_error.h"
#include "data/extended/cms_time.h"
#include "svc/connection/cms_authentication_parameter.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_MAX_CERT_LEN 2048
#define CMS_SAP_REF_MAX_LEN 64

typedef struct {
    cms_int16u_t               req_id;
    cms_service_error_t        service_error;
    cms_uint8_array_t          sap_ref;    /* VisibleString (0..64) */
    cms_boolean_t              sap_ref_present;
    cms_authentication_parameter_t auth_param;
    cms_boolean_t              auth_param_present;
} cms_associate_request_t;

typedef struct {
    cms_int16u_t               req_id;
    cms_service_error_t        service_error;
    cms_association_id_t       assoc_id;
    cms_authentication_parameter_t auth_param;
    cms_boolean_t              auth_param_present;
} cms_associate_response_t;

typedef struct {
    cms_int16u_t          req_id;
    cms_service_error_t   service_error;
} cms_associate_error_t;

CMS_EXPORT int cms_associate_request_encode(
    const cms_associate_request_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_request_decode(
    cms_associate_request_t *sdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_associate_response_encode(
    const cms_associate_response_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_response_decode(
    cms_associate_response_t *sdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_associate_error_encode(
    const cms_associate_error_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_error_decode(
    cms_associate_error_t *sdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
