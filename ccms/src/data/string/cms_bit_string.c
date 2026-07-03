#include "data/string/cms_bit_string.h"
#include <string.h>

#define ARRAY_PTR(v) (*(const uint8_t *const *) (v))
#define ARRAY_LEN(v) (*((const int32_t *) ((const uint8_t *) (v) + 8)))
#define ARRAY_PTR_MUT(v) (*(uint8_t **) (v))
#define ARRAY_LEN_PTR(v) ((int32_t *) ((uint8_t *) (v) + 8))

/* Fixed length: align + bits (no length prefix)
 * Callers are responsible for MSB-first packing via pack_bit/pack_bit16. */
int cms_bit_string_fixed_encode_stream(per_stream_t *s, const uint8_t *data, int fixed_nbits) {
    return (int) per_encode_bit_string_fixed(s, data, fixed_nbits);
}

int cms_bit_string_fixed_decode_stream(per_stream_t *s, uint8_t *out, int fixed_nbits) {
    uint8_t tmp[64];
    uint8_t *target = out ? out : tmp;
    return (int) per_decode_bit_string_fixed(s, target, fixed_nbits);
}

/* Variable length: SIZE(0..max_nbits) */
int cms_bit_string_encode_stream(per_stream_t *s, const void *ptr, uint32_t max_nbits) {
    const uint8_t *vptr = ptr ? ARRAY_PTR(ptr) : NULL;
    int32_t nbits = ptr ? ARRAY_LEN(ptr) : 0;
    if (!vptr || nbits < 0)
        return CMS_ERR;
    if (max_nbits > 0)
        return (int) per_encode_bit_string(s, vptr, nbits, (int) max_nbits);
    return (int) per_encode_bit_string_unconstrained(s, vptr, nbits);
}

int cms_bit_string_decode_stream(per_stream_t *s, void *ptr, uint32_t max_nbits) {
    int out_nbits = 0;
    uint8_t tmp[64];
    per_error_t err;
    if (max_nbits > 0) {
        err = per_decode_bit_string(s, tmp, &out_nbits, (int) max_nbits);
    } else {
        err = per_decode_bit_string_unconstrained(s, tmp, &out_nbits);
    }
    if (err)
        return CMS_ERR;
    if (ptr) {
        memcpy(ARRAY_PTR_MUT(ptr), tmp, (out_nbits + 7) / 8);
        *(ARRAY_LEN_PTR(ptr)) = out_nbits;
    }
    return CMS_OK;
}

int cms_bit_string_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err)
        return (int) err;
    int rc = cms_bit_string_encode_stream(&s, ptr, 1024);
    if (rc) {
        per_stream_free(&s);
        return rc;
    }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_bit_string_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    return cms_bit_string_decode_stream(&s, ptr, 0);
}
