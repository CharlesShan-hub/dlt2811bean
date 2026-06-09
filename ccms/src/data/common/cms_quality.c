#include "data/common/cms_quality.h"
#include <string.h>

/* Pack 13 quality bits into a 2-byte array (big-endian) */
static void pack_quality(const cms_quality_t *q, uint8_t out[2]) {
    uint16_t bits = 0;
    if (q->validity)       bits |= (uint16_t)(q->validity->value & 0x03) << 0;
    if (q->overflow)       bits |= (q->overflow->value          ? 1 : 0) << 2;
    if (q->outOfRange)     bits |= (q->outOfRange->value        ? 1 : 0) << 3;
    if (q->badReference)   bits |= (q->badReference->value      ? 1 : 0) << 4;
    if (q->oscillatory)    bits |= (q->oscillatory->value       ? 1 : 0) << 5;
    if (q->failure)        bits |= (q->failure->value           ? 1 : 0) << 6;
    if (q->oldData)        bits |= (q->oldData->value           ? 1 : 0) << 7;
    if (q->inconsistent)   bits |= (q->inconsistent->value      ? 1 : 0) << 8;
    if (q->inaccurate)     bits |= (q->inaccurate->value        ? 1 : 0) << 9;
    if (q->substituted)    bits |= (q->substituted->value       ? 1 : 0) << 10;
    if (q->test)           bits |= (q->test->value              ? 1 : 0) << 11;
    if (q->operatorBlocked) bits |= (q->operatorBlocked->value   ? 1 : 0) << 12;
    out[0] = (uint8_t)(bits >> 8);
    out[1] = (uint8_t)(bits);
}

static void unpack_quality(const uint8_t in[2], cms_quality_t *q) {
    uint16_t bits = ((uint16_t)in[0] << 8) | in[1];
    if (q->validity)        q->validity->value          = (bits >> 0) & 0x03;
    if (q->overflow)        q->overflow->value          = (bits >> 2) & 1;
    if (q->outOfRange)      q->outOfRange->value        = (bits >> 3) & 1;
    if (q->badReference)    q->badReference->value      = (bits >> 4) & 1;
    if (q->oscillatory)     q->oscillatory->value       = (bits >> 5) & 1;
    if (q->failure)         q->failure->value           = (bits >> 6) & 1;
    if (q->oldData)         q->oldData->value           = (bits >> 7) & 1;
    if (q->inconsistent)    q->inconsistent->value      = (bits >> 8) & 1;
    if (q->inaccurate)      q->inaccurate->value        = (bits >> 9) & 1;
    if (q->substituted)     q->substituted->value       = (bits >> 10) & 1;
    if (q->test)            q->test->value              = (bits >> 11) & 1;
    if (q->operatorBlocked) q->operatorBlocked->value   = (bits >> 12) & 1;
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

int cms_quality_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_quality_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_quality_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_quality_decode_stream(&s, ptr);
}
