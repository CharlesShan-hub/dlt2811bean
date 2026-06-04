#include "data/common/cms_tcmd.h"
#include "per/cms_string.h"

int cms_tcmd_encode_stream(per_stream_t *s, const cms_tcmd_t *v)
{
    uint8_t buf[1] = { (uint8_t)v->value.value };
    return per_encode_bit_string_fixed(s, buf, 2);
}

int cms_tcmd_decode_stream(per_stream_t *s, cms_tcmd_t *v)
{
    uint8_t buf[1] = {0};
    int rc = per_decode_bit_string_fixed(s, buf, 2);
    if (rc) return rc;
    v->value.value = buf[0] & 0x03;
    return CMS_OK;
}

CMS_EXPORT int cms_tcmd_encode(const cms_tcmd_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_tcmd_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_tcmd_decode(cms_tcmd_t *v, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_tcmd_decode_stream(&r, v); }
