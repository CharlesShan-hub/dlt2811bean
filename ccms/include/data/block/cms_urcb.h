#ifndef DATA_BLOCK_CMS_URCB_H
#define DATA_BLOCK_CMS_URCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "per/cms_integer.h"
#include "per/cms_string.h"
#include "per/cms_sequence.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_string.h"
#include "data/basic/cms_integer.h"
#include "data/block/cms_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * URCB (Unbuffered Report Control Block)
 *
 * ASN.1 definition:
 * URCB ::= SEQUENCE {
 *     rptID           [1] IMPLICIT VisibleString129,
 *     rptEna          [2] IMPLICIT BOOLEAN,
 *     datSet          [3] IMPLICIT ObjectReference,
 *     confRev         [4] IMPLICIT INT32U,
 *     optFlds         [5] IMPLICIT RCBOptFlds,
 *     bufTm           [6] IMPLICIT INT32U,
 *     sqNum           [7] IMPLICIT INT16U,
 *     trgOps          [8] IMPLICIT TriggerConditions,
 *     intgPd          [9] IMPLICIT INT32U,
 *     gi              [10] IMPLICIT BOOLEAN,
 *     resv            [14] IMPLICIT BOOLEAN,
 *     owner           [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_visible_string_fixed_t  rptID;               /* VisibleString129 */
    int                         rptEna;               /* BOOLEAN */
    char                        datSet[256];          /* ObjectReference */
    uint32_t                    confRev;              /* INT32U */
    uint8_t                     optFlds[2];           /* RCBOptFlds (10 bits) */
    uint32_t                    bufTm;                /* INT32U */
    uint16_t                    sqNum;                /* INT16U */
    uint8_t                     trgOps[1];            /* TriggerConditions (6 bits) */
    uint32_t                    intgPd;               /* INT32U */
    int                         gi;                   /* BOOLEAN */
    int                         resv;                 /* BOOLEAN */
    cms_octet_string_var_t      owner;                /* OCTET STRING (SIZE(0..64)) OPTIONAL */
    int                         owner_present;        /* 1 if present */
} cms_urcb_t;

CMS_EXPORT int cms_urcb_encode(const cms_urcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_urcb_decode(const uint8_t *in_buf, int in_len, cms_urcb_t *value);
int cms_urcb_encode_stream(per_stream_t *s, const cms_urcb_t *value);
int cms_urcb_decode_stream(per_stream_t *s, cms_urcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
