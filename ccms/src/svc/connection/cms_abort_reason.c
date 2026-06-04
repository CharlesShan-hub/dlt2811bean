#include "svc/connection/cms_abort_reason.h"

/* ---- internal stream version ---- */

int cms_abort_reason_encode_stream(per_stream_t *s, cms_abort_reason_t value)
    { per_encode_constrained_int(s, value, 0, 5); return CMS_OK; }
int cms_abort_reason_decode_stream(per_stream_t *s, cms_abort_reason_t *value)
    { int64_t t; per_decode_constrained_int(s, &t, 0, 5); *value = (cms_abort_reason_t)(int)t; return CMS_OK; }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_abort_reason_encode(cms_abort_reason_t value, uint8_t *out_buf, int *out_len)
    { per_stream_t w; per_stream_init_write(&w, out_buf, (size_t)*out_len); cms_abort_reason_encode_stream(&w, value); *out_len = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_abort_reason_decode(const uint8_t *in_buf, int in_len, cms_abort_reason_t *value)
    { per_stream_t r; per_stream_init_read(&r, in_buf, (size_t)in_len); cms_abort_reason_decode_stream(&r, value); return CMS_OK; }
