#include "data/common/cms_time_stamp.h"

/* ---- internal stream version ----
 * TimeStamp is an alias for UtcTime — delegate to cms_utc_time functions.
 */

int cms_time_stamp_encode_stream(per_stream_t *s, const cms_utc_time_t *t)
    { return cms_utc_time_encode_stream(s, t); }

int cms_time_stamp_decode_stream(per_stream_t *s, cms_utc_time_t *t)
    { return cms_utc_time_decode_stream(s, t); }

int cms_entry_id_encode_stream(per_stream_t *s, const uint8_t value[8])
    { per_encode_octet_string_fixed(s, value, 8); return CMS_OK; }
int cms_entry_id_decode_stream(per_stream_t *s, uint8_t value[8])
    { per_decode_octet_string_fixed(s, value, 8); return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_time_stamp_encode(const cms_utc_time_t *t, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_time_stamp_encode_stream(&w, t); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_time_stamp_decode(const uint8_t *b, int l, cms_utc_time_t *t)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_time_stamp_decode_stream(&r, t); return CMS_OK; }
CMS_EXPORT int cms_entry_id_encode(const uint8_t v[8], uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_entry_id_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_entry_id_decode(const uint8_t *b, int l, uint8_t v[8])
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_entry_id_decode_stream(&r, v); return CMS_OK; }
