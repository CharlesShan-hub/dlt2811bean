#include "per/cms_stream.h"
#include <string.h>
#include <stdlib.h>

void per_stream_init_write(per_stream_t *s, uint8_t *buf, size_t capacity) {
    s->buf = buf;
    s->capacity = capacity;
    s->byte_pos = 0;
    s->bit_pos = 0;
    s->is_write = true;
    s->is_dynamic = false;
    if (capacity > 0) memset(buf, 0, capacity);
}

void per_stream_init_read(per_stream_t *s, const uint8_t *buf, size_t capacity) {
    s->buf = (uint8_t *)buf;
    s->capacity = capacity;
    s->byte_pos = 0;
    s->bit_pos = 0;
    s->is_write = false;
    s->is_dynamic = false;
}

size_t per_stream_tell(const per_stream_t *s) {
    return s->byte_pos * 8 + s->bit_pos;
}

size_t per_stream_bytes_written(const per_stream_t *s) {
    if (s->bit_pos > 0) return s->byte_pos + 1;
    return s->byte_pos;
}

void per_stream_align(per_stream_t *s) {
    if (s->bit_pos == 0) return;
    s->byte_pos++;
    s->bit_pos = 0;
}

void per_stream_skip_align(per_stream_t *s) {
    if (s->is_write) return;
    if (s->bit_pos == 0) return;
    s->byte_pos++;
    s->bit_pos = 0;
}

static per_error_t ensure_space(per_stream_t *s, size_t need_bytes) {
    if (s->byte_pos + need_bytes <= s->capacity) return PER_OK;
    if (!s->is_dynamic) return PER_ERR_OVERFLOW;

    size_t new_cap = s->capacity > 0 ? s->capacity * 2 : 256;
    while (s->byte_pos + need_bytes > new_cap) {
        new_cap *= 2;
    }
    uint8_t *new_buf = realloc(s->buf, new_cap);
    if (!new_buf) return PER_ERR_OOM;
    memset(new_buf + s->capacity, 0, new_cap - s->capacity);
    s->buf = new_buf;
    s->capacity = new_cap;
    return PER_OK;
}

per_error_t per_stream_init_dynamic(per_stream_t *s, size_t initial_capacity)
{
    if (initial_capacity < 64) initial_capacity = 64;
    s->buf = (uint8_t *)calloc(1, initial_capacity);
    if (!s->buf) return PER_ERR_OOM;
    s->capacity = initial_capacity;
    s->byte_pos = 0;
    s->bit_pos = 0;
    s->is_write = true;
    s->is_dynamic = true;
    return PER_OK;
}

uint8_t* per_stream_detach(per_stream_t *s, size_t *out_len)
{
    uint8_t *buf = s->buf;
    *out_len = per_stream_bytes_written(s);
    s->buf = NULL;
    s->capacity = 0;
    s->byte_pos = 0;
    s->bit_pos = 0;
    s->is_dynamic = false;
    return buf;
}
 
/* ---- bit I/O ---- */

per_error_t per_stream_write_bit(per_stream_t *s, int bit) {
    per_error_t err = ensure_space(s, 1);
    if (err) return err;
    if (bit) {
        s->buf[s->byte_pos] |= (uint8_t)(0x80 >> s->bit_pos);
    }
    s->bit_pos++;
    if (s->bit_pos == 8) {
        s->byte_pos++;
        s->bit_pos = 0;
    }
    return PER_OK;
}

per_error_t per_stream_read_bit(per_stream_t *s, int *out) {
    if (s->byte_pos >= s->capacity) return PER_ERR_TRUNCATED;
    *out = (s->buf[s->byte_pos] >> (7 - s->bit_pos)) & 1;
    s->bit_pos++;
    if (s->bit_pos == 8) {
        s->byte_pos++;
        s->bit_pos = 0;
    }
    return PER_OK;
}

/* ---- multi-bit I/O ---- */

per_error_t per_stream_write_bits(per_stream_t *s, uint64_t value, int nbits) {
    if (nbits <= 0) return PER_OK;
    if (nbits > 64) return PER_ERR_INVALID_ARG;
    for (int i = nbits - 1; i >= 0; i--) {
        per_error_t err = per_stream_write_bit(s, (int)((value >> i) & 1ULL));
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_stream_read_bits(per_stream_t *s, uint64_t *out, int nbits) {
    if (nbits <= 0) { *out = 0; return PER_OK; }
    if (nbits > 64) return PER_ERR_INVALID_ARG;
    uint64_t val = 0;
    for (int i = 0; i < nbits; i++) {
        int bit;
        per_error_t err = per_stream_read_bit(s, &bit);
        if (err) return err;
        val = (val << 1) | (uint64_t)bit;
    }
    *out = val;
    return PER_OK;
}

/* ---- byte-aligned I/O (aligns before read/write) ---- */

per_error_t per_stream_write_byte_aligned(per_stream_t *s, uint8_t byte) {
    per_stream_align(s);
    per_error_t err = ensure_space(s, 1);
    if (err) return err;
    s->buf[s->byte_pos] = byte;
    s->byte_pos++;
    return PER_OK;
}

per_error_t per_stream_read_byte_aligned(per_stream_t *s, uint8_t *out) {
    per_stream_align(s);
    if (s->byte_pos >= s->capacity) return PER_ERR_TRUNCATED;
    *out = s->buf[s->byte_pos];
    s->byte_pos++;
    return PER_OK;
}

/* ---- raw byte I/O (also aligns first) ---- */

per_error_t per_stream_write_bytes(per_stream_t *s, const uint8_t *data, size_t len) {
    if (len == 0) return PER_OK;
    per_stream_align(s);
    per_error_t err = ensure_space(s, len);
    if (err) return err;
    memcpy(s->buf + s->byte_pos, data, len);
    s->byte_pos += len;
    return PER_OK;
}

per_error_t per_stream_read_bytes(per_stream_t *s, uint8_t *out, size_t len) {
    if (len == 0) return PER_OK;
    per_stream_align(s);
    if (s->byte_pos + len > s->capacity) return PER_ERR_TRUNCATED;
    memcpy(out, s->buf + s->byte_pos, len);
    s->byte_pos += len;
    return PER_OK;
}
