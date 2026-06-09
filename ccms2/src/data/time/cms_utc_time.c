#include "data/time/cms_utc_time.h"
#include "data/string/cms_octet_string.h"
#include <string.h>

static void pack_utc_time(const cms_utc_time_t *t, uint8_t out[8]) {
    uint32_t secs = t->seconds_since_epoch.value;
    uint32_t frac = t->fraction_of_second.value;
    uint8_t tq = (uint8_t)(
        (t->time_quality.leap_seconds_known.value      ? 0x01 : 0) |
        (t->time_quality.clock_failure.value           ? 0x02 : 0) |
        (t->time_quality.clock_not_synchronized.value  ? 0x04 : 0) |
        ((t->time_quality.precision.value & 0x1F) << 3)
    );
    out[0] = (uint8_t)(secs >> 24);
    out[1] = (uint8_t)(secs >> 16);
    out[2] = (uint8_t)(secs >> 8);
    out[3] = (uint8_t)(secs);
    out[4] = (uint8_t)(frac >> 16);
    out[5] = (uint8_t)(frac >> 8);
    out[6] = (uint8_t)(frac);
    out[7] = tq;
}

static void unpack_utc_time(const uint8_t in[8], cms_utc_time_t *t) {
    t->seconds_since_epoch.value = ((uint32_t)in[0] << 24) | ((uint32_t)in[1] << 16) |
                                   ((uint32_t)in[2] << 8)  | (uint32_t)in[3];
    t->fraction_of_second.value  = ((uint32_t)in[4] << 16) | ((uint32_t)in[5] << 8) | (uint32_t)in[6];
    t->time_quality.leap_seconds_known.value      = (in[7] >> 0) & 1;
    t->time_quality.clock_failure.value           = (in[7] >> 1) & 1;
    t->time_quality.clock_not_synchronized.value  = (in[7] >> 2) & 1;
    t->time_quality.precision.value               = (in[7] >> 3) & 0x1F;
}

int cms_utc_time_encode_stream(per_stream_t *s, const void *ptr) {
    uint8_t buf[8];
    pack_utc_time((const cms_utc_time_t*)ptr, buf);
    return cms_octet_string_fixed_encode_stream(s, buf, 8);
}

int cms_utc_time_decode_stream(per_stream_t *s, void *ptr) {
    uint8_t buf[8];
    int err = cms_octet_string_fixed_decode_stream(s, buf, 8);
    if (err) return CMS_ERR;
    unpack_utc_time(buf, (cms_utc_time_t*)ptr);
    return CMS_OK;
}

int cms_utc_time_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_utc_time_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_utc_time_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_utc_time_decode_stream(&s, ptr);
}
