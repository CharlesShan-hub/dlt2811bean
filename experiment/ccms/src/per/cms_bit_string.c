#include "per/cms_bit_string.h"
#include "per/cms_integer.h"
#include <string.h>

/* Fixed-length BIT STRING: no length prefix, just bits */
per_error_t per_encode_bit_string_fixed(per_stream_t *s, const uint8_t *data, int fixed_nbits) {
    for (int i = 0; i < fixed_nbits; i++) {
        int bit = (data[i / 8] >> (7 - (i % 8))) & 1;
        per_error_t err = per_stream_write_bit(s, bit);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_bit_string_fixed(per_stream_t *s, uint8_t *out, int fixed_nbits) {
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

/* Variable-length BIT STRING: length constrained int + bits */
per_error_t per_encode_bit_string(per_stream_t *s, const uint8_t *data, int nbits, int ub) {
    per_error_t err = per_encode_constrained_int(s, nbits, 0, ub);
    if (err) return err;
    return per_encode_bit_string_fixed(s, data, nbits);
}

per_error_t per_decode_bit_string(per_stream_t *s, uint8_t *out, int *out_nbits, int ub) {
    int64_t len;
    per_error_t err = per_decode_constrained_int(s, &len, 0, ub);
    if (err) return err;
    *out_nbits = (int)len;
    return per_decode_bit_string_fixed(s, out, (int)len);
}
