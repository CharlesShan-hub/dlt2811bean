#include "data/block/cms_rcb_opt_flds.h"

static uint16_t pack_rcb(const cms_rcb_opt_flds_t *q) {
    uint16_t b = 0;
    /* bit 0: reserved, always 0 */
    if (q->sequence_number)       b |= (q->sequence_number->value      ? 1 : 0) << 1;
    if (q->report_time_stamp)     b |= (q->report_time_stamp->value    ? 1 : 0) << 2;
    if (q->reason_for_inclusion)  b |= (q->reason_for_inclusion->value ? 1 : 0) << 3;
    if (q->data_set_name)         b |= (q->data_set_name->value        ? 1 : 0) << 4;
    if (q->data_reference)        b |= (q->data_reference->value       ? 1 : 0) << 5;
    if (q->buffer_overflow)       b |= (q->buffer_overflow->value      ? 1 : 0) << 6;
    if (q->entry_id)              b |= (q->entry_id->value             ? 1 : 0) << 7;
    if (q->conf_revision)         b |= (q->conf_revision->value        ? 1 : 0) << 8;
    if (q->segmentation)          b |= (q->segmentation->value         ? 1 : 0) << 9;
    return b;
}

static void unpack_rcb(uint16_t bits, cms_rcb_opt_flds_t *q) {
    /* bit 0: reserved, ignored */
    if (q->sequence_number)       q->sequence_number->value      = (bits >> 1) & 1;
    if (q->report_time_stamp)     q->report_time_stamp->value    = (bits >> 2) & 1;
    if (q->reason_for_inclusion)  q->reason_for_inclusion->value = (bits >> 3) & 1;
    if (q->data_set_name)         q->data_set_name->value        = (bits >> 4) & 1;
    if (q->data_reference)        q->data_reference->value       = (bits >> 5) & 1;
    if (q->buffer_overflow)       q->buffer_overflow->value      = (bits >> 6) & 1;
    if (q->entry_id)              q->entry_id->value             = (bits >> 7) & 1;
    if (q->conf_revision)         q->conf_revision->value        = (bits >> 8) & 1;
    if (q->segmentation)          q->segmentation->value         = (bits >> 9) & 1;
}

int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const void *ptr) {
    uint16_t bits = pack_rcb((const cms_rcb_opt_flds_t*)ptr);
    uint8_t buf[2] = { (uint8_t)(bits >> 8), (uint8_t)(bits) };
    return cms_bit_string_fixed_encode_stream(s, buf, 10);
}

int cms_rcb_opt_flds_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t buf[2];
    int err = cms_bit_string_fixed_decode_stream(s, buf, 10);
    if (err) return CMS_ERR;
    uint16_t bits = ((uint16_t)buf[0] << 8) | buf[1];
    unpack_rcb(bits, (cms_rcb_opt_flds_t*)ptr);
    return CMS_OK;
}

int cms_rcb_opt_flds_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_rcb_opt_flds_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_rcb_opt_flds_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_rcb_opt_flds_decode_stream(&s, ptr);
}
