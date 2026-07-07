#ifndef CMSPER_CHOICE_H
#define CMSPER_CHOICE_H

#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_choice.h
 * @brief PER CHOICE index encoding (X.691 §17).
 *
 * CHOICE indices are encoded using "normally small non-negative integer"
 * encoding. Extensible CHOICE variants have an additional 1-bit extension
 * flag before the index.
 */

/*
 * Encode a non-extensible CHOICE index.
 * Encoded as a normally small non-negative integer.
 */
per_error_t per_encode_choice(per_stream_t *s, uint32_t index);

/* Decode a non-extensible CHOICE index. */
per_error_t per_decode_choice(per_stream_t *s, uint32_t *out);

/*
 * Encode an extensible CHOICE index.
 * @param is_extension 1 if the value is in the extension range, 0 if in the root.
 * @param index The CHOICE alternative index.
 */
per_error_t per_encode_choice_extensible(per_stream_t *s, bool is_extension, uint32_t index);

/*
 * Decode an extensible CHOICE index.
 * @param is_extension Output: 1 if extension, 0 if root.
 * @param out Output: the CHOICE alternative index.
 */
per_error_t per_decode_choice_extensible(per_stream_t *s, bool *is_extension, uint32_t *out);

#ifdef __cplusplus
}
#endif

#endif
