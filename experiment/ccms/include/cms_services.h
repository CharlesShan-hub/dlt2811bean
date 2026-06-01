#ifndef CMS_SERVICES_H
#define CMS_SERVICES_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== Associate-Request ==================== */
CMS_EXPORT int cms_associate_request_encode(
    int64_t req_id,
    const char *sap_ref,
    int has_auth,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_request_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    char *sap_ref, int *sap_ref_cap,
    int *has_auth
);

/* ==================== Release-Request ==================== */
CMS_EXPORT int cms_release_request_encode(
    int64_t req_id,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_request_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id
);

/* ==================== Abort ==================== */
CMS_EXPORT int cms_abort_encode(
    int64_t req_id,
    int64_t abort_reason,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_abort_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    int64_t *abort_reason
);

#ifdef __cplusplus
}
#endif

#endif
