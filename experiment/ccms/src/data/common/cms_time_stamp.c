#include "data/common/cms_time_stamp.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>
#include <stdlib.h>

CMS_EXPORT int cms_time_stamp_encode(int64_t seconds_since_epoch, int64_t fractional, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, seconds_since_epoch, -2147483648, 2147483647);
    per_encode_constrained_int(&w, fractional, 0, 16777215);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_time_stamp_decode(const uint8_t *in_buf, int in_len, int64_t *seconds_since_epoch, int64_t *fractional)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_constrained_int(&r, seconds_since_epoch, -2147483648, 2147483647);
    per_decode_constrained_int(&r, fractional, 0, 16777215);
    return CMS_OK;
}

CMS_EXPORT int cms_entry_id_encode(const uint8_t value[8], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, value, 8);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_entry_id_decode(const uint8_t *in_buf, int in_len, uint8_t value[8])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, value, 8);
    return CMS_OK;
}