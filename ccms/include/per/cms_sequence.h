#ifndef CMSPER_SEQUENCE_H
#define CMSPER_SEQUENCE_H

#include "per/cms_stream.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_sequence.h
 * @brief PER SEQUENCE encoding/decoding (X.691 §22).
 *
 * Provides OPTIONAL bitmap functions for SEQUENCE types. In PER, all
 * OPTIONAL and DEFAULT fields in a SEQUENCE are preceded by a single
 * bit-map that is byte-aligned before the first bit.
 *
 * Usage:
 * @code
 *   // encode
 *   uint64_t bitmap = 0;
 *   if (field1_present) bitmap |= 1U << 0;
 *   if (field2_present) bitmap |= 1U << 1;
 *   per_encode_optional_bitmap(s, bitmap, 2);
 *   if (field1_present) encode_field1(s, ...);
 *   if (field2_present) encode_field2(s, ...);
 *
 *   // decode
 *   uint64_t bitmap;
 *   per_decode_optional_bitmap(s, &bitmap, 2);
 *   if (bitmap & (1U << 0)) decode_field1(s, ...);
 *   if (bitmap & (1U << 1)) decode_field2(s, ...);
 * @endcode
 */

/*
 * Encode a SEQUENCE OPTIONAL bitmap.
 * Byte-aligns then writes @p nfields bits (one per OPTIONAL field).
 *
 * @param s       Stream pointer.
 * @param bitmap  Bitmask: bit 0 = first OPTIONAL field, etc.
 * @param nfields Number of OPTIONAL fields (max 64).
 */
per_error_t per_encode_optional_bitmap(per_stream_t *s, uint64_t bitmap, int nfields);

/*
 * Decode a SEQUENCE OPTIONAL bitmap.
 * Byte-aligns then reads @p nfields bits.
 *
 * @param s       Stream pointer.
 * @param bitmap  Output bitmask.
 * @param nfields Number of OPTIONAL fields to read (max 64).
 */
per_error_t per_decode_optional_bitmap(per_stream_t *s, uint64_t *bitmap, int nfields);

#ifdef __cplusplus
}
#endif

#endif
