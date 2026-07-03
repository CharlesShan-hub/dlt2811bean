#include "per/cms_integer.h"
#include <string.h>

/* ---- Internal helpers ---- */

/* Compute ceil(log2(range)). */
static int bits_needed(uint64_t range) {
    if (range <= 1)
        return 0;
    int bits = 0;
    uint64_t v = range - 1;
    while (v > 0) {
        v >>= 1;
        bits++;
    }
    return bits;
}

/* Compute ceil(log2(range))/8 — number of bytes to represent the range. */
static int bytes_for_range(uint64_t range) {
    int bits = bits_needed(range);
    return (bits + 7) / 8;
}

/* ==================== Constrained INTEGER ==================== */

per_error_t per_encode_constrained_int(per_stream_t *s, int64_t value, int64_t lower_bound, int64_t upper_bound) {
    if (value < lower_bound || value > upper_bound)
        return PER_ERR_RANGE;

    uint64_t range = (uint64_t) (upper_bound - lower_bound + 1);
    if (range == 1)
        return PER_OK; /* 0 bits — value is implicit */

    uint64_t offset = (uint64_t) (value - lower_bound);

    if (range < 256) {
        /* Range 2..255: write ceil(log2(range)) bits directly */
        return per_stream_write_bits(s, offset, bits_needed(range));
    } else if (range <= 65536) {
        /* Range 256..65536: align + big-endian bytes */
        int nbytes = bytes_for_range(range);
        per_stream_align(s);
        for (int i = nbytes - 1; i >= 0; i--) {
            per_error_t err = per_stream_write_byte_aligned(s, (uint8_t) (offset >> (i * 8)));
            if (err)
                return err;
        }
        return PER_OK;
    } else {
        /* Range > 65536: length(constrained 1..maxLen) + align + content */
        int max_len = bytes_for_range(range);
        uint8_t content[16];
        int content_len = per_unsigned_to_bytes(offset, content, max_len);
        per_error_t err = per_encode_constrained_int(s, content_len, 1, max_len);
        if (err)
            return err;
        per_stream_align(s);
        return per_stream_write_bytes(s, content, content_len);
    }
}

per_error_t per_decode_constrained_int(per_stream_t *s, int64_t *out, int64_t lower_bound, int64_t upper_bound) {
    uint64_t range = (uint64_t) (upper_bound - lower_bound + 1);
    if (range == 1) {
        *out = lower_bound;
        return PER_OK;
    }

    uint64_t offset;

    if (range < 256) {
        uint64_t v;
        per_error_t err = per_stream_read_bits(s, &v, bits_needed(range));
        if (err)
            return err;
        offset = v;
    } else if (range <= 65536) {
        int nbytes = bytes_for_range(range);
        per_stream_align(s);
        offset = 0;
        for (int i = 0; i < nbytes; i++) {
            uint8_t byte;
            per_error_t err = per_stream_read_byte_aligned(s, &byte);
            if (err)
                return err;
            offset = (offset << 8) | byte;
        }
    } else {
        int max_len = bytes_for_range(range);
        int64_t content_len;
        per_error_t err = per_decode_constrained_int(s, &content_len, 1, max_len);
        if (err)
            return err;
        per_stream_align(s);
        uint8_t content[32];
        size_t clen = (size_t) content_len;
        if (clen > sizeof(content))
            return PER_ERR_LENGTH;
        err = per_stream_read_bytes(s, content, clen);
        if (err)
            return err;
        offset = 0;
        for (size_t i = 0; i < clen; i++) {
            offset = (offset << 8) | content[i];
        }
    }

    *out = lower_bound + (int64_t) offset;
    return PER_OK;
}

/* ==================== Length determinant ==================== */

per_error_t per_encode_length(per_stream_t *s, uint32_t length) {
    if (length <= 127) {
        return per_stream_write_byte_aligned(s, (uint8_t) length);
    } else if (length <= 16383) {
        uint8_t high = (uint8_t) ((length >> 8) | 0x80);
        uint8_t low = (uint8_t) (length & 0xFF);
        per_error_t err = per_stream_write_byte_aligned(s, high);
        if (err)
            return err;
        return per_stream_write_byte_aligned(s, low);
    }
    return PER_ERR_RANGE; /* fragmented form not supported */
}

per_error_t per_decode_length(per_stream_t *s, uint32_t *out) {
    uint8_t first;
    per_error_t err = per_stream_read_byte_aligned(s, &first);
    if (err)
        return err;

    if ((first & 0x80) == 0) {
        /* Short form (1 byte): 0xxxxxxx */
        *out = first;
        return PER_OK;
    }
    if ((first & 0xC0) == 0x80) {
        /* Long form (2 bytes): 10xxxxxx xxxxxxxx */
        uint8_t second;
        err = per_stream_read_byte_aligned(s, &second);
        if (err)
            return err;
        *out = ((uint32_t) (first & 0x3F) << 8) | second;
        return PER_OK;
    }
    return PER_ERR_RANGE; /* fragmented form not supported */
}

/* ==================== Normally small non-negative ==================== */

per_error_t per_encode_small_non_negative(per_stream_t *s, uint32_t value) {
    if (value <= 63) {
        per_error_t err = per_stream_write_bit(s, 0);
        if (err)
            return err;
        return per_stream_write_bits(s, value, 6);
    }
    per_error_t err = per_stream_write_bit(s, 1);
    if (err)
        return err;
    return per_encode_semi_constrained(s, (int64_t) value, 0);
}

per_error_t per_decode_small_non_negative(per_stream_t *s, uint32_t *out) {
    int is_large;
    per_error_t err = per_stream_read_bit(s, &is_large);
    if (err)
        return err;
    if (!is_large) {
        uint64_t v;
        err = per_stream_read_bits(s, &v, 6);
        if (err)
            return err;
        *out = (uint32_t) v;
        return PER_OK;
    }
    int64_t v;
    err = per_decode_semi_constrained(s, &v, 0);
    if (err)
        return err;
    *out = (uint32_t) v;
    return PER_OK;
}

/* ==================== Semi-constrained (lb..MAX) ==================== */

per_error_t per_encode_semi_constrained(per_stream_t *s, int64_t value, int64_t lower_bound) {
    uint64_t offset = (uint64_t) (value - lower_bound);
    uint8_t content[16];
    int len = per_unsigned_to_bytes(offset, content, 16);
    per_error_t err = per_encode_length(s, (uint32_t) len);
    if (err)
        return err;
    return per_stream_write_bytes(s, content, len);
}

per_error_t per_decode_semi_constrained(per_stream_t *s, int64_t *out, int64_t lower_bound) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err)
        return err;
    if (len > 16)
        return PER_ERR_LENGTH;
    uint8_t content[16];
    err = per_stream_read_bytes(s, content, len);
    if (err)
        return err;
    uint64_t offset = 0;
    for (uint32_t i = 0; i < len; i++) {
        offset = (offset << 8) | content[i];
    }
    *out = lower_bound + (int64_t) offset;
    return PER_OK;
}

/* ==================== Unconstrained (signed) ==================== */

per_error_t per_encode_unconstrained_int(per_stream_t *s, int64_t value) {
    /* Determine minimal bytes for two's complement representation */
    int nbytes;
    if (value >= -128 && value <= 127) {
        nbytes = 1;
    } else if (value >= -32768 && value <= 32767) {
        nbytes = 2;
    } else if (value >= -8388608 && value <= 8388607) {
        nbytes = 3;
    } else if (value >= -2147483648LL && value <= 2147483647LL) {
        nbytes = 4;
    } else {
        nbytes = 8;
    }
    uint8_t content[8];
    for (int i = 0; i < nbytes; i++) {
        content[nbytes - 1 - i] = (uint8_t) ((value >> (i * 8)) & 0xFF);
    }
    per_error_t err = per_encode_length(s, (uint32_t) nbytes);
    if (err)
        return err;
    return per_stream_write_bytes(s, content, nbytes);
}

per_error_t per_decode_unconstrained_int(per_stream_t *s, int64_t *out) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err)
        return err;
    if (len > 8)
        return PER_ERR_LENGTH;
    uint8_t content[8];
    err = per_stream_read_bytes(s, content, len);
    if (err)
        return err;
    int64_t value = 0;
    for (uint32_t i = 0; i < len; i++) {
        value = (value << 8) | content[i];
    }
    /* Sign-extend if negative */
    if (len > 0 && (content[0] & 0x80)) {
        int shift = 64 - (int) (len * 8);
        value = (value << shift) >> shift;
    }
    *out = value;
    return PER_OK;
}

/* ==================== Helper: unsigned → minimal big-endian ==================== */

int per_unsigned_to_bytes(uint64_t value, uint8_t *out, int max_bytes) {
    if (value == 0) {
        out[0] = 0;
        return 1;
    }
    int n = 0;
    uint64_t tmp = value;
    while (tmp > 0) {
        tmp >>= 8;
        n++;
    }
    if (n > max_bytes)
        n = max_bytes;
    for (int i = n - 1; i >= 0; i--) {
        out[i] = (uint8_t) (value & 0xFF);
        value >>= 8;
    }
    return n;
}
