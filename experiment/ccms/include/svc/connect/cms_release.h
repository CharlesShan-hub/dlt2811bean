#ifndef CMS_RELEASE_H
#define CMS_RELEASE_H

#include "svc/cms_svc.h"
#include "data/basic/cms_string.h"
#include "data/common/cms_quality.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    uint8_t assoc_id[32];
    int assoc_id_len;
} cms_release_request_t;

typedef struct {
    uint8_t assoc_id[32];
    int assoc_id_len;
    cms_service_error_t service_error;
} cms_release_response_t;

typedef struct {
    cms_service_error_t service_error;
} cms_release_error_t;

CMS_EXPORT int cms_release_request_encode(
    const cms_release_request_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_request_decode(
    const uint8_t *in_buf, int in_len,
    cms_release_request_t *sdu
);

CMS_EXPORT int cms_release_response_encode(
    const cms_release_response_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_response_decode(
    const uint8_t *in_buf, int in_len,
    cms_release_response_t *sdu
);

CMS_EXPORT int cms_release_error_encode(
    const cms_release_error_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_error_decode(
    const uint8_t *in_buf, int in_len,
    cms_release_error_t *sdu
);

#ifdef __cplusplus
}
#endif

#endif