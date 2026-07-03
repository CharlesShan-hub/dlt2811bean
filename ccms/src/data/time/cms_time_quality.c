#include "data/time/cms_time_quality.h"
#include "data/string/cms_bit_string.h"
#include <string.h>

static uint8_t pack_time_quality(const cms_time_quality_t *q) {
    uint8_t byte = 0;
    if (q->leap_seconds_known && q->leap_seconds_known->value)      byte |= 0x01;
    if (q->clock_failure && q->clock_failure->value)                byte |= 0x02;
    if (q->clock_not_synchronized && q->clock_not_synchronized->value) byte |= 0x04;
    if (q->precision)                                                byte |= (q->precision->value & 0x1F) << 3;
    return byte;
}

static void unpack_time_quality(uint8_t byte, cms_time_quality_t *q) {
    if (q->leap_seconds_known)      q->leap_seconds_known->value      = (byte >> 0) & 1;
    if (q->clock_failure)           q->clock_failure->value           = (byte >> 1) & 1;
    if (q->clock_not_synchronized)  q->clock_not_synchronized->value  = (byte >> 2) & 1;
    if (q->precision)               q->precision->value               = (byte >> 3) & 0x1F;
}

int cms_time_quality_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_time_quality((const cms_time_quality_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 8);
}

int cms_time_quality_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 8);
    if (err) return CMS_ERR;
    if (ptr) unpack_time_quality(byte, (cms_time_quality_t*)ptr);
    return CMS_OK;
}

int cms_time_quality_encode(const void *ptr, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_time_quality_encode_stream(&s, ptr);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_time_quality_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_time_quality_decode_stream(&s, ptr);
}
