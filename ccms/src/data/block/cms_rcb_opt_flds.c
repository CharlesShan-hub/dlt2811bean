#include "data/block/cms_rcb_opt_flds.h"
#include "data/string/cms_bitutil.h"

static uint16_t pack_rcb(const cms_rcb_opt_flds_t *q) {
    uint16_t b = 0;
    /* bit 0: reserved */
    if (q->sequence_number)       pack_bit16(&b, 1, q->sequence_number->value);
    if (q->report_time_stamp)     pack_bit16(&b, 2, q->report_time_stamp->value);
    if (q->reason_for_inclusion)  pack_bit16(&b, 3, q->reason_for_inclusion->value);
    if (q->data_set_name)         pack_bit16(&b, 4, q->data_set_name->value);
    if (q->data_reference)        pack_bit16(&b, 5, q->data_reference->value);
    if (q->buffer_overflow)       pack_bit16(&b, 6, q->buffer_overflow->value);
    if (q->entry_id)              pack_bit16(&b, 7, q->entry_id->value);
    if (q->conf_revision)         pack_bit16(&b, 8, q->conf_revision->value);
    if (q->segmentation)          pack_bit16(&b, 9, q->segmentation->value);
    return b;
}

static void unpack_rcb(uint16_t bits, cms_rcb_opt_flds_t *q) {
    /* bit 0: reserved */
    if (q->sequence_number)       q->sequence_number->value      = unpack_bit16(bits, 1);
    if (q->report_time_stamp)     q->report_time_stamp->value    = unpack_bit16(bits, 2);
    if (q->reason_for_inclusion)  q->reason_for_inclusion->value = unpack_bit16(bits, 3);
    if (q->data_set_name)         q->data_set_name->value        = unpack_bit16(bits, 4);
    if (q->data_reference)        q->data_reference->value       = unpack_bit16(bits, 5);
    if (q->buffer_overflow)       q->buffer_overflow->value      = unpack_bit16(bits, 6);
    if (q->entry_id)              q->entry_id->value             = unpack_bit16(bits, 7);
    if (q->conf_revision)         q->conf_revision->value        = unpack_bit16(bits, 8);
    if (q->segmentation)          q->segmentation->value         = unpack_bit16(bits, 9);
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
