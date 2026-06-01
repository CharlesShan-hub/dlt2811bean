#include "data/block/cms_opt_flds.h"

/* ---- internal stream version ---- */

int cms_lcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[1])
    { per_encode_bit_string_fixed(s, value, 1); return CMS_OK; }
int cms_lcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[1])
    { per_decode_bit_string_fixed(s, value, 1); return CMS_OK; }
int cms_msvcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[1])
    { per_encode_bit_string_fixed(s, value, 5); return CMS_OK; }
int cms_msvcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[1])
    { per_decode_bit_string_fixed(s, value, 5); return CMS_OK; }
int cms_rcb_opt_flds_encode_stream(per_stream_t *s, const uint8_t value[2])
    { per_encode_bit_string_fixed(s, value, 10); return CMS_OK; }
int cms_rcb_opt_flds_decode_stream(per_stream_t *s, uint8_t value[2])
    { per_decode_bit_string_fixed(s, value, 10); return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_lcb_opt_flds_encode(const uint8_t v[1], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_lcb_opt_flds_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_lcb_opt_flds_decode(const uint8_t *b, int l, uint8_t v[1])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_lcb_opt_flds_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_msvcb_opt_flds_encode(const uint8_t v[1], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_msvcb_opt_flds_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_msvcb_opt_flds_decode(const uint8_t *b, int l, uint8_t v[1])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_msvcb_opt_flds_decode_stream(&r, v); return CMS_OK; }
CMS_EXPORT int cms_rcb_opt_flds_encode(const uint8_t v[2], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_rcb_opt_flds_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_rcb_opt_flds_decode(const uint8_t *b, int l, uint8_t v[2])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_rcb_opt_flds_decode_stream(&r, v); return CMS_OK; }
