#include "data/basic/cms_packed_list.h"
#include "per/cms_bit_string.h"
#include "per/cms_stream.h"
#include <string.h>
#include <stdlib.h>

/* 7.1.8 PackedList */
CMS_EXPORT int cms_packed_list_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_bit_string(&w, value, value_len * 8, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_packed_list_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    int out_nbits = *value_cap * 8;
    per_decode_bit_string(&r, value, &out_nbits, 65535);
    *value_cap = (out_nbits + 7) / 8;
    return CMS_OK;
}