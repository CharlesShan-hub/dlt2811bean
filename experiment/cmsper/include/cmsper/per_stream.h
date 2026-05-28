#ifndef CMSPER_STREAM_H
#define CMSPER_STREAM_H

#include "per_types.h"
#include <stdint.h>
#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    uint8_t *buf;
    size_t   capacity;
    size_t   byte_pos;
    int      bit_pos;   /* 0-7, 0=MSB */
    bool     is_write;
} per_stream_t;

void      per_stream_init_write(per_stream_t *s, uint8_t *buf, size_t capacity);
void      per_stream_init_read(per_stream_t *s, const uint8_t *buf, size_t capacity);
size_t    per_stream_tell(const per_stream_t *s);
size_t    per_stream_bytes_written(const per_stream_t *s);
void      per_stream_align(per_stream_t *s);
void      per_stream_skip_align(per_stream_t *s);

per_error_t per_stream_write_bit(per_stream_t *s, int bit);
per_error_t per_stream_read_bit(per_stream_t *s, int *out);
per_error_t per_stream_write_bits(per_stream_t *s, uint64_t value, int nbits);
per_error_t per_stream_read_bits(per_stream_t *s, uint64_t *out, int nbits);
per_error_t per_stream_write_byte_aligned(per_stream_t *s, uint8_t byte);
per_error_t per_stream_read_byte_aligned(per_stream_t *s, uint8_t *out);
per_error_t per_stream_write_bytes(per_stream_t *s, const uint8_t *data, size_t len);
per_error_t per_stream_read_bytes(per_stream_t *s, uint8_t *out, size_t len);

#ifdef __cplusplus
}
#endif

#endif
