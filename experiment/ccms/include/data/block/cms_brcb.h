#ifndef DATA_BLOCK_CMS_BRCB_H
#define DATA_BLOCK_CMS_BRCB_H

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
#include "data/common/cms_time_stamp.h"
#include "data/extended/cms_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * BRCB (Buffered Report Control Block)
 *
 * ASN.1 definition (IEC 61850-7-2):
 * BRCB ::= SEQUENCE {
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
 *     purgeBuf        [11] IMPLICIT BOOLEAN,
 *     entryID         [12] IMPLICIT EntryID,
 *     timeOfEntry     [13] IMPLICIT EntryTime,
 *     resvTms         [14] IMPLICIT INT16 OPTIONAL,
 *     owner           [15] IMPLICIT OCTET STRING (SIZE (0..64)) OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    char        rptID[130];            /* VisibleString129 */
    int         rptEna;                /* BOOLEAN */
    char        datSet[256];           /* ObjectReference */
    uint32_t    confRev;               /* INT32U */
    uint8_t     optFlds[2];            /* RCBOptFlds (10 bits) */
    uint32_t    bufTm;                 /* INT32U */
    uint16_t    sqNum;                 /* INT16U */
    uint8_t     trgOps[1];             /* TriggerConditions (6 bits) */
    uint32_t    intgPd;                /* INT32U */
    int         gi;                    /* BOOLEAN */
    int         purgeBuf;              /* BOOLEAN */
    uint8_t     entryID[8];            /* EntryID (OCTET STRING SIZE(8)) */
    uint32_t    timeOfEntry_ms;        /* EntryTime = BinaryTime: msOfDay */
    uint16_t    timeOfEntry_days;      /* EntryTime = BinaryTime: daysSince1984 */
    int16_t     resvTms;               /* INT16 OPTIONAL */
    int         resvTms_present;       /* 1 if present */
    uint8_t     owner[64];             /* OCTET STRING (SIZE(0..64)) OPTIONAL */
    int         owner_len;             /* actual length of owner */
    int         owner_present;         /* 1 if present */
} cms_brcb_t;

CMS_EXPORT int cms_brcb_encode(const cms_brcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_brcb_decode(const uint8_t *in_buf, int in_len, cms_brcb_t *value);
int cms_brcb_encode_stream(per_stream_t *s, const cms_brcb_t *value);
int cms_brcb_decode_stream(per_stream_t *s, cms_brcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
