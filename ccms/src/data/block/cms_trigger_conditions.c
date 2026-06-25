#include "data/block/cms_trigger_conditions.h"
#include "data/string/cms_bitutil.h"

static uint8_t pack_trigger(const cms_trigger_conditions_t *q) {
    uint8_t b = 0;
    /* bit 0: reserved */
    if (q->data_change)             pack_bit(&b, 1, q->data_change->value);
    if (q->quality_change)          pack_bit(&b, 2, q->quality_change->value);
    if (q->data_update)             pack_bit(&b, 3, q->data_update->value);
    if (q->integrity)               pack_bit(&b, 4, q->integrity->value);
    if (q->general_interrogation)   pack_bit(&b, 5, q->general_interrogation->value);
    return b;
}

static void unpack_trigger(uint8_t byte, cms_trigger_conditions_t *q) {
    /* bit 0: reserved */
    if (q->data_change)             q->data_change->value           = unpack_bit(byte, 1);
    if (q->quality_change)          q->quality_change->value        = unpack_bit(byte, 2);
    if (q->data_update)             q->data_update->value           = unpack_bit(byte, 3);
    if (q->integrity)               q->integrity->value             = unpack_bit(byte, 4);
    if (q->general_interrogation)   q->general_interrogation->value = unpack_bit(byte, 5);
}

int cms_trigger_conditions_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_trigger((const cms_trigger_conditions_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 6);
}

int cms_trigger_conditions_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 6);
    if (err) return CMS_ERR;
    unpack_trigger(byte, (cms_trigger_conditions_t*)ptr);
    return CMS_OK;
}

int cms_trigger_conditions_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_trigger_conditions_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_trigger_conditions_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_trigger_conditions_decode_stream(&s, ptr);
}
