#ifndef DATA_BLOCK_CMS_LCB_H
#define DATA_BLOCK_CMS_LCB_H

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
#include "data/common/cms_object_name.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LCB (Log Control Block)
 *
 * ASN.1 definition:
 * LCB ::= SEQUENCE {
 *     logEna          [1] IMPLICIT BOOLEAN,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     trgOps          [3] IMPLICIT TriggerConditions,
 *     intgPd          [4] IMPLICIT INT32U,
 *     logRef          [5] IMPLICIT ObjectReference,
 *     optFlds         [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm           [7] IMPLICIT INT32U OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    int         logEna;                /* BOOLEAN */
    char        datSet[256];           /* ObjectReference */
    uint8_t     trgOps[1];             /* TriggerConditions (6 bits) */
    uint32_t    intgPd;                /* INT32U */
    char        logRef[256];           /* ObjectReference */
    uint8_t     optFlds[1];            /* LCBOptFlds (1 bit) OPTIONAL */
    int         optFlds_present;       /* 1 if present */
    uint32_t    bufTm;                 /* INT32U OPTIONAL */
    int         bufTm_present;         /* 1 if present */
} cms_lcb_t;

CMS_EXPORT int cms_lcb_encode(const cms_lcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_decode(const uint8_t *in_buf, int in_len, cms_lcb_t *value);
int cms_lcb_encode_stream(per_stream_t *s, const cms_lcb_t *value);
int cms_lcb_decode_stream(per_stream_t *s, cms_lcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
