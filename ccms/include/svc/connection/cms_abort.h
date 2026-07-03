#ifndef CMS_ABORT_H
#define CMS_ABORT_H

#include "svc/cms_svc.h"
#include "svc/other/cms_association_id.h"
#include "svc/connection/cms_abort_reason.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Abort-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     associationId   [0] IMPLICIT OCTET STRING (SIZE(0..64)),
 *     reason          [1] IMPLICIT INTEGER {
 *         other                       (0),
 *         unrecognized-service        (1),
 *         invalid-reqID               (2),
 *         invalid-argument            (3),
 *         invalid-result              (4),
 *         max-serv-outstanding-exceeded (5)
 *     } (0..5)
 * }
 *
 * Note: Abort has no Response or Error PDU — it is a one-way message.
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_association_id_t *assoc_id;
    cms_abort_reason_t *reason;
} cms_abort_t;

CMS_EXPORT int cms_abort_encode(const cms_abort_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_abort_decode(cms_abort_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
