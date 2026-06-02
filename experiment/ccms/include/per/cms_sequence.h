#ifndef CMSPER_SEQUENCE_H
#define CMSPER_SEQUENCE_H

#include "per/cms_stream.h"
#include "per/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/* ==================== SEQUENCE helpers ==================== */

static inline per_error_t per_encode_optional(per_stream_t *s, int present) {
    return per_stream_write_bit(s, present ? 1 : 0);
}

static inline int per_decode_optional(per_stream_t *s) {
    int bit;
    per_stream_read_bit(s, &bit);
    return bit;
}

static inline per_error_t per_encode_sequence_of_count(per_stream_t *s, uint32_t count, uint32_t max_count) {
    return per_encode_constrained_int(s, (int64_t)count, 0, (int64_t)max_count);
}

static inline per_error_t per_decode_sequence_of_count(per_stream_t *s, uint32_t *count, uint32_t max_count) {
    int64_t v;
    per_error_t err = per_decode_constrained_int(s, &v, 0, (int64_t)max_count);
    if (err) return err;
    *count = (uint32_t)v;
    return PER_OK;
}

#ifdef __cplusplus
}
#endif

#endif
