#include "data/block/cms_reason_code.h"
#include "data/string/cms_bitutil.h"

static uint8_t pack_reason(const cms_reason_code_t *q) {
    uint8_t b = 0;
    /* bit 0: reserved */
    if (q->data_change)              pack_bit(&b, 1, q->data_change->value);
    if (q->quality_change)           pack_bit(&b, 2, q->quality_change->value);
    if (q->data_update)              pack_bit(&b, 3, q->data_update->value);
    if (q->integrity)                pack_bit(&b, 4, q->integrity->value);
    if (q->general_interrogation)    pack_bit(&b, 5, q->general_interrogation->value);
    if (q->application_trigger)      pack_bit(&b, 6, q->application_trigger->value);
    return b;
}

static void unpack_reason(uint8_t byte, cms_reason_code_t *q) {
    /* bit 0: reserved */
    if (q->data_change)              q->data_change->value           = unpack_bit(byte, 1);
    if (q->quality_change)           q->quality_change->value        = unpack_bit(byte, 2);
    if (q->data_update)              q->data_update->value           = unpack_bit(byte, 3);
    if (q->integrity)                q->integrity->value             = unpack_bit(byte, 4);
    if (q->general_interrogation)    q->general_interrogation->value = unpack_bit(byte, 5);
    if (q->application_trigger)      q->application_trigger->value   = unpack_bit(byte, 6);
}

int cms_reason_code_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_reason((const cms_reason_code_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 7);
}

int cms_reason_code_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 7);
    if (err) return CMS_ERR;
    if (ptr) unpack_reason(byte, (cms_reason_code_t*)ptr);
    return CMS_OK;
}

int cms_reason_code_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_reason_code_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_reason_code_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_reason_code_decode_stream(&s, ptr);
}
