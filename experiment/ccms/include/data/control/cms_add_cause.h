#ifndef DATA_CONTROL_CMS_ADD_CAUSE_H
#define DATA_CONTROL_CMS_ADD_CAUSE_H

#include "cms_core.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

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