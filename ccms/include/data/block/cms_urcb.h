#ifndef DATA_BLOCK_CMS_URCB_H
#define DATA_BLOCK_CMS_URCB_H

#include "cms_core.h"
#include "per/cms_stream.h"
#include "data/basic/cms_boolean.h"
#include "data/basic/cms_integer.h"
#include "data/basic/cms_string.h"
#include "data/block/cms_opt_flds.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/common/cms_object_reference.h"

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
    cms_visible_string_fixed_t  rptID;
    cms_boolean_t               rptEna;
    cms_object_reference_t      datSet;
    cms_int32u_t                confRev;
    cms_rcb_opt_flds_t          optFlds;
    cms_int32u_t                bufTm;
    cms_int16u_t                sqNum;
    cms_trigger_conditions_t    trgOps;
    cms_int32u_t                intgPd;
    cms_boolean_t               gi;
    cms_boolean_t               resv;
    cms_octet_string_var_t      owner;
    int                         owner_present;
} cms_urcb_t;

CMS_EXPORT int cms_urcb_encode(const cms_urcb_t *value, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_urcb_decode(const uint8_t *in_buf, int in_len, cms_urcb_t *value);
int cms_urcb_encode_stream(per_stream_t *s, const cms_urcb_t *value);
int cms_urcb_decode_stream(per_stream_t *s, cms_urcb_t *value);

#ifdef __cplusplus
}
#endif

#endif
