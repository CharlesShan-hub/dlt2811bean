#include "per/cms_choice.h"
#include "per/cms_integer.h"

per_error_t per_encode_choice(per_stream_t *s, uint32_t index) {
    return per_encode_small_non_negative(s, index);
}

per_error_t per_decode_choice(per_stream_t *s, uint32_t *out) {
    return per_decode_small_non_negative(s, out);
}

per_error_t per_encode_choice_extensible(per_stream_t *s, bool is_extension, uint32_t index) {
    per_error_t err = per_stream_write_bit(s, is_extension);
    if (err)
        return err;
    return per_encode_small_non_negative(s, index);
}

per_error_t per_decode_choice_extensible(per_stream_t *s, bool *is_extension, uint32_t *out) {
    int ext;
    per_error_t err = per_stream_read_bit(s, &ext);
    if (err)
        return err;
    *is_extension = (ext != 0);
    return per_decode_small_non_negative(s, out);
}
