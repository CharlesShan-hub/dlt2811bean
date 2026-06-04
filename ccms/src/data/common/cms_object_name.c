#include "data/common/cms_object_name.h"
#include "per/cms_string.h"

int cms_object_name_encode_stream(per_stream_t *s, const cms_object_name_t *v)
    { return per_encode_visible_string(s, (const char *)v->value, 64); }
int cms_object_name_decode_stream(per_stream_t *s, cms_object_name_t *v)
    { return per_decode_visible_string(s, (char *)v->value, 64); }

CMS_EXPORT int cms_object_name_encode(const cms_object_name_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_object_name_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_object_name_decode(cms_object_name_t *v, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_object_name_decode_stream(&r, v); }
