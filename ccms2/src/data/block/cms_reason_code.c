#include "data/block/cms_reason_code.h"

static uint8_t pack_reason(const cms_reason_code_t *q) {
    uint8_t b = 0;
    /* bit 0: reserved, always 0 */
    if (q->data_change)              b |= (q->data_change->value              ? 1 : 0) << 1;
    if (q->quality_change)           b |= (q->quality_change->value           ? 1 : 0) << 2;
    if (q->data_update)              b |= (q->data_update->value              ? 1 : 0) << 3;
    if (q->integrity)                b |= (q->integrity->value                ? 1 : 0) << 4;
    if (q->general_interrogation)    b |= (q->general_interrogation->value    ? 1 : 0) << 5;
    if (q->application_trigger)      b |= (q->application_trigger->value      ? 1 : 0) << 6;
    return b;
}

static void unpack_reason(uint8_t byte, cms_reason_code_t *q) {
    /* bit 0: reserved, ignored */
    if (q->data_change)              q->data_change->value           = (byte >> 1) & 1;
    if (q->quality_change)           q->quality_change->value        = (byte >> 2) & 1;
    if (q->data_update)              q->data_update->value           = (byte >> 3) & 1;
    if (q->integrity)                q->integrity->value             = (byte >> 4) & 1;
    if (q->general_interrogation)    q->general_interrogation->value = (byte >> 5) & 1;
    if (q->application_trigger)      q->application_trigger->value   = (byte >> 6) & 1;
}

int cms_reason_code_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_reason((const cms_reason_code_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 7);
}

int cms_reason_code_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 7);
    if (err) return CMS_ERR;
    unpack_reason(byte, (cms_reason_code_t*)ptr);
    return CMS_OK;
}

int cms_reason_code_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_reason_code_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_reason_code_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_reason_code_decode_stream(&s, ptr);
}
