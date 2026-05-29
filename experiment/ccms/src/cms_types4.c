#include "cms_types4.h"
#include "per_stream.h"
#include "per_bit_string.h"
#include <string.h>

/* 7.4 FC */
int cms_encode_FC(const uint8_t value[2], uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string_fixed(&w, value, 16);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

int cms_decode_FC(const uint8_t *in_buf, int in_len, uint8_t value[2])
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_bit_string_fixed(&r, value, 16);
    return CMS_OK;
}
