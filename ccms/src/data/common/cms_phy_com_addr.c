#include "data/common/cms_phy_com_addr.h"
#include "per/cms_string.h"
#include "per/cms_integer.h"

int cms_phy_com_addr_encode_stream(per_stream_t *s, const cms_phy_com_addr_t *v)
{
    per_encode_octet_string_fixed(s, v->addr, 6);
    per_encode_constrained_int(s, (int64_t)v->priority, 0, 255);
    per_encode_constrained_int(s, (int64_t)v->vid, 0, 65535);
    per_encode_constrained_int(s, (int64_t)v->appid, 0, 65535);
    return CMS_OK;
}

int cms_phy_com_addr_decode_stream(per_stream_t *s, cms_phy_com_addr_t *v)
{
    per_decode_octet_string_fixed(s, v->addr, 6);
    int64_t tmp;
    per_decode_constrained_int(s, &tmp, 0, 255); v->priority = (uint8_t)tmp;
    per_decode_constrained_int(s, &tmp, 0, 65535); v->vid = (uint16_t)tmp;
    per_decode_constrained_int(s, &tmp, 0, 65535); v->appid = (uint16_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_phy_com_addr_encode(const cms_phy_com_addr_t *v, uint8_t *b, int *l)
    { per_stream_t w = per_stream_new_write(b, (size_t)*l); int rc = cms_phy_com_addr_encode_stream(&w, v); *l = (int)per_stream_bytes_written(&w); return rc; }
CMS_EXPORT int cms_phy_com_addr_decode(cms_phy_com_addr_t *v, const uint8_t *b, int l)
    { per_stream_t r = per_stream_new_read(b, (size_t)l); return cms_phy_com_addr_decode_stream(&r, v); }
