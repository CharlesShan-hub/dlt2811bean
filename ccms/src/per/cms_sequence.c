#include "per/cms_sequence.h"

per_error_t per_encode_optional_bitmap(per_stream_t *s, const bool *flags, int nfields) {
    if (nfields <= 0)
        return PER_OK;
    if (nfields > 64)
        return PER_ERR_INVALID_ARG;

    per_stream_align(s);
    for (int i = 0; i < nfields; i++) {
        per_error_t err = per_stream_write_bit(s, flags[i] ? 1 : 0);
        if (err)
            return err;
    }
    return PER_OK;
}

per_error_t per_decode_optional_bitmap(per_stream_t *s, bool *flags, int nfields) {
    if (nfields <= 0)
        return PER_OK;
    if (nfields > 64)
        return PER_ERR_INVALID_ARG;

    per_stream_align(s);
    for (int i = 0; i < nfields; i++) {
        int bit;
        per_error_t err = per_stream_read_bit(s, &bit);
        if (err)
            return err;
        flags[i] = (bit != 0);
    }
    return PER_OK;
}
