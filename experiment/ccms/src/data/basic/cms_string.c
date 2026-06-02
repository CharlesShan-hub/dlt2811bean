#include "data/basic/cms_string.h"
#include <string.h>

/* ---- internal stream version ---- */

int cms_visible_string_encode_stream(per_stream_t *s, const char *value)
    { per_encode_visible_string(s, value, 255); return CMS_OK; }
int cms_visible_string_decode_stream(per_stream_t *s, char *value)
    { per_decode_visible_string(s, value, 255); return CMS_OK; }

int cms_visible_string_encode_stream_fixed(per_stream_t *s, const char *value, int fixed_len)
    { per_encode_visible_string_fixed(s, value, fixed_len); return CMS_OK; }
int cms_visible_string_decode_stream_fixed(per_stream_t *s, char *value, int fixed_len)
    { per_decode_visible_string_fixed(s, value, fixed_len); return CMS_OK; }

int cms_utf8_string_encode_stream(per_stream_t *s, const char *value)
    { per_encode_utf8_string(s, value, 255); return CMS_OK; }
int cms_utf8_string_decode_stream(per_stream_t *s, char *value)
    { per_decode_utf8_string(s, value, 255); return CMS_OK; }

int cms_utf8_string_encode_stream_fixed(per_stream_t *s, const char *value, int fixed_len)
    { per_encode_utf8_string_fixed(s, value, fixed_len); return CMS_OK; }
int cms_utf8_string_decode_stream_fixed(per_stream_t *s, char *value, int fixed_len)
    { per_decode_utf8_string_fixed(s, value, fixed_len); return CMS_OK; }

int cms_octet_string_encode_stream(per_stream_t *s, const uint8_t *value, int value_len)
    { per_encode_octet_string(s, value, value_len, 65535); return CMS_OK; }
int cms_octet_string_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap)
    { size_t l = (size_t)*value_cap; per_decode_octet_string(s, value, &l, 65535); *value_cap = (int)l; return CMS_OK; }

int cms_bit_string_encode_stream(per_stream_t *s, const uint8_t *value, int value_len)
    { per_encode_bit_string(s, value, value_len * 8, 65535); return CMS_OK; }
int cms_bit_string_decode_stream(per_stream_t *s, uint8_t *value, int *value_cap)
    { int n = *value_cap * 8; per_decode_bit_string(s, value, &n, 65535); *value_cap = (n + 7) / 8; return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_visible_string_encode(const char *value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_visible_string_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_visible_string_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_visible_string_decode_stream(&r, value); *value_cap = (int)strlen(value); return CMS_OK; }

CMS_EXPORT int cms_utf8_string_encode(const char *value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_utf8_string_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_utf8_string_decode(const uint8_t *in_buf, int in_len, char *value, int *value_cap)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_utf8_string_decode_stream(&r, value); *value_cap = (int)strlen(value); return CMS_OK; }

CMS_EXPORT int cms_octet_string_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_octet_string_encode_stream(&w, value, value_len); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_octet_string_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_octet_string_decode_stream(&r, value, value_cap); return CMS_OK; }

CMS_EXPORT int cms_bit_string_encode(const uint8_t *value, int value_len, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_bit_string_encode_stream(&w, value, value_len); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_bit_string_decode(const uint8_t *in_buf, int in_len, uint8_t *value, int *value_cap)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_bit_string_decode_stream(&r, value, value_cap); return CMS_OK; }
