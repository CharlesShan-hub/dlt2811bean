#include "data/common/cms_dbpos.h"
#include "per/cms_string.h"

int cms_dbpos_encode_stream(per_stream_t *s, const cms_dbpos_t *v)
{
    uint8_t buf[1] = { (uint8_t)v->value.value };
    return per_encode_bit_string_fixed(s, buf, 2);
}

int cms_dbpos_decode_stream(per_stream_t *s, cms_dbpos_t *v)
{
    uint8_t buf[1] = {0};
    int rc = per_decode_bit_string_fixed(s, buf, 2);
    if (rc) return rc;
    v->value.value = buf[0] & 0x03;
    return CMS_OK;
}

CMS_EXPORT int cms_dbpos_encode(const cms_dbpos_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_dbpos_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_dbpos_decode(cms_dbpos_t *v, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_dbpos_decode_stream(&r, v); }
