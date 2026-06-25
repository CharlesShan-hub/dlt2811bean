#include "data/common/cms_quality.h"
#include "data/string/cms_bitutil.h"
#include <string.h>

/* Pack 13 quality bits into a 2-byte array */
static void pack_quality(const cms_quality_t *q, uint8_t out[2]) {
    uint16_t bits = 0;
    /* validity is 2 bits: high bit first */
    if (q->validity) {
        pack_bit16(&bits, 0, (q->validity->value >> 1) & 1);
        pack_bit16(&bits, 1, (q->validity->value) & 1);
    }
    if (q->overflow)        pack_bit16(&bits, 2,  q->overflow->value);
    if (q->outOfRange)      pack_bit16(&bits, 3,  q->outOfRange->value);
    if (q->badReference)    pack_bit16(&bits, 4,  q->badReference->value);
    if (q->oscillatory)     pack_bit16(&bits, 5,  q->oscillatory->value);
    if (q->failure)         pack_bit16(&bits, 6,  q->failure->value);
    if (q->oldData)         pack_bit16(&bits, 7,  q->oldData->value);
    if (q->inconsistent)    pack_bit16(&bits, 8,  q->inconsistent->value);
    if (q->inaccurate)      pack_bit16(&bits, 9,  q->inaccurate->value);
    if (q->substituted)     pack_bit16(&bits, 10, q->substituted->value);
    if (q->test)            pack_bit16(&bits, 11, q->test->value);
    if (q->operatorBlocked) pack_bit16(&bits, 12, q->operatorBlocked->value);
    out[0] = (uint8_t)(bits >> 8);
    out[1] = (uint8_t)(bits);
}

static void unpack_quality(const uint8_t in[2], cms_quality_t *q) {
    uint16_t bits = ((uint16_t)in[0] << 8) | in[1];
    if (q->validity)        q->validity->value          = (unpack_bit16(bits, 0) << 1) | unpack_bit16(bits, 1);
    if (q->overflow)        q->overflow->value          = unpack_bit16(bits, 2);
    if (q->outOfRange)      q->outOfRange->value        = unpack_bit16(bits, 3);
    if (q->badReference)    q->badReference->value      = unpack_bit16(bits, 4);
    if (q->oscillatory)     q->oscillatory->value       = unpack_bit16(bits, 5);
    if (q->failure)         q->failure->value           = unpack_bit16(bits, 6);
    if (q->oldData)         q->oldData->value           = unpack_bit16(bits, 7);
    if (q->inconsistent)    q->inconsistent->value      = unpack_bit16(bits, 8);
    if (q->inaccurate)      q->inaccurate->value        = unpack_bit16(bits, 9);
    if (q->substituted)     q->substituted->value       = unpack_bit16(bits, 10);
    if (q->test)            q->test->value              = unpack_bit16(bits, 11);
    if (q->operatorBlocked) q->operatorBlocked->value   = unpack_bit16(bits, 12);
}

int cms_quality_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t buf[2];
    pack_quality((const cms_quality_t*)ptr, buf);
    return cms_bit_string_fixed_encode_stream(s, buf, 13);
}

int cms_quality_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t buf[2];
    int err = cms_bit_string_fixed_decode_stream(s, buf, 13);
    if (err) return CMS_ERR;
    unpack_quality(buf, (cms_quality_t*)ptr);
    return CMS_OK;
}

int cms_quality_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_quality_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_quality_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_quality_decode_stream(&s, ptr);
}
