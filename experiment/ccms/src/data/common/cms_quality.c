#include "data/common/cms_quality.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_bit_string.h"
#include <string.h>
#include <stdlib.h>

CMS_EXPORT int cms_quality_encode(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 13);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_quality_decode(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 13);
    return CMS_OK;
}

CMS_EXPORT int cms_dbpos_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_dbpos_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t tmp;
    per_decode_small_non_negative(&r, &tmp);
    *value = (int)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_tcmd_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_small_non_negative(&w, value);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_tcmd_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    uint32_t tmp;
    per_decode_small_non_negative(&r, &tmp);
    *value = (int)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_service_error_encode(int value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_constrained_int(&w, value, 0, 12);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_service_error_decode(const uint8_t *in_buf, int in_len, int *value)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 12);
    *value = (int)tmp;
    return CMS_OK;
}