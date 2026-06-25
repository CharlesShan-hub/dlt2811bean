#ifndef CMS_BLOCK_LCB_H
#define CMS_BLOCK_LCB_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_lcb_opt_flds.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * LCB ::= SEQUENCE {
 *     logEna          [1] IMPLICIT BOOLEAN,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     trgOps          [3] IMPLICIT TriggerConditions,
 *     intgPd          [4] IMPLICIT INT32U,
 *     logRef          [5] IMPLICIT ObjectReference,
 *     optFlds         [6] IMPLICIT LCBOptFlds OPTIONAL,
 *     bufTm           [7] IMPLICIT INT32U OPTIONAL
 * }  —  8.8.2
 */

typedef struct {
    cms_boolean_t            *logEna;         /* BOOLEAN */
    cms_object_reference_t   *datSet;         /* ObjectReference */
    cms_trigger_conditions_t *trgOps;         /* TriggerConditions */
    cms_int32u_t             *intgPd;         /* INT32U */
    cms_object_reference_t   *logRef;         /* ObjectReference */
    cms_boolean_t            *optFlds_present;
    cms_lcb_opt_flds_t       *optFlds;        /* LCBOptFlds OPTIONAL */
    cms_boolean_t            *bufTm_present;
    cms_int32u_t             *bufTm;          /* INT32U OPTIONAL */
} cms_lcb_t;

int cms_lcb_encode_stream(per_stream_t *s, const void *ptr);
int cms_lcb_decode_stream(per_stream_t *s, void *ptr);

CMS_EXPORT int cms_lcb_encode(const void *ptr, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_lcb_decode(void *ptr, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
