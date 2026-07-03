#ifndef CMSPER_INTEGER_H
#define CMSPER_INTEGER_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_integer.h
 * @brief PER integer encoding/decoding (X.691 §12 & §11.9).
 *
 * Supports constrained, semi-constrained, and unconstrained integer types,
 * as well as length determinants and "normally small" non-negative integers
 * used for CHOICE indices and extensibility markers.
 */

/*
 * Encode a constrained integer (X.691 §12).
 *
 * Encoding depends on the range size:
 *   range=1        → 0 bits (value is implicit)
 *   2..255         → ceil(log2(range)) bits
 *   256..65536     → align + big-endian bytes
 *   >65536         → length(constrained 1..maxLen) + align + content
 */
per_error_t per_encode_constrained_int(per_stream_t *s, int64_t value, int64_t lower_bound, int64_t upper_bound);

/* Decode a constrained integer (inverse of per_encode_constrained_int). */
per_error_t per_decode_constrained_int(per_stream_t *s, int64_t *out, int64_t lower_bound, int64_t upper_bound);

/*
 * Encode a PER length determinant (X.691 §11.9).
 *
 *   0..127     → 1 byte:  0xxxxxxx
 *   128..16383 → 2 bytes: 10xxxxxx xxxxxxxx
 *   >16383     → returns PER_ERR_RANGE (fragmented form not supported)
 */
per_error_t per_encode_length(per_stream_t *s, uint32_t length);

/* Decode a PER length determinant (inverse of per_encode_length). */
per_error_t per_decode_length(per_stream_t *s, uint32_t *out);

/*
 * Encode a normally small non-negative integer (X.691 §11.9).
 *
 *   0..63  → 1bit(0) + 6 bits
 *   >=64   → 1bit(1) + semi-constrained encoding
 */
per_error_t per_encode_small_non_negative(per_stream_t *s, uint32_t value);

/* Decode a normally small non-negative integer. */
per_error_t per_decode_small_non_negative(per_stream_t *s, uint32_t *out);

/*
 * Encode a semi-constrained integer (lb..MAX).
 * Encoded as length determinant + big-endian content.
 */
per_error_t per_encode_semi_constrained(per_stream_t *s, int64_t value, int64_t lower_bound);

/* Decode a semi-constrained integer. */
per_error_t per_decode_semi_constrained(per_stream_t *s, int64_t *out, int64_t lower_bound);

/*
 * Encode an unconstrained signed integer.
 * Encoded as length determinant + two's complement content.
 */
per_error_t per_encode_unconstrained_int(per_stream_t *s, int64_t value);

/* Decode an unconstrained signed integer. */
per_error_t per_decode_unconstrained_int(per_stream_t *s, int64_t *out);

/*
 * Write an unsigned 64-bit value to a big-endian byte array using the
 * minimal number of bytes that can represent the value.
 * @return Number of bytes written (1..max_bytes).
 */
int per_unsigned_to_bytes(uint64_t value, uint8_t *out, int max_bytes);

#ifdef __cplusplus
}
#endif

#endif
