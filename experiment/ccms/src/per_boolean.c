#include "per_boolean.h"

per_error_t per_encode_boolean(per_stream_t *s, bool value) {
    return per_stream_write_bit(s, value ? 1 : 0);
}

per_error_t per_decode_boolean(per_stream_t *s, bool *out) {
    int bit;
    per_error_t err = per_stream_read_bit(s, &bit);
    if (err) return err;
    *out = (bit != 0);
    return PER_OK;
}
