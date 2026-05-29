#ifndef CMS_TYPES1_H
#define CMS_TYPES1_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_encode_BOOLEAN(
    int value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_BOOLEAN(
    const uint8_t *in_buf, int in_len,
    int *value
);

CMS_EXPORT int cms_encode_Int8(
    int8_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int8(
    const uint8_t *in_buf, int in_len,
    int8_t *value
);

CMS_EXPORT int cms_encode_Int8U(
    uint8_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int8U(
    const uint8_t *in_buf, int in_len,
    uint8_t *value
);

CMS_EXPORT int cms_encode_Int16(
    int16_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int16(
    const uint8_t *in_buf, int in_len,
    int16_t *value
);

CMS_EXPORT int cms_encode_Int16U(
    uint16_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int16U(
    const uint8_t *in_buf, int in_len,
    uint16_t *value
);

CMS_EXPORT int cms_encode_Int24U(
    uint32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int24U(
    const uint8_t *in_buf, int in_len,
    uint32_t *value
);

CMS_EXPORT int cms_encode_Int32(
    int32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int32(
    const uint8_t *in_buf, int in_len,
    int32_t *value
);

CMS_EXPORT int cms_encode_Int32U(
    uint32_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int32U(
    const uint8_t *in_buf, int in_len,
    uint32_t *value
);

CMS_EXPORT int cms_encode_Int64(
    int64_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int64(
    const uint8_t *in_buf, int in_len,
    int64_t *value
);

CMS_EXPORT int cms_encode_Int64U(
    uint64_t value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Int64U(
    const uint8_t *in_buf, int in_len,
    uint64_t *value
);

CMS_EXPORT int cms_encode_Float32(
    float value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Float32(
    const uint8_t *in_buf, int in_len,
    float *value
);

CMS_EXPORT int cms_encode_Float64(
    double value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_Float64(
    const uint8_t *in_buf, int in_len,
    double *value
);

CMS_EXPORT int cms_encode_VisibleString(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_VisibleString(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_encode_UTF8String(
    const char *value,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_UTF8String(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

CMS_EXPORT int cms_encode_OctetString(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_OctetString(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

CMS_EXPORT int cms_encode_BitString(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_BitString(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

CMS_EXPORT int cms_encode_PackedList(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_decode_PackedList(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

#ifdef __cplusplus
}
#endif

#endif
