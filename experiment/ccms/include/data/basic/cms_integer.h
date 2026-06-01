#ifndef DATA_BASIC_INTEGER_H
#define DATA_BASIC_INTEGER_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

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

#ifdef __cplusplus
}
#endif

#endif