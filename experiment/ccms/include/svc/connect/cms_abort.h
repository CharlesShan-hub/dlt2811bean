#ifndef CMS_ABORT_H
#define CMS_ABORT_H

#include "svc/cms_svc.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    uint8_t assoc_id[32];
    int assoc_id_len;
    int reason;
} cms_abort_t;

CMS_EXPORT int cms_abort_encode(
    const cms_abort_t *sdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_abort_decode(
    const uint8_t *in_buf, int in_len,
    cms_abort_t *sdu
);

#ifdef __cplusplus
}
#endif

#endif