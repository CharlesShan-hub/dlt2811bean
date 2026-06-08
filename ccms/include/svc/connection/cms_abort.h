#ifndef CMS_ABORT_H
#define CMS_ABORT_H

#include "svc/cms_svc.h"
#include "svc/other/cms_association_id.h"
#include "svc/connection/cms_abort_reason.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_int16u_t          req_id;
    cms_association_id_t  assoc_id;
    cms_abort_reason_t    reason;
} cms_abort_t;

CMS_EXPORT int cms_abort_encode(
    const cms_abort_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_abort_decode(
    cms_abort_t *sdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
