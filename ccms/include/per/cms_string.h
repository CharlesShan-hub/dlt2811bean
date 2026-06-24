#ifndef CMSPER_STRING_H
#define CMSPER_STRING_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_string.h
 * @brief PER string and bit-string encoding/decoding.
 *
 * Covers OCTET STRING, VisibleString, UTF8String, and BIT STRING in
 * fixed-length, constrained-variable, and unconstrained variants.
 */

/* ---- OCTET STRING ---- */

/* Fixed-length OCTET STRING: align + bytes (no length prefix). */
per_error_t per_encode_octet_string_fixed(per_stream_t *s, const uint8_t *data, size_t fixed_len);
per_error_t per_decode_octet_string_fixed(per_stream_t *s, uint8_t *out, size_t fixed_len);

/* Constrained-variable OCTET STRING SIZE(lb..ub): constrained int length + align + bytes. */
per_error_t per_encode_octet_string(per_stream_t *s, const uint8_t *data, size_t len, uint32_t ub);
per_error_t per_decode_octet_string(per_stream_t *s, uint8_t *out, size_t *out_len, uint32_t ub);

/* Unconstrained OCTET STRING: length determinant + align + bytes. */
per_error_t per_encode_octet_string_unconstrained(per_stream_t *s, const uint8_t *data, size_t len);
per_error_t per_decode_octet_string_unconstrained(per_stream_t *s, uint8_t *out, size_t *out_len);

/* ---- VisibleString (7-bit or 8-bit characters) ---- */

/* Constrained-variable VisibleString SIZE(0..max_len): constrained int length + 8-bit chars. */
per_error_t per_encode_visible_string(per_stream_t *s, const uint8_t *str, uint32_t max_len);
per_error_t per_decode_visible_string(per_stream_t *s, uint8_t *out, uint32_t max_len);

/* Unconstrained VisibleString: length determinant + 8-bit chars. */
per_error_t per_encode_visible_string_unconstrained(per_stream_t *s, const uint8_t *str);
per_error_t per_decode_visible_string_unconstrained(per_stream_t *s, uint8_t *out, uint32_t *out_len);

/* Fixed-length VisibleString: no length prefix, pad with zeros. */
per_error_t per_encode_visible_string_fixed(per_stream_t *s, const uint8_t *str, uint32_t fixed_len);
per_error_t per_decode_visible_string_fixed(per_stream_t *s, uint8_t *out, uint32_t fixed_len);

/* ---- UTF8String (byte-oriented) ---- */

/* Constrained-variable UTF8String SIZE(0..max_len bytes): constrained int length + bytes. */
per_error_t per_encode_utf8_string(per_stream_t *s, const uint8_t *str, uint32_t max_len);
per_error_t per_decode_utf8_string(per_stream_t *s, uint8_t *out, uint32_t max_len);

/* Unconstrained UTF8String: length determinant + bytes. */
per_error_t per_encode_utf8_string_unconstrained(per_stream_t *s, const uint8_t *str);
per_error_t per_decode_utf8_string_unconstrained(per_stream_t *s, uint8_t *out, uint32_t *out_len);

/* Fixed-length UTF8String: no length prefix, pad with zeros. */
per_error_t per_encode_utf8_string_fixed(per_stream_t *s, const uint8_t *str, uint32_t fixed_len);
per_error_t per_decode_utf8_string_fixed(per_stream_t *s, uint8_t *out, uint32_t fixed_len);

/* ---- BIT STRING ---- */

/* Fixed-length BIT STRING: no length prefix, just bits (align if >16 bits). */
per_error_t per_encode_bit_string_fixed(per_stream_t *s, const uint8_t *data, int fixed_nbits);
per_error_t per_decode_bit_string_fixed(per_stream_t *s, uint8_t *out, int fixed_nbits);

/* Constrained-variable BIT STRING SIZE(0..ub): constrained int length + align + bits. */
per_error_t per_encode_bit_string(per_stream_t *s, const uint8_t *data, int nbits, int ub);
per_error_t per_decode_bit_string(per_stream_t *s, uint8_t *out, int *out_nbits, int ub);

/* Unconstrained BIT STRING: semi-constrained length + align + bits. */
per_error_t per_encode_bit_string_unconstrained(per_stream_t *s, const uint8_t *data, int nbits);
per_error_t per_decode_bit_string_unconstrained(per_stream_t *s, uint8_t *out, int *out_nbits);

#ifdef __cplusplus
}
#endif

#endif
