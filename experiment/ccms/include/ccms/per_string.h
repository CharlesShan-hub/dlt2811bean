#ifndef CMSPER_STRING_H
#define CMSPER_STRING_H

#include "per_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* OctetString: fixed length (align + bytes) */
per_error_t per_encode_octet_string_fixed(per_stream_t *s, const uint8_t *data, size_t fixed_len);
per_error_t per_decode_octet_string_fixed(per_stream_t *s, uint8_t *out, size_t fixed_len);

/* OctetString: variable length SIZE(lb..ub) — constrained int length + align + bytes */
per_error_t per_encode_octet_string(per_stream_t *s, const uint8_t *data, size_t len, uint32_t ub);
per_error_t per_decode_octet_string(per_stream_t *s, uint8_t *out, size_t *out_len, uint32_t ub);

/* OctetString: unconstrained — length determinant + bytes */
per_error_t per_encode_octet_string_unconstrained(per_stream_t *s, const uint8_t *data, size_t len);
per_error_t per_decode_octet_string_unconstrained(per_stream_t *s, uint8_t *out, size_t *out_len);

/* VisibleString: variable length SIZE(0..max_len) — each char 8 bits */
per_error_t per_encode_visible_string(per_stream_t *s, const char *str, uint32_t max_len);
per_error_t per_decode_visible_string(per_stream_t *s, char *out, uint32_t max_len);

/* UTF8String: variable length SIZE(0..max_len bytes) — length constrained int + bytes */
per_error_t per_encode_utf8_string(per_stream_t *s, const char *str, uint32_t max_len);
per_error_t per_decode_utf8_string(per_stream_t *s, char *out, uint32_t max_len);

/* Open type: length determinant + content bytes */
per_error_t per_encode_open_type(per_stream_t *s, const uint8_t *data, size_t len);
per_error_t per_decode_open_type(per_stream_t *s, const uint8_t **out, size_t *out_len);

#ifdef __cplusplus
}
#endif

#endif
