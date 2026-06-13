#ifndef CMS_OPERATE_H
#define CMS_OPERATE_H

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
 * Operate-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t              *req_id;
    cms_object_reference_t    *reference;
    cms_data_t                *ctl_val;
    cms_originator_t          *origin;
    cms_int8u_t               *ctl_num;
    cms_time_stamp_t          *t;
    cms_boolean_t             *test;
    cms_check_t               *check;
} cms_operate_request_t;

/*
 * Operate-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *reference;
} cms_operate_response_t;

/*
 * Operate-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference,
 *     ctlVal          [1] IMPLICIT Data,
 *     origin          [3] IMPLICIT Originator,
 *     ctlNum          [4] IMPLICIT INT8U,
 *     t               [5] IMPLICIT TimeStamp,
 *     test            [6] IMPLICIT BOOLEAN,
 *     check           [7] IMPLICIT Check,
 *     addCause        [8] IMPLICIT AddCause
 * }
 */
typedef struct {
    cms_req_id_t              *req_id;
    cms_object_reference_t    *reference;
    cms_data_t                *ctl_val;
    cms_originator_t          *origin;
    cms_int8u_t               *ctl_num;
    cms_time_stamp_t          *t;
    cms_boolean_t             *test;
    cms_check_t               *check;
    cms_add_cause_t           *add_cause;
} cms_operate_error_t;

CMS_EXPORT int cms_operate_request_encode(const cms_operate_request_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_operate_request_decode(cms_operate_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_operate_response_encode(const cms_operate_response_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_operate_response_decode(cms_operate_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_operate_error_encode(const cms_operate_error_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_operate_error_decode(cms_operate_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
