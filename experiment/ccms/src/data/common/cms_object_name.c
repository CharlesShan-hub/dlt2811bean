#include "data/common/cms_object_name.h"
#include "per/cms_stream.h"
#include "per/cms_string.h"
#include <string.h>
#include <stdlib.h>

CMS_EXPORT int cms_object_name_encode(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 64);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_object_name_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

CMS_EXPORT int cms_object_reference_encode(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 129);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_object_reference_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}

CMS_EXPORT int cms_sub_reference_encode(const char *value, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_visible_string(&w, value, 129);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_sub_reference_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_visible_string(&r, value, (uint32_t)*value_cap);
    *value_cap = (int)strlen(value);
    return CMS_OK;
}