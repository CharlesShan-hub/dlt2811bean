#ifndef DATA_BASIC_BOOLEAN_H
#define DATA_BASIC_BOOLEAN_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_boolean_encode(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_boolean_decode(
    const uint8_t *in_buf, int in_len,
    int *value
);

#ifdef __cplusplus
}
#endif

#endif