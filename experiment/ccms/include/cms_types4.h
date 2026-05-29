#ifndef CMS_TYPES4_H
#define CMS_TYPES4_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_encode_FC(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_FC(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

#ifdef __cplusplus
}
#endif

#endif
