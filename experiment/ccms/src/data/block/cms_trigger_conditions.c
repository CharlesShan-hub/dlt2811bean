#include "data/block/cms_trigger_conditions.h"

/* ---- internal stream version ---- */

int cms_trigger_conditions_encode_stream(per_stream_t *s, const uint8_t value[1])
    { per_encode_bit_string_fixed(s, value, 6); return CMS_OK; }
int cms_trigger_conditions_decode_stream(per_stream_t *s, uint8_t value[1])
    { per_decode_bit_string_fixed(s, value, 6); return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_trigger_conditions_encode(const uint8_t value[1], uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_trigger_conditions_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_trigger_conditions_decode(const uint8_t *in_buf, int in_len, uint8_t value[1])
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_trigger_conditions_decode_stream(&r, value); return CMS_OK; }
