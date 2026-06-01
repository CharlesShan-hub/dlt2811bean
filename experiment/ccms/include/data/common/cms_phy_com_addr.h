#ifndef DATA_COMMON_CMS_PHY_COM_ADDR_H
#define DATA_COMMON_CMS_PHY_COM_ADDR_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * ============================================================
 * PhyComAddr
 * ============================================================
 */
CMS_EXPORT int cms_phy_com_addr_encode(const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_phy_com_addr_decode(const uint8_t *in_buf, int in_len, uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid);
int cms_phy_com_addr_encode_stream(per_stream_t *s, const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid);
int cms_phy_com_addr_decode_stream(per_stream_t *s, uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid);

#ifdef __cplusplus
}
#endif

#endif
