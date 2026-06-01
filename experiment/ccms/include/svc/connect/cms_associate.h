#ifndef CMS_ASSOCIATE_H
#define CMS_ASSOCIATE_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_associate_request_encode(
    const char *sap_ref,
    int has_auth,
    const uint8_t *cert, int cert_len,
    int64_t signed_time,
    const uint8_t *sig_val, int sig_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_associate_request_decode(
    const uint8_t *in_buf, int in_len,
    char *sap_ref, int *sap_ref_cap,
    int *has_auth,
    uint8_t *cert, int *cert_cap,
    int64_t *signed_time,
    uint8_t *sig_val, int *sig_val_cap
);

#ifdef __cplusplus
}
#endif

#endif
