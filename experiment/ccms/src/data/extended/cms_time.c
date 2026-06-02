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

int cms_binary_time_encode_stream(per_stream_t *s, uint32_t msOfDay, uint16_t daysSince1984)
{
    uint8_t bytes[6];
    bytes[0] = (uint8_t)(msOfDay >> 24);
    bytes[1] = (uint8_t)(msOfDay >> 16);
    bytes[2] = (uint8_t)(msOfDay >> 8);
    bytes[3] = (uint8_t)(msOfDay);
    bytes[4] = (uint8_t)(daysSince1984 >> 8);
    bytes[5] = (uint8_t)(daysSince1984);
    per_encode_octet_string_fixed(s, bytes, 6);
    return CMS_OK;
}
int cms_binary_time_decode_stream(per_stream_t *s, uint32_t *msOfDay, uint16_t *daysSince1984)
{
    uint8_t bytes[6];
    per_decode_octet_string_fixed(s, bytes, 6);
    *msOfDay = ((uint32_t)bytes[0] << 24) | ((uint32_t)bytes[1] << 16)
             | ((uint32_t)bytes[2] << 8)  | (uint32_t)bytes[3];
    *daysSince1984 = (uint16_t)(((uint16_t)bytes[4] << 8) | (uint16_t)bytes[5]);
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
CMS_EXPORT int cms_binary_time_encode(uint32_t md, uint16_t ds, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_binary_time_encode_stream(&w, md, ds); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_binary_time_decode(const uint8_t *b, int l, uint32_t *md, uint16_t *ds)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_binary_time_decode_stream(&r, md, ds); return CMS_OK; }
CMS_EXPORT int cms_time_quality_encode(const uint8_t v[1], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_time_quality_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_time_quality_decode(const uint8_t *b, int l, uint8_t v[1])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_time_quality_decode_stream(&r, v); return CMS_OK; }
