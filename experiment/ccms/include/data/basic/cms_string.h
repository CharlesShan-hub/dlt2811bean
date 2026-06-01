#ifndef DATA_BASIC_CMS_STRING_H
#define DATA_BASIC_CMS_STRING_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include "per/cms_bit_string.h"

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_visible_string_encode(const char *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_visible_string_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap);
int cms_visible_string_encode_stream(per_stream_t *s, const char *value);
int cms_visible_string_decode_stream(per_stream_t *s, char *value);

CMS_EXPORT int cms_utf8_string_encode(const char *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utf8_string_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap);
int cms_utf8_string_encode_stream(per_stream_t *s, const char *value);
int cms_utf8_string_decode_stream(per_stream_t *s, char *value);

CMS_EXPORT int cms_octet_string_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_octet_string_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap);
int cms_octet_string_encode_stream(per_stream_t *s, const uint8_t *value, int value_len);
int cms_octet_string_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap);

CMS_EXPORT int cms_bit_string_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_bit_string_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap);
int cms_bit_string_encode_stream(per_stream_t *s, const uint8_t *value, int value_len);
int cms_bit_string_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap);

#ifdef __cplusplus
}
#endif

#endif
