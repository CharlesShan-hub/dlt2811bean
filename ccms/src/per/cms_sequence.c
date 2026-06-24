#include "per/cms_sequence.h"
#include "per/cms_integer.h"

per_error_t per_encode_optional_bitmap(per_stream_t *s, uint64_t bitmap, int nfields) {
    if (nfields <= 0) return PER_OK;
    if (nfields > 64) return PER_ERR_INVALID_ARG;

    per_stream_align(s);
    for (int i = 0; i < nfields; i++) {
        int bit = (int)((bitmap >> i) & 1ULL);
        per_error_t err = per_stream_write_bit(s, bit);
        if (err) return err;
    }
    return PER_OK;
}

per_error_t per_decode_optional_bitmap(per_stream_t *s, uint64_t *bitmap, int nfields) {
    if (nfields <= 0) { *bitmap = 0; return PER_OK; }
    if (nfields > 64) return PER_ERR_INVALID_ARG;

    per_stream_align(s);
    uint64_t result = 0;
    for (int i = 0; i < nfields; i++) {
        int bit;
        per_error_t err = per_stream_read_bit(s, &bit);
        if (err) return err;
        if (bit) result |= (1ULL << i);
    }
    *bitmap = result;
    return PER_OK;
}
