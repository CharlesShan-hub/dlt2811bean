#include "data/common/cms_entry_time.h"

/* ---- internal stream version ----
 * EntryTime is an alias for BinaryTime — delegate to cms_binary_time functions.
 */

int cms_entry_time_encode_stream(per_stream_t *s, const cms_binary_time_t *t)
    { return cms_binary_time_encode_stream(s, t); }

int cms_entry_time_decode_stream(per_stream_t *s, cms_binary_time_t *t)
    { return cms_binary_time_decode_stream(s, t); }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_entry_time_encode(const cms_binary_time_t *t, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_entry_time_encode_stream(&w, t); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_entry_time_decode(const uint8_t *b, int l, cms_binary_time_t *t)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_entry_time_decode_stream(&r, t); return CMS_OK; }
