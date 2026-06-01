#ifndef DATA_BASIC_CMS_PACKED_LIST_H
#define DATA_BASIC_CMS_PACKED_LIST_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_packed_list_encode(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_packed_list_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

#ifdef __cplusplus
}
#endif

#endif