#include "data/block/cms_msvcb_opt_flds.h"
#include "data/string/cms_bitutil.h"

static uint8_t pack_msvcb(const cms_msvcb_opt_flds_t *q) {
    uint8_t b = 0;
    if (q->refresh_time)    pack_bit(&b, 0, q->refresh_time->value);
    /* bit 1: reserved */
    if (q->sample_rate)     pack_bit(&b, 2, q->sample_rate->value);
    if (q->data_set_name)   pack_bit(&b, 3, q->data_set_name->value);
    if (q->security)        pack_bit(&b, 4, q->security->value);
    return b;
}

static void unpack_msvcb(uint8_t byte, cms_msvcb_opt_flds_t *q) {
    if (q->refresh_time)    q->refresh_time->value   = unpack_bit(byte, 0);
    /* bit 1: reserved */
    if (q->sample_rate)     q->sample_rate->value    = unpack_bit(byte, 2);
    if (q->data_set_name)   q->data_set_name->value  = unpack_bit(byte, 3);
    if (q->security)        q->security->value       = unpack_bit(byte, 4);
}

int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_msvcb((const cms_msvcb_opt_flds_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 5);
}

int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 5);
    if (err) return CMS_ERR;
    if (ptr) unpack_msvcb(byte, (cms_msvcb_opt_flds_t*)ptr);
    return CMS_OK;
}

int cms_msvcb_opt_flds_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_msvcb_opt_flds_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_msvcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_msvcb_opt_flds_decode_stream(&s, ptr);
}
