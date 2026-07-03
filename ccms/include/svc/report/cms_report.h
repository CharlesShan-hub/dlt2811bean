#ifndef CMS_REPORT_H
#define CMS_REPORT_H

#include "svc/cms_svc.h"
#include "svc/report/cms_report_entry.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ReportPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     rptID               [0] IMPLICIT VisibleString129,
 *     optFlds             [1] IMPLICIT RCBOptFlds,
 *     sqNum               [2] IMPLICIT INT16U OPTIONAL,
 *     subSeqNum           [3] IMPLICIT INT16U OPTIONAL,
 *     moreSegmentsFollow  [4] IMPLICIT BOOLEAN OPTIONAL,
 *     dataSet             [5] IMPLICIT ObjectReference OPTIONAL,
 *     bufOvfl             [6] IMPLICIT BOOLEAN OPTIONAL,
 *     confRev             [7] IMPLICIT INT32U OPTIONAL,
 *     entry               [8] IMPLICIT ReportEntry
 * }
 *
 * Unconfirmed service (0x35) — no Response or Error PDU.
 * ============================================================
 */

#define CMS_REPORT_RPT_ID_MAX_LEN 129

typedef struct {
    cms_req_id_t *req_id;
    cms_uint8_array_t *rpt_id; /* VisibleString129 */
    cms_rcb_opt_flds_t *opt_flds;
    cms_boolean_t *sq_num_present;
    cms_int16u_t *sq_num;
    cms_boolean_t *sub_seq_num_present;
    cms_int16u_t *sub_seq_num;
    cms_boolean_t *more_segments_follow_present;
    cms_boolean_t *more_segments_follow;
    cms_boolean_t *data_set_present;
    cms_object_reference_t *data_set;
    cms_boolean_t *buf_ovfl_present;
    cms_boolean_t *buf_ovfl;
    cms_boolean_t *conf_rev_present;
    cms_int32u_t *conf_rev;
    cms_report_entry_t *entry;
} cms_report_t;

CMS_EXPORT int cms_report_encode(const cms_report_t *pdu, uint8_t **out_buf, size_t *out_len);

CMS_EXPORT int cms_report_decode(cms_report_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
