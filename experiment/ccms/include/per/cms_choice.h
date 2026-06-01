#ifndef CMSPER_CHOICE_H
#define CMSPER_CHOICE_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Per X.691 §17 — matches Java PerChoice.
 *
 * Non-extensible CHOICE index is encoded as
 * "normally small non-negative integer" (PerInteger.encodeSmallNonNegative).
 *
 * Extensible CHOICE: 1 bit extension flag + small non-negative index. */

per_error_t per_encode_choice(per_stream_t *s, uint32_t index);
per_error_t per_decode_choice(per_stream_t *s, uint32_t *out);

per_error_t per_encode_choice_extensible(per_stream_t *s, bool is_extension, uint32_t index);
per_error_t per_decode_choice_extensible(per_stream_t *s, bool *is_extension, uint32_t *out);

#ifdef __cplusplus
}
#endif

#endif
