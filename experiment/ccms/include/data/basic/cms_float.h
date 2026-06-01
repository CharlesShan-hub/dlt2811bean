#ifndef DATA_BASIC_FLOAT_H
#define DATA_BASIC_FLOAT_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_float32_encode(
    float value,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_float32_decode(
    const uint8_t *in_buf, int in_len,
    float *value
);

CMS_EXPORT int cms_float64_encode(
    double value,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_float64_decode(
    const uint8_t *in_buf, int in_len,
    double *value
);

#ifdef __cplusplus
}
#endif

#endif