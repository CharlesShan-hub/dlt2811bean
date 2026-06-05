#ifndef DATA_BLOCK_CMS_LCB_H
#define DATA_BLOCK_CMS_LCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/block/cms_lcb_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/common/cms_object_reference.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * LCB ::= SEQUENCE {
 *     logEna  [1] IMPLICIT BOOLEAN,
 *     datSet  [2] IMPLICIT ObjectReference,
 *     trgOps  [3] IMPLICIT TriggerConditions,
 *     intgPd  [4] IMPLICIT INT32U,
 *     logRef  [5] IMPLICIT ObjectReference,
 *     optFlds [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm   [7] IMPLICIT INT32U OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_boolean_t            logEna;
    cms_object_reference_t   datSet;
    cms_trigger_conditions_t trgOps;
    cms_int32u_t             intgPd;
    cms_object_reference_t   logRef;
    cms_lcb_opt_flds_t       optFlds;
    cms_boolean_t            optFlds_present;
    cms_int32u_t             bufTm;
    cms_boolean_t            bufTm_present;
} cms_lcb_t;

CMS_EXPORT int cms_lcb_encode(const cms_lcb_t *v, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_lcb_decode(cms_lcb_t *v, const uint8_t *in_buf, int in_len);
int cms_lcb_encode_stream(per_stream_t *s, const cms_lcb_t *v);
int cms_lcb_decode_stream(per_stream_t *s, cms_lcb_t *v);

#ifdef __cplusplus
}
#endif

#endif
