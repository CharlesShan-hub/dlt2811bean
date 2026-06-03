#ifndef DATA_BASIC_CMS_STRING_H
#define DATA_BASIC_CMS_STRING_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * VisibleString
 * ============================================================
 * size_len > 0  -> fixed mode (no length prefix)
 * size_len == 0 -> variable mode (constrained int length prefix, max_len = ub)
 */
CMS_EXPORT int cms_visible_string_encode(const char *value, int size_len, int max_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_visible_string_decode(const uint8_t *in_buf, int in_len, int size_len, int max_len, char *value, int *value_cap);
int cms_visible_string_encode_stream(per_stream_t *s, const char *value, int max_len);
int cms_visible_string_decode_stream(per_stream_t *s, char *value, int max_len);
int cms_visible_string_encode_stream_fixed(per_stream_t *s, const char *value, int fixed_len);
int cms_visible_string_decode_stream_fixed(per_stream_t *s, char *value, int fixed_len);

/*
 * ============================================================
 * UTF8String
 * ============================================================
 * size_len > 0  -> fixed mode (no length prefix)
 * size_len == 0 -> variable mode (constrained int length prefix, max_len = ub)
 */
CMS_EXPORT int cms_utf8_string_encode(const char *value, int size_len, int max_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_utf8_string_decode(const uint8_t *in_buf, int in_len, int size_len, int max_len, char *value, int *value_cap);
int cms_utf8_string_encode_stream(per_stream_t *s, const char *value, int max_len);
int cms_utf8_string_decode_stream(per_stream_t *s, char *value, int max_len);
int cms_utf8_string_encode_stream_fixed(per_stream_t *s, const char *value, int fixed_len);
int cms_utf8_string_decode_stream_fixed(per_stream_t *s, char *value, int fixed_len);

/*
 * ============================================================
 * OctetString
 * ============================================================
 * size_len > 0  -> fixed mode, encode exactly size_len bytes (no length prefix)
 * size_len == 0 -> variable mode, constrained int length prefix, max_len = ub
 */
CMS_EXPORT int cms_octet_string_encode(const uint8_t *value, int value_len, int size_len, int max_len, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_octet_string_decode(const uint8_t *in_buf, int in_len, int size_len, int max_len, uint8_t *value, int *value_cap);
int cms_octet_string_encode_stream(per_stream_t *s, const uint8_t *value, int value_len, int max_len);
int cms_octet_string_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap, int max_len);
int cms_octet_string_encode_stream_fixed(per_stream_t *s, const uint8_t *value, int fixed_len);
int cms_octet_string_decode_stream_fixed(per_stream_t *s, uint8_t *value, int fixed_len);

/*
 * ============================================================
 * BitString
 * ============================================================
 * nbits > 0 && max_nbits == 0 -> fixed mode, encode exactly nbits bits
 * nbits > 0 && max_nbits > 0  -> variable mode, encode nbits of max_nbits
 */
CMS_EXPORT int cms_bit_string_encode(const uint8_t *value, int nbits, int max_nbits, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_bit_string_decode(const uint8_t *in_buf, int in_len, int nbits, int max_nbits, uint8_t *value, int *value_cap);
int cms_bit_string_encode_stream(per_stream_t *s, const uint8_t *value, int value_len, int max_len);
int cms_bit_string_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap, int max_len);
int cms_bit_string_encode_stream_fixed(per_stream_t *s, const uint8_t *value, int fixed_nbits);
int cms_bit_string_decode_stream_fixed(per_stream_t *s, uint8_t *value, int fixed_nbits);

#ifdef __cplusplus
}
#endif

#endif
