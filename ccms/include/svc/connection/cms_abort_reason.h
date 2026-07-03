#ifndef CMS_ABORT_REASON_H
#define CMS_ABORT_REASON_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_int32.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * AbortReason ::= INTEGER {
 *     other                        (0),
 *     unrecognized-service         (1),
 *     invalid-reqID                (2),
 *     invalid-argument             (3),
 *     invalid-result               (4),
 *     max-serv-outstanding-exceeded (5)
 * } (0..5)
 * ============================================================
 */
#define CMS_ABORT_OTHER                         0
#define CMS_ABORT_UNRECOGNIZED_SERVICE          1
#define CMS_ABORT_INVALID_REQ_ID                2
#define CMS_ABORT_INVALID_ARGUMENT              3
#define CMS_ABORT_INVALID_RESULT                4
#define CMS_ABORT_MAX_SERV_OUTSTANDING_EXCEEDED 5

typedef cms_int32_t cms_abort_reason_t;

CMS_EXPORT int cms_abort_reason_encode(const cms_abort_reason_t *v, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_abort_reason_decode(cms_abort_reason_t *v, const uint8_t *in_buf, int in_len);
int cms_abort_reason_encode_stream(per_stream_t *s, const cms_abort_reason_t *v);
int cms_abort_reason_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
