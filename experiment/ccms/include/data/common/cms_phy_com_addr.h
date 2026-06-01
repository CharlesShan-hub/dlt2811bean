#ifndef DATA_COMMON_CMS_PHY_COM_ADDR_H
#define DATA_COMMON_CMS_PHY_COM_ADDR_H

#include "cms_core.h"
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

CMS_EXPORT int cms_phy_com_addr_encode(const uint8_t addr[6], uint8_t priority, uint16_t vid, uint16_t appid, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_phy_com_addr_decode(const uint8_t *in_buf, int in_len, uint8_t addr[6], uint8_t *priority, uint16_t *vid, uint16_t *appid);

#ifdef __cplusplus
}
#endif

#endif