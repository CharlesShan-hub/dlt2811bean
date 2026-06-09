#ifndef CMS_COMMON_PHY_COM_ADDR_H
#define CMS_COMMON_PHY_COM_ADDR_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/string/cms_octet_string.h"
#include "data/scalar/cms_int8u.h"
#include "data/scalar/cms_int16u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * PhyComAddr ::= SEQUENCE {
 *     addr        [0] IMPLICIT OCTET STRING (SIZE(6)),
 *     priority    [1] IMPLICIT Int8U,
 *     vid         [2] IMPLICIT Int16U,
 *     appid       [3] IMPLICIT Int16U
 * }  —  7.3.12
 *
 * All-pointer layout (sizeof = 4 * 8 = 32):
 *   [0]  addr      → uint8_t[6] (cms_uint8_array_t)
 *   [8]  priority  → cms_int8u_t*
 *   [16] vid       → cms_int16u_t*
 *   [24] appid     → cms_int16u_t*
 */
typedef struct {
    cms_uint8_array_t *addr;        /* OCTET STRING (SIZE(6)) */
    cms_int8u_t       *priority;    /* Int8U */
    cms_int16u_t      *vid;         /* Int16U */
    cms_int16u_t      *appid;       /* Int16U */
} cms_phy_com_addr_t;

int cms_phy_com_addr_encode_stream(per_stream_t *s, const void *ptr);
int cms_phy_com_addr_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_phy_com_addr_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_phy_com_addr_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
