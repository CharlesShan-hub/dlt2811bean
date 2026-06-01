#ifndef CMS_RELEASE_H
#define CMS_RELEASE_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_release_request_encode(
    int64_t req_id,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_release_request_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *req_id
);

#ifdef __cplusplus
}
#endif

#endif
