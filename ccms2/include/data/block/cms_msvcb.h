#ifndef CMS_BLOCK_MSVCB_H
#define CMS_BLOCK_MSVCB_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int16u.h"
#include "data/string/cms_uint8_array.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_phy_com_addr.h"
#include "data/block/cms_smp_mod.h"
#include "data/block/cms_msvcb_opt_flds.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * MSVCB ::= SEQUENCE {
 *     svEna           [1] IMPLICIT BOOLEAN,
 *     msvID           [2] IMPLICIT VisibleString129,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     smpMod          [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate         [6] IMPLICIT INT16U,
 *     optFlds         [7] IMPLICIT MSVCBOptFlds,
 *     dstAddress      [8] IMPLICIT PHYCOMADDR OPTIONAL
 * }  —  8.10.2
 */

#define CMS_MSVCB_MSV_ID_MAX_LEN 129

typedef struct {
    cms_boolean_t          *svEna;          /* BOOLEAN */
    cms_uint8_array_t      *msvID;          /* VisibleString129 */
    cms_object_reference_t *datSet;         /* ObjectReference */
    cms_int32u_t           *confRev;        /* INT32U */
    cms_boolean_t          *smpMod_present;
    cms_smp_mod_t          *smpMod;         /* SmpMod OPTIONAL */
    cms_int16u_t           *smpRate;        /* INT16U */
    cms_msvcb_opt_flds_t   *optFlds;        /* MSVCBOptFlds */
    cms_boolean_t          *dstAddress_present;
    cms_phy_com_addr_t     *dstAddress;     /* PHYCOMADDR OPTIONAL */
} cms_msvcb_t;

int cms_msvcb_encode_stream(per_stream_t *s, const void *ptr);
int cms_msvcb_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_msvcb_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
