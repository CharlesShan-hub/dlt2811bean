#ifndef CMS_ABORT_H
#define CMS_ABORT_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

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
