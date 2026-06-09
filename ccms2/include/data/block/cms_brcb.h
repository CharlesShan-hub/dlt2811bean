#ifndef CMS_BLOCK_BRCB_H
#define CMS_BLOCK_BRCB_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int16.h"
#include "data/string/cms_uint8_array.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * BRCB ::= SEQUENCE {
 *     rptID         [1] IMPLICIT VisibleString129,
 *     rptEna        [2] IMPLICIT BOOLEAN,
 *     datSet        [3] IMPLICIT ObjectReference,
 *     confRev       [4] IMPLICIT INT32U,
 *     optFlds       [5] IMPLICIT RCBOptFlds,
 *     bufTm         [6] IMPLICIT INT32U,
 *     sqNum         [7] IMPLICIT INT16U,
 *     trgOps        [8] IMPLICIT TriggerConditions,
 *     intgPd        [9] IMPLICIT INT32U,
 *     gi            [10] IMPLICIT BOOLEAN,
 *     purgeBuf      [11] IMPLICIT BOOLEAN,
 *     entryID       [12] IMPLICIT EntryID,
 *     timeOfEntry   [13] IMPLICIT EntryTime,
 *     resvTms       [14] IMPLICIT INT16 OPTIONAL,
 *     owner         [15] IMPLICIT OCTET STRING (SIZE(0..64)) OPTIONAL
 * }  —  8.4
 */

#define CMS_BRCB_RPT_ID_MAX_LEN   129
#define CMS_BRCB_OWNER_MAX_LEN     64

typedef struct {
    cms_boolean_t            *rptEna;
    cms_uint8_array_t        *rptID;         /* VisibleString (SIZE(129)) */
    cms_object_reference_t   *datSet;        /* ObjectReference */
    cms_int32u_t             *confRev;       /* INT32U */
    cms_rcb_opt_flds_t       *optFlds;       /* RCBOptFlds */
    cms_int32u_t             *bufTm;         /* INT32U */
    cms_int16u_t             *sqNum;         /* INT16U */
    cms_trigger_conditions_t *trgOps;        /* TriggerConditions */
    cms_int32u_t             *intgPd;        /* INT32U */
    cms_boolean_t            *gi;            /* BOOLEAN */
    cms_boolean_t            *purgeBuf;      /* BOOLEAN */
    cms_entry_id_t           *entryID;       /* EntryID */
    cms_entry_time_t         *timeOfEntry;   /* EntryTime */
    cms_boolean_t            *resvTms_present;
    cms_int16_t              *resvTms;       /* INT16 OPTIONAL */
    cms_boolean_t            *owner_present;
    cms_uint8_array_t        *owner;         /* OCTET STRING (SIZE(0..64)) OPTIONAL */
} cms_brcb_t;

int cms_brcb_encode_stream(per_stream_t *s, const void *ptr);
int cms_brcb_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_brcb_encode(const void *ptr, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_brcb_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
