#include "ccms/per_string.h"
#include "ccms/per_integer.h"
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

/* ---- OctetString unconstrained: length determinant + align + content ---- */
per_error_t per_encode_octet_string_unconstrained(per_stream_t *s, const uint8_t *data, size_t len) {
    per_error_t err = per_encode_length(s, (uint32_t)len);
    if (err) return err;
    return per_stream_write_bytes(s, data, len);
}

per_error_t per_decode_octet_string_unconstrained(per_stream_t *s, uint8_t *out, size_t *out_len) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err) return err;
    *out_len = len;
    return per_stream_read_bytes(s, out, len);
}

/* ---- VisibleString (8 bits per char, variable length) ---- */
per_error_t per_encode_visible_string(per_stream_t *s, const char *str, uint32_t max_len) {
    size_t len = str ? strlen(str) : 0;
    if (len > max_len) return PER_ERR_LENGTH;
    per_error_t err = per_encode_constrained_int(s, (int64_t)len, 0, max_len);
    if (err) return err;
    for (size_t i = 0; i < len; i++) {
        err = per_stream_write_bits(s, (uint8_t)str[i], 8);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_visible_string(per_stream_t *s, char *out, uint32_t max_len) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, max_len);
    if (err) return err;
    if (len < 0 || (uint64_t)len > max_len) return PER_ERR_LENGTH;
    for (int64_t i = 0; i < len; i++) {
        uint64_t ch;
        err = per_stream_read_bits(s, &ch, 8);
        if (err) return err;
        out[i] = (char)ch;
    }
    out[len] = '\0';
    return PER_OK;
}

/* ---- UTF8String (bytes) variable ---- */
per_error_t per_encode_utf8_string(per_stream_t *s, const char *str, uint32_t max_len) {
    size_t len = str ? strlen(str) : 0;
    if (len > max_len) return PER_ERR_LENGTH;
    per_error_t err = per_encode_constrained_int(s, (int64_t)len, 0, max_len);
    if (err) return err;
    return per_stream_write_bytes(s, (const uint8_t *)str, len);
}

per_error_t per_decode_utf8_string(per_stream_t *s, char *out, uint32_t max_len) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, max_len);
    if (err) return err;
    if (len < 0 || (uint64_t)len > max_len) return PER_ERR_LENGTH;
    per_stream_align(s);
    err = per_stream_read_bytes(s, (uint8_t *)out, (size_t)len);
    if (err) return err;
    out[len] = '\0';
    return PER_OK;
}

/* ---- Open type: length determinant + align + content ---- */
per_error_t per_encode_open_type(per_stream_t *s, const uint8_t *data, size_t len) {
    per_error_t err = per_encode_length(s, (uint32_t)len);
    if (err) return err;
    return per_stream_write_bytes(s, data, len);
}

per_error_t per_decode_open_type(per_stream_t *s, const uint8_t **out, size_t *out_len) {
    uint32_t len;
    per_error_t err = per_decode_length(s, &len);
    if (err) return err;
    *out_len = len;
    if (s->byte_pos + len > s->capacity) return PER_ERR_TRUNCATED;
    *out = s->buf + s->byte_pos;
    s->byte_pos += len;
    return PER_OK;
}
