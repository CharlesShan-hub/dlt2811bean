#include "data/control/cms_check.h"
#include "data/string/cms_bitutil.h"
#include <string.h>

static uint8_t pack_check(const cms_check_t *q) {
    uint8_t b = 0;
    if (q->syncheck && q->syncheck->value)
        pack_bit(&b, 0, 1);
    if (q->interlock_check && q->interlock_check->value)
        pack_bit(&b, 1, 1);
    return b;
}

static void unpack_check(uint8_t byte, cms_check_t *q) {
    if (q->syncheck)
        q->syncheck->value = unpack_bit(byte, 0);
    if (q->interlock_check)
        q->interlock_check->value = unpack_bit(byte, 1);
}

int cms_check_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_check((const cms_check_t *) ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 2);
}

int cms_check_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 2);
    if (err)
        return CMS_ERR;
    if (ptr)
        unpack_check(byte, (cms_check_t *) ptr);
    return CMS_OK;
}

int cms_check_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err)
        return (int) err;
    int rc = cms_check_encode_stream(&s, ptr);
    if (rc) {
        per_stream_free(&s);
        return rc;
    }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_check_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    return cms_check_decode_stream(&s, ptr);
}
