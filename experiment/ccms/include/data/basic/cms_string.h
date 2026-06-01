#ifndef DATA_BASIC_CMS_STRING_H
#define DATA_BASIC_CMS_STRING_H

#include "cms_core.h"

#ifdef __cplusplus
extern "C" {
#endif

/* 7.1.5 VisibleString */
CMS_EXPORT int cms_visible_string_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_visible_string_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

/* 7.1.5 UTF8String */
CMS_EXPORT int cms_utf8_string_encode(
    const char *value,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_utf8_string_decode(
    const uint8_t *in_buf, int in_len,
    char *value, int *value_cap
);

/* 7.1.5 OctetString */
CMS_EXPORT int cms_octet_string_encode(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_octet_string_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

/* 7.1.5 BitString */
CMS_EXPORT int cms_bit_string_encode(
    const uint8_t *value, int value_len,
    uint8_t *out_buf, int *out_len
);
CMS_EXPORT int cms_bit_string_decode(
    const uint8_t *in_buf, int in_len,
    uint8_t *value, int *value_cap
);

#ifdef __cplusplus
}
#endif

#endif