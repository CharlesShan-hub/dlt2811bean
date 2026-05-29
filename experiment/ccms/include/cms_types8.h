#ifndef CMS_TYPES8_H
#define CMS_TYPES8_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_encode_DataDefinition(
    int choice,
    int64_t int_val,
    const char *str_val,
    const uint8_t *bytes_val, int bytes_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_DataDefinition(
    const uint8_t *in_buf, int in_len,
    int *choice,
    int64_t *int_val,
    char *str_val, int *str_cap,
    uint8_t *bytes_val, int *bytes_cap
);

#ifdef __cplusplus
}
#endif

#endif
