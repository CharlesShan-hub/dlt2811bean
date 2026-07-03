#ifndef CMS_SEND_MSV_MESSAGE_H
#define CMS_SEND_MSV_MESSAGE_H

#include "svc/cms_svc.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/block/cms_smp_mod.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int8u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SendMSVMessage-PDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     msvID           [0] IMPLICIT VisibleString129,
 *     datSet          [1] IMPLICIT ObjectReference OPTIONAL,
 *     smpCnt          [2] IMPLICIT INT16U,
 *     confRev         [3] IMPLICIT INT32U,
 *     refTm           [4] IMPLICIT TimeStamp OPTIONAL,
 *     smpSynch        [5] IMPLICIT INT8U,
 *     smpRate         [6] IMPLICIT INT16U OPTIONAL,
 *     simulation      [7] IMPLICIT BOOLEAN,
 *     sample          [8] IMPLICIT SEQUENCE OF Data,
 *     smpMod          [9] IMPLICIT SmpMod OPTIONAL
 * }
 *
 * Unconfirmed service (0x88) — no Response or Error PDU.
 * ============================================================
 */

#define CMS_SEND_MSV_MSV_ID_MAX_LEN 129

typedef struct {
    cms_req_id_t *req_id;
    cms_uint8_array_t *msv_id;
    cms_boolean_t *dat_set_present;
    cms_object_reference_t *dat_set;
    cms_int16u_t *smp_cnt;
    cms_int32u_t *conf_rev;
    cms_boolean_t *ref_tm_present;
    cms_time_stamp_t *ref_tm;
    cms_int8u_t *smp_synch;
    cms_boolean_t *smp_rate_present;
    cms_int16u_t *smp_rate;
    cms_boolean_t *simulation;
    cms_array_t *sample; /* SEQUENCE OF Data */
    cms_boolean_t *smp_mod_present;
    cms_smp_mod_t *smp_mod;
} cms_send_msv_message_t;

CMS_EXPORT int cms_send_msv_message_encode(const cms_send_msv_message_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_send_msv_message_decode(cms_send_msv_message_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
