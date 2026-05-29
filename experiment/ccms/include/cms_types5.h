#ifndef CMS_TYPES5_H
#define CMS_TYPES5_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_encode_Originator(
    int or_cat,
    const uint8_t *or_ident, int or_ident_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Originator(
    const uint8_t *in_buf, int in_len,
    int *or_cat,
    uint8_t *or_ident, int *or_ident_cap
);

CMS_EXPORT int cms_encode_Check(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Check(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

CMS_EXPORT int cms_encode_AddCause(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_AddCause(
    const uint8_t *in_buf, int in_len,
    int *value
);

#ifdef __cplusplus
}
#endif

#endif
