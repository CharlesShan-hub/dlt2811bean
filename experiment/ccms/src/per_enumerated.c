#include "ccms/per_enumerated.h"
#include "ccms/per_integer.h"

per_error_t per_encode_enumerated(per_stream_t *s, uint32_t value, uint32_t value_count) {
    return per_encode_constrained_int(s, (int64_t)value, 0, (int64_t)(value_count - 1));
}

per_error_t per_decode_enumerated(per_stream_t *s, uint32_t *out, uint32_t value_count) {
    int64_t v;
    per_error_t err = per_decode_constrained_int(s, &v, 0, (int64_t)(value_count - 1));
    if (err) return err;
    *out = (uint32_t)v;
    return PER_OK;
}
