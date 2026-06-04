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
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_entry_time_encode_stream(&w, t); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_entry_time_decode(cms_binary_time_t *t, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_entry_time_decode_stream(&r, t); }
