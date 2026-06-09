#ifndef DATA_BLOCK_CMS_BRCB_H
#define DATA_BLOCK_CMS_BRCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/common/cms_time_stamp.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_object_reference.h"
#include "data/extended/cms_time.h"
#include "data/basic/cms_string.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * BRCB ::= SEQUENCE {
 *     rptID       [1] IMPLICIT VisibleString129,
 *     rptEna      [2] IMPLICIT BOOLEAN,
 *     datSet      [3] IMPLICIT ObjectReference,
 *     confRev     [4] IMPLICIT INT32U,
 *     optFlds     [5] IMPLICIT RCBOptFlds,
 *     bufTm       [6] IMPLICIT INT32U,
 *     sqNum       [7] IMPLICIT INT16U,
 *     trgOps      [8] IMPLICIT TriggerConditions,
 *     intgPd      [9] IMPLICIT INT32U,
 *     gi          [10] IMPLICIT BOOLEAN,
 *     purgeBuf    [11] IMPLICIT BOOLEAN,
 *     entryID     [12] IMPLICIT EntryID,
 *     timeOfEntry [13] IMPLICIT EntryTime,
 *     resvTms     [14] IMPLICIT INT16 OPTIONAL,
 *     owner       [15] IMPLICIT OCTET STRING (SIZE(0..64)) OPTIONAL
 * }
 * ============================================================
 */
#define CMS_RPT_ID_MAX_LEN 129
#define CMS_OWNER_MAX_LEN 64

typedef struct {
    cms_uint8_array_t          rptID;
    cms_boolean_t              rptEna;
    cms_object_reference_t     datSet;
    cms_int32u_t               confRev;
    cms_rcb_opt_flds_t         optFlds;
    cms_int32u_t               bufTm;
    cms_int16u_t               sqNum;
    cms_trigger_conditions_t   trgOps;
    cms_int32u_t               intgPd;
    cms_boolean_t              gi;
    cms_boolean_t              purgeBuf;
    cms_entry_id_t             entryID;
    cms_binary_time_t          timeOfEntry;
    cms_int16_t                resvTms;
    cms_boolean_t              resvTms_is_present;
    cms_uint8_array_t          owner;
    cms_boolean_t              owner_is_present;
} cms_brcb_t;

CMS_EXPORT int cms_brcb_encode(const cms_brcb_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_brcb_decode(cms_brcb_t *v, const uint8_t *in_buf, int in_len);
int cms_brcb_encode_stream(per_stream_t *s, const cms_brcb_t *v);
int cms_brcb_decode_stream(per_stream_t *s, cms_brcb_t *v);

#ifdef __cplusplus
}
#endif

#endif
