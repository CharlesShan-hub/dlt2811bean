#include "per/cms_string.h"
#include "per/cms_integer.h"
#include <string.h>

/* ---- OctetString fixed ---- */
per_error_t per_encode_octet_string_fixed(per_stream_t *s, const uint8_t *data, size_t fixed_len) {
    per_stream_align(s);
    return per_stream_write_bytes(s, data, fixed_len);
}

per_error_t per_decode_octet_string_fixed(per_stream_t *s, uint8_t *out, size_t fixed_len) {
    per_stream_align(s);
    return per_stream_read_bytes(s, out, fixed_len);
}

/* ---- OctetString variable (SIZE(lb..ub)): length + content ---- */
per_error_t per_encode_octet_string(per_stream_t *s, const uint8_t *data, size_t len, uint32_t ub) {
    per_error_t err = per_encode_constrained_int(s, (int64_t)len, 0, ub);
    if (err) return err;
    per_stream_align(s);
    return per_stream_write_bytes(s, data, len);
}

per_error_t per_decode_octet_string(per_stream_t *s, uint8_t *out, size_t *out_len, uint32_t ub) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, ub);
    if (err) return err;
    *out_len = (size_t)len;
    per_stream_align(s);
    return per_stream_read_bytes(s, out, len);
}

/* ---- OctetString unconstrained — length determinant + content ---- */
per_error_t per_encode_octet_string_unconstrained(per_stream_t *s, const uint8_t *data, size_t len) {
    per_error_t err = per_encode_length(s, (uint32_t)len);
    if (err) return err;
    return per_stream_write_bytes(s, data, len);
}

per_error_t per_decode_octet_string_unconstrained(per_stream_t *s, uint8_t *out, size_t *out_len) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err) return err;
    *out_len = (size_t)len;
    return per_stream_read_bytes(s, out, len);
}

/* ---- VisibleString (8 bits per char, variable length) ---- */
per_error_t per_encode_visible_string(per_stream_t *s, const uint8_t *str, uint32_t max_len) {
    size_t len = str ? strlen((const char *)str) : 0;
    if (len > max_len) return PER_ERR_LENGTH;
    per_error_t err = per_encode_constrained_int(s, (int64_t)len, 0, max_len);
    if (err) return err;
    if (max_len * 8 > 16) per_stream_align(s);
    for (size_t i = 0; i < len; i++) {
        err = per_stream_write_bits(s, str[i], 8);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_visible_string(per_stream_t *s, uint8_t *out, uint32_t max_len) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, max_len);
    if (err) return err;
    if (len < 0 || (uint64_t)len > max_len) return PER_ERR_LENGTH;
    if (max_len * 8 > 16) per_stream_align(s);
    for (int64_t i = 0; i < len; i++) {
        uint64_t ch;
        err = per_stream_read_bits(s, &ch, 8);
        if (err) return err;
        out[i] = (uint8_t)ch;
    }
    out[len] = '\0';
    return PER_OK;
}

/* ---- VisibleString fixed ---- */
per_error_t per_encode_visible_string_fixed(per_stream_t *s, const uint8_t *str, uint32_t fixed_len) {
    size_t len = str ? strlen((const char *)str) : 0;
    if (fixed_len * 8 > 16) per_stream_align(s);
    uint32_t i;
    for (i = 0; i < fixed_len; i++) {
        uint8_t ch = (i < len) ? str[i] : 0;
        per_error_t err = per_stream_write_bits(s, ch, 8);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_visible_string_fixed(per_stream_t *s, uint8_t *out, uint32_t fixed_len) {
    if (fixed_len * 8 > 16) per_stream_align(s);
    for (uint32_t i = 0; i < fixed_len; i++) {
        uint64_t ch;
        per_error_t err = per_stream_read_bits(s, &ch, 8);
        if (err) return err;
        out[i] = (uint8_t)ch;
    }
    out[fixed_len] = '\0';
    return PER_OK;
}

/* ---- VisibleString unconstrained — length determinant + 8-bit chars ---- */
per_error_t per_encode_visible_string_unconstrained(per_stream_t *s, const uint8_t *str) {
    size_t len = str ? strlen((const char *)str) : 0;
    per_error_t err = per_encode_length(s, (uint32_t)len);
    if (err) return err;
    per_stream_align(s);
    for (size_t i = 0; i < len; i++) {
        err = per_stream_write_bits(s, str[i], 8);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_visible_string_unconstrained(per_stream_t *s, uint8_t *out, uint32_t *out_len) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err) return err;
    per_stream_align(s);
    for (uint32_t i = 0; i < len; i++) {
        uint64_t ch;
        err = per_stream_read_bits(s, &ch, 8);
        if (err) return err;
        out[i] = (uint8_t)ch;
    }
    out[len] = '\0';
    *out_len = len;
    return PER_OK;
}

/* ---- BitString fixed ---- */
per_error_t per_encode_bit_string_fixed(per_stream_t *s, const uint8_t *data, int fixed_nbits) {
    if (fixed_nbits > 16) per_stream_align(s);
    for (int i = 0; i < fixed_nbits; i++) {
        int bit = (data[i / 8] >> (7 - (i % 8))) & 1;
        per_error_t err = per_stream_write_bit(s, bit);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_bit_string_fixed(per_stream_t *s, uint8_t *out, int fixed_nbits) {
    if (fixed_nbits > 16) per_stream_align(s);
    int nbytes = (fixed_nbits + 7) / 8;
    memset(out, 0, nbytes);
    for (int i = 0; i < fixed_nbits; i++) {
        int bit;
        per_error_t err = per_stream_read_bit(s, &bit);
        if (err) return err;
        if (bit) out[i / 8] |= (uint8_t)(0x80 >> (i % 8));
    }
    return PER_OK;
}

/* ---- BitString variable ---- */
per_error_t per_encode_bit_string(per_stream_t *s, const uint8_t *data, int nbits, int ub) {
    per_error_t err = per_encode_constrained_int(s, nbits, 0, ub);
    if (err) return err;
    per_stream_align(s);
    return per_encode_bit_string_fixed(s, data, nbits);
}

per_error_t per_decode_bit_string(per_stream_t *s, uint8_t *out, int *out_nbits, int ub) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, ub);
    if (err) return err;
    *out_nbits = (int)len;
    per_stream_align(s);
    return per_decode_bit_string_fixed(s, out, (int)len);
}

/* ---- BitString unconstrained — semi-constrained length + bits ---- */
per_error_t per_encode_bit_string_unconstrained(per_stream_t *s, const uint8_t *data, int nbits) {
    per_error_t err = per_encode_semi_constrained(s, nbits, 0);
    if (err) return err;
    per_stream_align(s);
    return per_encode_bit_string_fixed(s, data, nbits);
}

per_error_t per_decode_bit_string_unconstrained(per_stream_t *s, uint8_t *out, int *out_nbits) {
    int64_t len;
    per_error_t err = per_decode_semi_constrained(s, &len, 0);
    if (err) return err;
    *out_nbits = (int)len;
    per_stream_align(s);
    return per_decode_bit_string_fixed(s, out, (int)len);
}

/* ---- UTF8String (bytes) variable ---- */
per_error_t per_encode_utf8_string(per_stream_t *s, const uint8_t *str, uint32_t max_len) {
    size_t len = str ? strlen((const char *)str) : 0;
    if (len > max_len) return PER_ERR_LENGTH;
    per_error_t err = per_encode_constrained_int(s, (int64_t)len, 0, max_len);
    if (err) return err;
    return per_stream_write_bytes(s, str, len);
}

per_error_t per_decode_utf8_string(per_stream_t *s, uint8_t *out, uint32_t max_len) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, max_len);
    if (err) return err;
    if (len < 0 || (uint64_t)len > max_len) return PER_ERR_LENGTH;
    per_stream_align(s);
    err = per_stream_read_bytes(s, out, (size_t)len);
    if (err) return err;
    out[len] = '\0';
    return PER_OK;
}

/* ---- UTF8String unconstrained — length determinant + bytes ---- */
per_error_t per_encode_utf8_string_unconstrained(per_stream_t *s, const uint8_t *str) {
    size_t len = str ? strlen((const char *)str) : 0;
    per_error_t err = per_encode_length(s, (uint32_t)len);
    if (err) return err;
    return per_stream_write_bytes(s, str, len);
}

per_error_t per_decode_utf8_string_unconstrained(per_stream_t *s, uint8_t *out, uint32_t *out_len) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err) return err;
    err = per_stream_read_bytes(s, out, len);
    if (err) return err;
    out[len] = '\0';
    *out_len = len;
    return PER_OK;
}

/* ---- UTF8String fixed ---- */
per_error_t per_encode_utf8_string_fixed(per_stream_t *s, const uint8_t *str, uint32_t fixed_len) {
    size_t len = str ? strlen((const char *)str) : 0;
    if (len > fixed_len) len = fixed_len;
    per_stream_align(s);
    per_error_t err = per_stream_write_bytes(s, str, len);
    if (err) return err;
    for (uint32_t i = (uint32_t)len; i < fixed_len; i++) {
        err = per_stream_write_byte_aligned(s, 0);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_utf8_string_fixed(per_stream_t *s, uint8_t *out, uint32_t fixed_len) {
    per_stream_align(s);
    per_error_t err = per_stream_read_bytes(s, out, fixed_len);
    if (err) return err;
    out[fixed_len] = '\0';
    return PER_OK;
}
