#include "data/common/cms_phy_com_addr.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

int cms_phy_com_addr_encode_stream(per_stream_t *s, const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid)
{
    per_encode_octet_string_fixed(s, addr, 6);
    per_encode_constrained_int(s, priority, 0, 255);
    per_encode_constrained_int(s, vid, 0, 65535);
    per_encode_constrained_int(s, appid, 0, 65535);
    return CMS_OK;
}
int cms_phy_com_addr_decode_stream(per_stream_t *s, uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid)
{
    per_decode_octet_string_fixed(s, addr, 6);
    int64_t tmp;
    per_decode_constrained_int(s, &tmp, 0, 255); *priority = (uint8_t)tmp;
    per_decode_constrained_int(s, &tmp, 0, 65535); *vid = (uint16_t)tmp;
    per_decode_constrained_int(s, &tmp, 0, 65535); *appid = (uint16_t)tmp;
    return CMS_OK;
}

CMS_EXPORT int cms_phy_com_addr_encode(const uint8_t a[6], uint8_t p, uint16_t v, uint16_t aid, uint8_t *b, int *l)
    { per_stream_t w; per_stream_init_write(&w, b, (size_t)*l); cms_phy_com_addr_encode_stream(&w, a, p, v, aid); *l = (int)per_stream_bytes_written(&w); return CMS_OK; }
CMS_EXPORT int cms_phy_com_addr_decode(const uint8_t *b, int l, uint8_t a[6], uint8_t *p, uint16_t *v, uint16_t *aid)
    { per_stream_t r; per_stream_init_read(&r, b, (size_t)l); cms_phy_com_addr_decode_stream(&r, a, p, v, aid); return CMS_OK; }
