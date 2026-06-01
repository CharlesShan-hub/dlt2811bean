#ifndef CMS_TYPES5_H
#define CMS_TYPES5_H

#include "cms_core.h"

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

CMS_EXPORT int cms_check_encode(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_check_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

CMS_EXPORT int cms_add_cause_encode(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_add_cause_decode(
    const uint8_t *in_buf, int in_len,
    int *value
);

#ifdef __cplusplus
}
#endif

#endif
