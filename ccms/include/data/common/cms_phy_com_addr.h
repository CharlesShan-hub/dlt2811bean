#ifndef DATA_COMMON_CMS_PHY_COM_ADDR_H
#define DATA_COMMON_CMS_PHY_COM_ADDR_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_integer.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * PhyComAddr ::= SEQUENCE {
 *     addr     [0] IMPLICIT OCTET STRING (SIZE(6)),
 *     priority [1] IMPLICIT Int8U,
 *     vid      [2] IMPLICIT Int16U,
 *     appid    [3] IMPLICIT Int16U
 * }
 * ============================================================
 */
typedef struct {
    uint8_t      addr[6];
    uint8_t      priority;
    uint16_t     vid;
    uint16_t     appid;
} cms_phy_com_addr_t;

CMS_EXPORT int cms_phy_com_addr_encode(const cms_phy_com_addr_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_phy_com_addr_decode(cms_phy_com_addr_t *v, const uint8_t *in_buf, int in_len);
int cms_phy_com_addr_encode_stream(per_stream_t *s, const cms_phy_com_addr_t *v);
int cms_phy_com_addr_decode_stream(per_stream_t *s, cms_phy_com_addr_t *v);

#ifdef __cplusplus
}
#endif

#endif
