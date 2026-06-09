#ifndef CMSPER_INTEGER_H
#define CMSPER_INTEGER_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Per X.691 §12 & §11.9 — matches Java PerInteger exactly.
 *
 * Constrained INTEGER (lb..ub):
 *   range=1        → 0 bits
 *   2..255         → ceil(log2(range)) bits
 *   256..65536     → align + bytes
 *   >65536         → length(constrained 1..maxLen) + align + content
 *
 * Length determinant:
 *   0..127         → 1 byte: 0xxxxxxx
 *   128..16383     → 2 bytes: 10xxxxxx xxxxxxxx
 *
 * Normally small non-negative integer:
 *   0..63          → 1bit(0) + 6bits
 *   >=64           → 1bit(1) + semi-constrained
 */

per_error_t per_encode_constrained_int(per_stream_t *s, int64_t value,
                                       int64_t lower_bound, int64_t upper_bound);
per_error_t per_decode_constrained_int(per_stream_t *s, int64_t *out,
                                       int64_t lower_bound, int64_t upper_bound);

/* Length determinant (aligns + writes/reads bytes) */
per_error_t per_encode_length(per_stream_t *s, uint32_t length);
per_error_t per_decode_length(per_stream_t *s, uint32_t *out);

/* Normally small non-negative integer (for CHOICE index, etc.) */
per_error_t per_encode_small_non_negative(per_stream_t *s, uint32_t value);
per_error_t per_decode_small_non_negative(per_stream_t *s, uint32_t *out);

/* Semi-constrained (lb..MAX) — length + align + content */
per_error_t per_encode_semi_constrained(per_stream_t *s, int64_t value, int64_t lower_bound);
per_error_t per_decode_semi_constrained(per_stream_t *s, int64_t *out, int64_t lower_bound);

/* Unconstrained integer (signed) — length + align + content */
per_error_t per_encode_unconstrained_int(per_stream_t *s, int64_t value);
per_error_t per_decode_unconstrained_int(per_stream_t *s, int64_t *out);

/* Unsigned value → minimal big-endian bytes helper */
int per_unsigned_to_bytes(uint64_t value, uint8_t *out, int max_bytes);

#ifdef __cplusplus
}
#endif

#endif
