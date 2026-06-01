#ifndef DATA_CONTROL_CMS_CHECK_H
#define DATA_CONTROL_CMS_CHECK_H

#include "cms_core.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_check_encode(
    const uint8_t value[2],
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_check_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t value[2]
);

#ifdef __cplusplus
}
#endif

#endif