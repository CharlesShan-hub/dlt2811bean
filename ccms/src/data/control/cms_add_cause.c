#include "data/control/cms_add_cause.h"
#include "data/basic/cms_enumerated.h"

/* ---- internal stream version ---- */

int cms_add_cause_encode_stream(per_stream_t *s, const cms_int32_t *v)
    { return cms_enumerated_encode_stream(s, v); }
int cms_add_cause_decode_stream(per_stream_t *s, cms_int32_t *v)
    { return cms_enumerated_decode_stream(s, v); }

/* ---- public buffer version ---- */

CMS_EXPORT int cms_add_cause_encode(const cms_int32_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_add_cause_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_add_cause_decode(cms_int32_t *v, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_add_cause_decode_stream(&r, v); }
