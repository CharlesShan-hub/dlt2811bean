#include "data/time/cms_time_quality.h"
#include <string.h>

static uint8_t pack_time_quality(const cms_time_quality_t *q) {
    return (uint8_t)(
        (q->leap_seconds_known.value      ? 0x01 : 0) |
        (q->clock_failure.value           ? 0x02 : 0) |
        (q->clock_not_synchronized.value  ? 0x04 : 0) |
        ((q->precision.value & 0x1F) << 3)
    );
}

static void unpack_time_quality(uint8_t byte, cms_time_quality_t *q) {
    q->leap_seconds_known.value      = (byte >> 0) & 1;
    q->clock_failure.value           = (byte >> 1) & 1;
    q->clock_not_synchronized.value  = (byte >> 2) & 1;
    q->precision.value               = (byte >> 3) & 0x1F;
}

int cms_time_quality_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t byte = pack_time_quality((const cms_time_quality_t*)ptr);
    return cms_bit_string_fixed_encode_stream(s, &byte, 8);
}

int cms_time_quality_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t byte = 0;
    int err = cms_bit_string_fixed_decode_stream(s, &byte, 8);
    if (err) return CMS_ERR;
    unpack_time_quality(byte, (cms_time_quality_t*)ptr);
    return CMS_OK;
}

int cms_time_quality_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_time_quality_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_time_quality_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_time_quality_decode_stream(&s, ptr);
}
