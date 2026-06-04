#ifndef DATA_BLOCK_CMS_MSVCB_H
#define DATA_BLOCK_CMS_MSVCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"
#include "data/block/cms_opt_flds.h"
#include "data/block/cms_smp_mod.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_phy_com_addr.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    cms_boolean_t               svEna;
    cms_visible_string_fixed_t  msvID;
    cms_object_reference_t      datSet;
    cms_int32u_t                confRev;
    cms_smp_mod_t               smpMod;
    int                         smpMod_present;
    cms_int16u_t                smpRate;
    cms_msvcb_opt_flds_t        optFlds;
    cms_phy_com_addr_t          dstAddress;
    int                         dstAddress_present;
} cms_msvcb_t;

CMS_EXPORT int cms_msvcb_encode(const cms_msvcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_decode(const uint8_t *in_buf, int in_len, cms_msvcb_t *value);
int cms_msvcb_encode_stream(per_stream_t *s, const cms_msvcb_t *value);
int cms_msvcb_decode_stream(per_stream_t *s, cms_msvcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
