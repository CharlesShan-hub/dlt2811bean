#ifndef DATA_BLOCK_CMS_MSVCB_H
#define DATA_BLOCK_CMS_MSVCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_sequence.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/block/cms_opt_flds.h"
#include "data/block/cms_smp_mod.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_phy_com_addr.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * MSVCB (Multicast Sampled Value Control Block)
 *
 * ASN.1 definition:
 * MSVCB ::= SEQUENCE {
 *     svEna           [1] IMPLICIT BOOLEAN,
 *     msvID           [2] IMPLICIT VisibleString129,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     smpMod          [5] IMPLICIT SmpMod OPTIONAL,
 *     smpRate         [6] IMPLICIT INT16U,
 *     optFlds         [7] IMPLICIT MSVCBOptFlds,
 *     dstAddress      [8] IMPLICIT PHYCOMADDR OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    int         svEna;                 /* BOOLEAN */
    char        msvID[130];            /* VisibleString129 */
    char        datSet[256];           /* ObjectReference */
    uint32_t    confRev;               /* INT32U */
    cms_smp_mod_t smpMod;              /* SmpMod OPTIONAL */
    int         smpMod_present;        /* 1 if present */
    uint16_t    smpRate;               /* INT16U */
    uint8_t     optFlds[1];            /* MSVCBOptFlds (5 bits) */
    /* dstAddress OPTIONAL */
    uint8_t     dstAddr[6];            /* PHYCOMADDR addr */
    uint8_t     dstPriority;           /* priority */
    uint16_t    dstVid;                /* VID */
    uint16_t    dstAppId;              /* APPID */
    int         dstAddress_present;    /* 1 if present */
} cms_msvcb_t;

CMS_EXPORT int cms_msvcb_encode(const cms_msvcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_msvcb_decode(const uint8_t *in_buf, int in_len, cms_msvcb_t *value);
int cms_msvcb_encode_stream(per_stream_t *s, const cms_msvcb_t *value);
int cms_msvcb_decode_stream(per_stream_t *s, cms_msvcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
