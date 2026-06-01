#ifndef DATA_CONTROL_CMS_ORIGINATOR_H
#define DATA_CONTROL_CMS_ORIGINATOR_H

#include "cms_core.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_originator_encode(
    int or_cat,
    const uint8_t *or_ident, int or_ident_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_originator_decode(
    const uint8_t *in_buf, int in_len,
    int *or_cat,
    uint8_t *or_ident, int *or_ident_cap
);

#ifdef __cplusplus
}
#endif

#endif