#ifndef CMS_ASSOCIATE_H
#define CMS_ASSOCIATE_H

#include "svc/cms_svc.h"
#include "svc/other/cms_association_id.h"
#include "per/cms_sequence.h"
#include "data/common/cms_quality.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif

#define CMS_MAX_CERT_LEN 2048

typedef struct {
    char     sap_ref[65];
    int      has_auth;
    uint8_t  cert[CMS_MAX_CERT_LEN];
    int      cert_len;
    int64_t  signed_time;
    uint8_t  sig_val[CMS_MAX_CERT_LEN];
    int      sig_len;
} cms_associate_request_t;

typedef struct {
    cms_association_id_t assoc_id;
    cms_service_error_t  service_error;
    int                  has_auth;
    uint8_t              cert[CMS_MAX_CERT_LEN];
    int                  cert_len;
    int64_t              signed_time;
    uint8_t              sig_val[CMS_MAX_CERT_LEN];
    int                  sig_len;
} cms_associate_response_t;

typedef struct {
    cms_service_error_t service_error;
} cms_associate_error_t;

CMS_EXPORT int cms_associate_request_encode(
    const cms_associate_request_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_request_decode(
    const uint8_t *in_buf, int in_len,
    cms_associate_request_t *sdu
);

CMS_EXPORT int cms_associate_response_encode(
    const cms_associate_response_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_response_decode(
    const uint8_t *in_buf, int in_len,
    cms_associate_response_t *sdu
);

CMS_EXPORT int cms_associate_error_encode(
    const cms_associate_error_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_error_decode(
    const uint8_t *in_buf, int in_len,
    cms_associate_error_t *sdu
);

#ifdef __cplusplus
}
#endif

#endif