#include "data/basic/cms_packed_list.h"
#include "per/cms_bit_string.h"
#include "per/cms_stream.h"

int cms_packed_list_encode_stream(per_stream_t *s, const uint8_t *value, int value_len)
    { per_encode_bit_string(s, value, value_len * 8, 65535); return CMS_OK; }
int cms_packed_list_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap)
    { int n = *value_cap * 8; per_decode_bit_string(s, value, &n, 65535); *value_cap = (n + 7) / 8; return CMS_OK; }

CMS_EXPORT int cms_packed_list_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_packed_list_encode_stream(&w, value, value_len); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_packed_list_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_packed_list_decode_stream(&r, value, value_cap); return CMS_OK; }
