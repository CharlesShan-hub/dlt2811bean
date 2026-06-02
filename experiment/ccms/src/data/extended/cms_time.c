#include "data/extended/cms_time.h"

/* ---- internal stream version ---- */

int cms_utc_time_encode_stream(per_stream_t *s, const cms_utc_time_t *t)
{
    uint8_t bytes[8];
    uint32_t sec = t->seconds_since_epoch;
    uint32_t frac = t->fraction_of_second;
    uint8_t tq = t->time_quality;
    bytes[0] = (uint8_t)(sec >> 24);
    bytes[1] = (uint8_t)(sec >> 16);
    bytes[2] = (uint8_t)(sec >> 8);
    bytes[3] = (uint8_t)(sec);
    bytes[4] = (uint8_t)(frac >> 16);
    bytes[5] = (uint8_t)(frac >> 8);
    bytes[6] = (uint8_t)(frac);
    bytes[7] = tq;
    per_encode_octet_string_fixed(s, bytes, 8);
    return CMS_OK;
}
int cms_utc_time_decode_stream(per_stream_t *s, cms_utc_time_t *t)
{
    uint8_t bytes[8];
    per_decode_octet_string_fixed(s, bytes, 8);
    t->seconds_since_epoch = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
                           | ((uint32_t)bytes[2] << 8)  | (uint32_t)bytes[3];
    t->fraction_of_second = ((uint32_t)bytes[4] << 16) | ((uint32_t)bytes[5] << 8)
                           | (uint32_t)bytes[6];
    t->time_quality = bytes[7];
    return CMS_OK;
}

int cms_binary_time_encode_stream(per_stream_t *s, int32_t hour, int32_t minute, int32_t second, int32_t millisecond)
{
    int64_t msOfDay = (int64_t)hour * 3600000 + (int64_t)minute * 60000
                    + (int64_t)second * 1000 + millisecond;
    per_encode_constrained_int(s, msOfDay, 0, 86400000);
    per_encode_constrained_int(s, 0, 0, 65535);
    return CMS_OK;
}
int cms_binary_time_decode_stream(per_stream_t *s, int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond)
{
    int64_t ms;
    per_decode_constrained_int(s, &ms, 0, 86400000);
    int64_t _dummy;
    per_decode_constrained_int(s, &_dummy, 0, 65535);
    *hour = (int32_t)(ms / 3600000); ms %= 3600000;
    *minute = (int32_t)(ms / 60000); ms %= 60000;
    *second = (int32_t)(ms / 1000);
    *millisecond = (int32_t)(ms % 1000);
    return CMS_OK;
}

int cms_time_quality_encode_stream(per_stream_t *s, const uint8_t value[1])
    { per_encode_bit_string_fixed(s, value, 8); return CMS_OK; }
int cms_time_quality_decode_stream(per_stream_t *s, uint8_t value[1])
    { per_decode_bit_string_fixed(s, value, 8); return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_utc_time_encode(const cms_utc_time_t *t, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_utc_time_encode_stream(&w, t); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_utc_time_decode(const uint8_t *b, int l, cms_utc_time_t *t)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_utc_time_decode_stream(&r, t); return CMS_OK; }
CMS_EXPORT int cms_binary_time_encode(int32_t h, int32_t m, int32_t s, int32_t ms, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_binary_time_encode_stream(&w, h, m, s, ms); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_binary_time_decode(const uint8_t *b, int l, int32_t *h, int32_t *m, int32_t *s, int32_t *ms)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_binary_time_decode_stream(&r, h, m, s, ms); return CMS_OK; }
CMS_EXPORT int cms_time_quality_encode(const uint8_t v[1], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_time_quality_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_time_quality_decode(const uint8_t *b, int l, uint8_t v[1])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_time_quality_decode_stream(&r, v); return CMS_OK; }
