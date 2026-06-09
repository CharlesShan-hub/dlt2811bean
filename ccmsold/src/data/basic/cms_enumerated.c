#include "data/basic/cms_enumerated.h"
#include "data/basic/cms_integer.h"

int cms_enumerated_encode_stream(per_stream_t *s, const cms_int32_t *v)
    { cms_int8_t _v = { (int8_t)v->value }; return cms_int8_encode_stream(s, &_v); }

int cms_enumerated_decode_stream(per_stream_t *s, cms_int32_t *v)
    { cms_int8_t _v; int rc = cms_int8_decode_stream(s, &_v); if (rc) return rc; v->value = _v.value; return CMS_OK; }
