#ifndef CMS_SEND_GOOSE_MESSAGE_H
#define CMS_SEND_GOOSE_MESSAGE_H

#include "svc/cms_svc.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SendGOOSEMessage-PDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     goID            [0] IMPLICIT VisibleString129,
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     goRef           [2] IMPLICIT ObjectReference OPTIONAL,
 *     t               [3] IMPLICIT TimeStamp,
 *     stNum           [4] IMPLICIT INT32U,
 *     sqNum           [5] IMPLICIT INT32U,
 *     simulation      [6] IMPLICIT BOOLEAN,
 *     confRev         [7] IMPLICIT INT32U,
 *     ndsCom          [8] IMPLICIT BOOLEAN,
 *     data            [9] IMPLICIT SEQUENCE OF Data
 * }
 *
 * Unconfirmed service (0x84) — no Response or Error PDU.
 * ============================================================
 */

#define CMS_SEND_GOOSE_GO_ID_MAX_LEN 129

typedef struct {
    cms_req_id_t *req_id;
    cms_uint8_array_t *go_id;
    cms_boolean_t *dat_set_present;
    cms_object_reference_t *dat_set;
    cms_boolean_t *go_ref_present;
    cms_object_reference_t *go_ref;
    cms_time_stamp_t *t;
    cms_int32u_t *st_num;
    cms_int32u_t *sq_num;
    cms_boolean_t *simulation;
    cms_int32u_t *conf_rev;
    cms_boolean_t *nds_com;
    cms_array_t *data; /* SEQUENCE OF Data */
} cms_send_goose_message_t;

CMS_EXPORT int cms_send_goose_message_encode(const cms_send_goose_message_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_send_goose_message_decode(cms_send_goose_message_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
