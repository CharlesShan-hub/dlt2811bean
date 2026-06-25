#ifndef CMSPER_SEQUENCE_H
#define CMSPER_SEQUENCE_H

#include "per/cms_stream.h"
#include <stdint.h>
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_sequence.h
 * @brief PER SEQUENCE OPTIONAL bitmap encoding/decoding (X.691 §22).
 *
 * In PER, all OPTIONAL and DEFAULT fields in a SEQUENCE are preceded by a
 * single bit-map (byte-aligned before the first bit).
 *
 * Usage:
 * @code
 *   // encode
 *   bool opt_present[2] = {
 *       field1_present && field1,
 *       field2_present && field2
 *   };
 *   per_encode_optional_bitmap(s, opt_present, 2);
 *   if (opt_present[0]) encode_field1(s, ...);
 *   if (opt_present[1]) encode_field2(s, ...);
 *
 *   // decode
 *   bool opt_present[2];
 *   per_decode_optional_bitmap(s, opt_present, 2);
 *   if (opt_present[0]) decode_field1(s, ...);
 *   if (opt_present[1]) decode_field2(s, ...);
 * @endcode
 */

/*
 * Encode a SEQUENCE OPTIONAL bitmap from a bool array.
 * Byte-aligns then writes @p nfields bits (one per OPTIONAL field).
 *
 * @param s       Stream pointer.
 * @param flags   Presence flags: flags[i] = true → field i is present.
 * @param nfields Number of OPTIONAL fields (max 64).
 */
per_error_t per_encode_optional_bitmap(per_stream_t *s, const bool *flags, int nfields);

/*
 * Decode a SEQUENCE OPTIONAL bitmap into a bool array.
 *
 * @param s       Stream pointer.
 * @param flags   Output presence flags.
 * @param nfields Number of OPTIONAL fields to read (max 64).
 */
per_error_t per_decode_optional_bitmap(per_stream_t *s, bool *flags, int nfields);

#ifdef __cplusplus
}
#endif

#endif
