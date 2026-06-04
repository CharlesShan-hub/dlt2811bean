#include "data/common/cms_entry_id.h"
#include "per/cms_string.h"

int cms_entry_id_encode_stream(per_stream_t *s, const cms_entry_id_t *v)
    { return per_encode_octet_string_fixed(s, v->value, 8); }
int cms_entry_id_decode_stream(per_stream_t *s, cms_entry_id_t *v)
    { return per_decode_octet_string_fixed(s, v->value, 8); }

CMS_EXPORT int cms_entry_id_encode(const cms_entry_id_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_entry_id_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_entry_id_decode(cms_entry_id_t *v, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_entry_id_decode_stream(&r, v); }
