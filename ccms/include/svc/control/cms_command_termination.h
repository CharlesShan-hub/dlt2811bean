#ifndef CMS_COMMAND_TERMINATION_H
#define CMS_COMMAND_TERMINATION_H

#include "svc/cms_svc.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/control/cms_originator.h"
#include "data/control/cms_check.h"
#include "data/control/cms_add_cause.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int8u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * CommandTermination-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     operTm          [2] IMPLICIT TimeStamp OPTIONAL,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause OPTIONAL
 * }
 *
 * Unconfirmed service (0x26) — no Response or Error PDU.
 * ============================================================
 */
typedef struct {
    cms_req_id_t              *req_id;
    cms_object_reference_t    *reference;
    cms_data_t                *ctl_val;
    cms_boolean_t             *oper_tm_present;
    cms_time_stamp_t          *oper_tm;
    cms_originator_t          *origin;
    cms_int8u_t               *ctl_num;
    cms_time_stamp_t          *t;
    cms_boolean_t             *test;
    cms_check_t               *check;
    cms_boolean_t             *add_cause_present;
    cms_add_cause_t           *add_cause;
} cms_command_termination_t;

CMS_EXPORT int cms_command_termination_encode(const cms_command_termination_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_command_termination_decode(cms_command_termination_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
