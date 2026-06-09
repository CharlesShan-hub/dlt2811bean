#include "data/common/cms_phy_com_addr.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"

int cms_phy_com_addr_encode_stream(per_stream_t *s, const cms_phy_com_addr_t *v){
    cms_octet_string_fixed_t _addr = { v->addr.value, CMS_PHY_COM_ADDR_ADDR_SIZE };
    int rc = cms_octet_string_fixed_encode_stream(s, &_addr);
    if (rc) return rc;
    rc = cms_int8u_encode_stream(s, &v->priority);
    if (rc) return rc;
    rc = cms_int16u_encode_stream(s, &v->vid);
    if (rc) return rc;
    return cms_int16u_encode_stream(s, &v->appid);
}

int cms_phy_com_addr_decode_stream(per_stream_t *s, cms_phy_com_addr_t *v){
    cms_octet_string_fixed_t _addr = { v->addr.value, CMS_PHY_COM_ADDR_ADDR_SIZE };
    int rc = cms_octet_string_fixed_decode_stream(s, &_addr);
    if (rc) return rc;
    v->addr.len = CMS_PHY_COM_ADDR_ADDR_SIZE;
    rc = cms_int8u_decode_stream(s, &v->priority);
    if (rc) return rc;
    rc = cms_int16u_decode_stream(s, &v->vid);
    if (rc) return rc;
    return cms_int16u_decode_stream(s, &v->appid);
}

CMS_EXPORT int cms_phy_com_addr_encode(const cms_phy_com_addr_t *v, uint8_t *b, int *l){
    per_stream_t w = per_stream_new_write(b, (size_t)*l); 
    int rc = cms_phy_com_addr_encode_stream(&w, v); 
    *l = (int)per_stream_bytes_written(&w); 
    return rc; 
}

CMS_EXPORT int cms_phy_com_addr_decode(cms_phy_com_addr_t *v, const uint8_t *b, int l){
    per_stream_t r = per_stream_new_read(b, (size_t)l); 
    return cms_phy_com_addr_decode_stream(&r, v); 
}
