#include "data/common/cms_phy_com_addr.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include <string.h>
#include <stdlib.h>

CMS_EXPORT int cms_phy_com_addr_encode(const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid, uint8_t *out_buf, int *out_len)
{
    per_stream_t w;
    per_stream_init_write(&w, out_buf, (size_t)*out_len);
    per_encode_octet_string_fixed(&w, addr, 6);
    per_encode_constrained_int(&w, priority, 0, 255);
    per_encode_constrained_int(&w, vid, 0, 65535);
    per_encode_constrained_int(&w, appid, 0, 65535);
    *out_len = (int)per_stream_bytes_written(&w);
    return CMS_OK;
}

CMS_EXPORT int cms_phy_com_addr_decode(const uint8_t *in_buf, int in_len, uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid)
{
    per_stream_t r;
    per_stream_init_read(&r, in_buf, (size_t)in_len);
    per_decode_octet_string_fixed(&r, addr, 6);
    int64_t tmp;
    per_decode_constrained_int(&r, &tmp, 0, 255);
    *priority = (uint8_t)tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *vid = (uint16_t)tmp;
    per_decode_constrained_int(&r, &tmp, 0, 65535);
    *appid = (uint16_t)tmp;
    return CMS_OK;
}