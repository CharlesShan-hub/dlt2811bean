#ifndef CMS_SERVICES_H
#define CMS_SERVICES_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== Associate-Request ==================== */
CMS_EXPORT int cms_encode_associate_request(
    int64_t req_id,
    const char *sap_ref,
    int has_auth,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_associate_request(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    char *sap_ref, int *sap_ref_cap,
    int *has_auth
);

/* ==================== Release-Request ==================== */
CMS_EXPORT int cms_encode_release_request(
    int64_t req_id,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_release_request(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id
);

/* ==================== Abort ==================== */
CMS_EXPORT int cms_encode_abort(
    int64_t req_id,
    int64_t abort_reason,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_abort(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id,
    int64_t *abort_reason
);

#ifdef __cplusplus
}
#endif

#endif
