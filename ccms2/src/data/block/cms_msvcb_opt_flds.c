#include "data/block/cms_msvcb_opt_flds.h"

static uint8_t pack_msvcb(const cms_msvcb_opt_flds_t *q) {
    uint8_t b = 0;
    if (q->refresh_time)    b |= (q->refresh_time->value   ? 1 : 0) << 0;
    /* bit 1: reserved, always 0 */
    if (q->sample_rate)     b |= (q->sample_rate->value    ? 1 : 0) << 2;
    if (q->data_set_name)   b |= (q->data_set_name->value  ? 1 : 0) << 3;
    if (q->security)        b |= (q->security->value       ? 1 : 0) << 4;
    return b;
}

static void unpack_msvcb(uint8_t byte, cms_msvcb_opt_flds_t *q) {
    if (q->refresh_time)    q->refresh_time->value   = (byte >> 0) & 1;
    /* bit 1: reserved, ignored */
    if (q->sample_rate)     q->sample_rate->value    = (byte >> 2) & 1;
    if (q->data_set_name)   q->data_set_name->value  = (byte >> 3) & 1;
    if (q->security)        q->security->value       = (byte >> 4) & 1;
}

int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_msvcb((const cms_msvcb_opt_flds_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 5);
}

int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 5);
    if (err) return CMS_ERR;
    unpack_msvcb(byte, (cms_msvcb_opt_flds_t*)ptr);
    return CMS_OK;
}

int cms_msvcb_opt_flds_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_msvcb_opt_flds_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_msvcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_msvcb_opt_flds_decode_stream(&s, ptr);
}
