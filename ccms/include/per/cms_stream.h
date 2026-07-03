#ifndef CMSPER_STREAM_H
#define CMSPER_STREAM_H

#include "per/cms_types.h"
#include <stdint.h>
#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * @file cms_stream.h
 * @brief Bit-level stream for PER encode/decode.
 *
 * per_stream_t provides a bit-oriented read/write buffer used by all
 * PER encoding and decoding functions. It tracks both byte position
 * and bit position within the current byte (MSB-first ordering).
 *
 * Two modes:
 *   - Read  stream: caller provides the buffer to decode from.
 *   - Write stream: heap-allocated, auto-grows on write.
 */

/* Bit stream state for PER encode/decode operations. */
typedef struct {
    uint8_t *buf;    /* Underlying byte buffer. */
    size_t capacity; /* Allocated size of buf. */
    size_t byte_pos; /* Current byte position (0-based). */
    int bit_pos;     /* Current bit position within the byte (0-7, 0=MSB). */
    bool is_write;   /* true for write mode (heap-allocated, auto-grows), false for read mode. */
} per_stream_t;

/* ---- Stream lifecycle ---- */

/* Read stream — caller provides buffer. */
void per_stream_init_read(per_stream_t *s, const uint8_t *buf, size_t capacity);
static inline per_stream_t per_stream_new_read(const uint8_t *buf, size_t capacity) {
    per_stream_t s;
    per_stream_init_read(&s, buf, capacity);
    return s;
}

/* Write stream — auto-grows on write (heap-allocated). */
per_error_t per_stream_init_write(per_stream_t *s, size_t initial_capacity);

/*
 * Detach the buffer from a write stream.
 * The caller takes ownership of the returned buffer (must be freed with free()).
 * After detach, the stream is no longer usable.
 */
uint8_t *per_stream_detach(per_stream_t *s, size_t *out_len);

/*
 * Free resources held by a write stream.
 * For read streams this is a no-op (caller owns the buffer).
 */
void per_stream_free(per_stream_t *s);

/* ---- Position queries ---- */

/* Current bit position in the stream (0 = start of buffer). */
size_t per_stream_tell(const per_stream_t *s);

/* Number of full bytes written (partial byte rounds up). */
size_t per_stream_bytes_written(const per_stream_t *s);

/* ---- Alignment ---- */

/*
 * Align to next byte boundary.
 * In write mode: advances byte_pos if bit_pos > 0.
 * In read mode: skips remaining bits in the current byte.
 * No-op if already aligned (bit_pos == 0).
 */
void per_stream_align(per_stream_t *s);

/* ---- Bit-level I/O ---- */

per_error_t per_stream_write_bit(per_stream_t *s, int bit);
per_error_t per_stream_read_bit(per_stream_t *s, int *out);
per_error_t per_stream_write_bits(per_stream_t *s, uint64_t value, int nbits);
per_error_t per_stream_read_bits(per_stream_t *s, uint64_t *out, int nbits);

/* ---- Byte-aligned I/O (auto-aligns before read/write) ---- */

per_error_t per_stream_write_byte_aligned(per_stream_t *s, uint8_t byte);
per_error_t per_stream_read_byte_aligned(per_stream_t *s, uint8_t *out);
per_error_t per_stream_write_bytes(per_stream_t *s, const uint8_t *data, size_t len);
per_error_t per_stream_read_bytes(per_stream_t *s, uint8_t *out, size_t len);

#ifdef __cplusplus
}
#endif

#endif
