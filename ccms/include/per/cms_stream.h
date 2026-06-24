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
 *   - Fixed-buffer mode: writes into a caller-provided buffer.
 *   - Dynamic mode: auto-grows the buffer on write (requires `calloc`/`realloc`).
 */

/* Bit stream state for PER encode/decode operations. */
typedef struct {
    uint8_t *buf;         /* Underlying byte buffer. */
    size_t   capacity;    /* Allocated size of buf. */
    size_t   byte_pos;    /* Current byte position (0-based). */
    int      bit_pos;     /* Current bit position within the byte (0-7, 0=MSB). */
    bool     is_write;    /* true for write mode, false for read mode. */
    bool     is_dynamic;  /* true if buf is heap-allocated and auto-grows. */
} per_stream_t;

/* ---- Stream lifecycle ---- */

/* Initialise a write stream backed by a fixed buffer. */
void      per_stream_init_write(per_stream_t *s, uint8_t *buf, size_t capacity);

/*
 * Initialise a write stream with a dynamically growing buffer.
 * @param initial_capacity Minimum starting capacity (clamped to 64).
 * @return PER_OK or PER_ERR_OOM.
 */
per_error_t per_stream_init_dynamic(per_stream_t *s, size_t initial_capacity);

/*
 * Detach the buffer from a dynamic stream.
 * The caller takes ownership of the returned buffer (must be freed with free()).
 * After detach, the stream is no longer usable.
 */
uint8_t*  per_stream_detach(per_stream_t *s, size_t *out_len);

/* Initialise a read stream over an existing buffer. */
void      per_stream_init_read(per_stream_t *s, const uint8_t *buf, size_t capacity);

/* Convenience: create a read stream (value semantics). */
static inline per_stream_t per_stream_new_read(const uint8_t *buf, size_t capacity) {
    per_stream_t s; per_stream_init_read(&s, buf, capacity); return s;
}

/* Convenience: create a fixed-buffer write stream (value semantics). */
static inline per_stream_t per_stream_new_write(uint8_t *buf, size_t capacity) {
    per_stream_t s; per_stream_init_write(&s, buf, capacity); return s;
}

/* ---- Position queries ---- */

/* Current bit position in the stream (0 = start of buffer). */
size_t    per_stream_tell(const per_stream_t *s);

/* Number of full bytes written (partial byte rounds up). */
size_t    per_stream_bytes_written(const per_stream_t *s);

/* ---- Alignment ---- */

/*
 * Align to next byte boundary.
 * In write mode: advances byte_pos if bit_pos > 0.
 * In read mode: skips remaining bits in the current byte.
 * No-op if already aligned (bit_pos == 0).
 */
void      per_stream_align(per_stream_t *s);

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
