#ifndef CMS_TYPES1_H
#define CMS_TYPES1_H

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

CMS_EXPORT int cms_int8_encode(
    int8_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int8_decode(
    const uint8_t *in_buf, int in_len,
    int8_t *value
);

CMS_EXPORT int cms_int8u_encode(
    uint8_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int8u_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *value
);

CMS_EXPORT int cms_int16_encode(
    int16_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int16_decode(
    const uint8_t *in_buf, int in_len,
    int16_t *value
);

CMS_EXPORT int cms_int16u_encode(
    uint16_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int16u_decode(
    const uint8_t *in_buf, int in_len,
    uint16_t *value
);

CMS_EXPORT int cms_int24u_encode(
    uint32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int24u_decode(
    const uint8_t *in_buf, int in_len,
    uint32_t *value
);

CMS_EXPORT int cms_int32_encode(
    int32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int32_decode(
    const uint8_t *in_buf, int in_len,
    int32_t *value
);

CMS_EXPORT int cms_int32u_encode(
    uint32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int32u_decode(
    const uint8_t *in_buf, int in_len,
    uint32_t *value
);

CMS_EXPORT int cms_int64_encode(
    int64_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int64_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *value
);

CMS_EXPORT int cms_int64u_encode(
    uint64_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_int64u_decode(
    const uint8_t *in_buf, int in_len,
    uint64_t *value
);

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

CMS_EXPORT int cms_visible_string_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_visible_string_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_utf8_string_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_utf8_string_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_octet_string_encode(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_octet_string_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

CMS_EXPORT int cms_bit_string_encode(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_bit_string_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

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
