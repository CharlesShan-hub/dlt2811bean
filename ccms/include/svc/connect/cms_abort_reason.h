#ifndef CMS_ABORT_REASON_H
#define CMS_ABORT_REASON_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * AbortReason — constrained INTEGER (0..5)
 * ============================================================
 */
typedef enum {
    CMS_ABORT_OTHER                        = 0,
    CMS_ABORT_UNRECOGNIZED_SERVICE         = 1,
    CMS_ABORT_INVALID_REQ_ID               = 2,
    CMS_ABORT_INVALID_ARGUMENT             = 3,
    CMS_ABORT_INVALID_RESULT               = 4,
    CMS_ABORT_MAX_SERV_OUTSTANDING_EXCEEDED = 5
} cms_abort_reason_t;

CMS_EXPORT int cms_abort_reason_encode(cms_abort_reason_t value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_abort_reason_decode(const uint8_t *in_buf, int in_len, cms_abort_reason_t *value);

int cms_abort_reason_encode_stream(per_stream_t *s, cms_abort_reason_t value);
int cms_abort_reason_decode_stream(per_stream_t *s, cms_abort_reason_t *value);

#ifdef __cplusplus
}
#endif

#endif
