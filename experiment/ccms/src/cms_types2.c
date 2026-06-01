#include "cms_types2.h"
#include "per_stream.h"
#include "per_integer.h"
#include "per_string.h"
#include "per_bit_string.h"
#include <string.h>
#include <stdlib.h>

/* 7.2.1 UtcTime */
CMS_EXPORT int cms_utc_time_encode(
    int64_t timestamp_ms,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    uint8_t bytes[8];
    uint64_t ms = (uint64_t)timestamp_ms;
    bytes[0] = (uint8_t)(ms >> 56);
    bytes[1] = (uint8_t)(ms >> 48);
    bytes[2] = (uint8_t)(ms >> 40);
    bytes[3] = (uint8_t)(ms >> 32);
    bytes[4] = (uint8_t)(ms >> 24);
    bytes[5] = (uint8_t)(ms >> 16);
    bytes[6] = (uint8_t)(ms >> 8);
    bytes[7] = (uint8_t)(ms);
    per_encode_octet_string_fixed(&w, bytes, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_utc_time_decode(
    const uint8_t *in_buf, int in_len,
    int64_t *timestamp_ms)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    uint8_t bytes[8];
    per_decode_octet_string_fixed(&r, bytes, 8);
    uint64_t ms = ((uint64_t)bytes[0] << 56) | ((uint64_t)bytes[1] << 48)
                | ((uint64_t)bytes[2] << 40) | ((uint64_t)bytes[3] << 32)
                | ((uint64_t)bytes[4] << 24) | ((uint64_t)bytes[5] << 16)
                | ((uint64_t)bytes[6] << 8)  | (uint64_t)bytes[7];
    *timestamp_ms = (int64_t)ms;
    return CMS_OK;
}

/* 7.2.2 BinaryTime / EntryTime */
CMS_EXPORT int cms_binary_time_encode(
    int32_t hour, int32_t minute, int32_t second, int32_t millisecond,
    uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);

    int64_t msOfDay = (int64_t)hour * 3600000 + (int64_t)minute * 60000
                    + (int64_t)second * 1000 + millisecond;
    per_encode_constrained_int(&w, msOfDay, 0, 86400000);
    per_encode_constrained_int(&w, 0, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_binary_time_decode(
    const uint8_t *in_buf, int in_len,
    int32_t *hour, int32_t *minute, int32_t *second, int32_t *millisecond)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);

    int64_t ms;
    per_decode_constrained_int(&r, &ms, 0, 86400000);
    int64_t _dummy;
    per_decode_constrained_int(&r, &_dummy, 0, 65535);

    *hour = (int32_t)(ms / 3600000); ms %= 3600000;
    *minute = (int32_t)(ms / 60000); ms %= 60000;
    *second = (int32_t)(ms / 1000);
    *millisecond = (int32_t)(ms % 1000);
    return CMS_OK;
}

/* 7.2.1 TimeQuality */
CMS_EXPORT int cms_time_quality_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 3);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_time_quality_decode(const uint8_t *in_buf, int in_len, uint8_t value[1])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 3);
    return CMS_OK;
}
