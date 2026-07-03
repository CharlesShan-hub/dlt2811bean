#include "svc/report/cms_report.h"
#include "svc/other/cms_req_id.h"
#include "svc/report/cms_report_entry.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

#define RPT_ID_MAX 129

int cms_report_encode(const cms_report_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init)
        return (int) err_init;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 1. rptID — VisibleString(129) */
    if (!pdu->rpt_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_visible_string_encode_stream(&s, pdu->rpt_id, RPT_ID_MAX);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 2. optFlds — RCBOptFlds */
    if (!pdu->opt_flds) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_rcb_opt_flds_encode_stream(&s, pdu->opt_flds);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 3. OPTIONAL bitmap (6 fields: sqNum, subSeqNum, moreSegmentsFollow, dataSet, bufOvfl, confRev) */
    bool opt_present[6] = {(pdu->sq_num_present && pdu->sq_num_present->value) && pdu->sq_num,
                           (pdu->sub_seq_num_present && pdu->sub_seq_num_present->value) && pdu->sub_seq_num,
                           (pdu->more_segments_follow_present && pdu->more_segments_follow_present->value),
                           (pdu->data_set_present && pdu->data_set_present->value) && pdu->data_set,
                           (pdu->buf_ovfl_present && pdu->buf_ovfl_present->value),
                           (pdu->conf_rev_present && pdu->conf_rev_present->value) && pdu->conf_rev};
    err = (int) per_encode_optional_bitmap(&s, opt_present, 6);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 4. sqNum — INT16U OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_int16u_encode_stream(&s, pdu->sq_num);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 5. subSeqNum — INT16U OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_int16u_encode_stream(&s, pdu->sub_seq_num);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 6. moreSegmentsFollow — BOOLEAN OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_boolean_encode_stream(&s, pdu->more_segments_follow);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 7. dataSet — ObjectReference OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_object_reference_encode_stream(&s, pdu->data_set);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 8. bufOvfl — BOOLEAN OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_boolean_encode_stream(&s, pdu->buf_ovfl);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 9. confRev — INT32U OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_int32u_encode_stream(&s, pdu->conf_rev);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 10. entry — ReportEntry */
    if (!pdu->entry) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_report_entry_encode_stream(&s, pdu->entry);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_report_decode(cms_report_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 1. rptID */
    if (!pdu->rpt_id)
        return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->rpt_id, RPT_ID_MAX);
    if (err)
        return err;

    /* 2. optFlds */
    if (!pdu->opt_flds)
        return CMS_ERR;
    err = cms_rcb_opt_flds_decode_stream(&s, pdu->opt_flds);
    if (err)
        return err;

    /* 3. OPTIONAL bitmap (6 fields) */
    bool opt_present[6];
    err = (int) per_decode_optional_bitmap(&s, opt_present, 6);
    if (err)
        return err;
    if (pdu->sq_num_present)
        pdu->sq_num_present->value = opt_present[0];
    if (pdu->sub_seq_num_present)
        pdu->sub_seq_num_present->value = opt_present[1];
    if (pdu->more_segments_follow_present)
        pdu->more_segments_follow_present->value = opt_present[2];
    if (pdu->data_set_present)
        pdu->data_set_present->value = opt_present[3];
    if (pdu->buf_ovfl_present)
        pdu->buf_ovfl_present->value = opt_present[4];
    if (pdu->conf_rev_present)
        pdu->conf_rev_present->value = opt_present[5];

    /* 4. sqNum OPTIONAL */
    if (opt_present[0] && pdu->sq_num) {
        err = cms_int16u_decode_stream(&s, pdu->sq_num);
        if (err)
            return err;
    }

    /* 5. subSeqNum OPTIONAL */
    if (opt_present[1] && pdu->sub_seq_num) {
        err = cms_int16u_decode_stream(&s, pdu->sub_seq_num);
        if (err)
            return err;
    }

    /* 6. moreSegmentsFollow OPTIONAL */
    if (opt_present[2] && pdu->more_segments_follow) {
        err = cms_boolean_decode_stream(&s, pdu->more_segments_follow);
        if (err)
            return err;
    }

    /* 7. dataSet OPTIONAL */
    if (opt_present[3] && pdu->data_set) {
        err = cms_object_reference_decode_stream(&s, pdu->data_set);
        if (err)
            return err;
    }

    /* 8. bufOvfl OPTIONAL */
    if (opt_present[4] && pdu->buf_ovfl) {
        err = cms_boolean_decode_stream(&s, pdu->buf_ovfl);
        if (err)
            return err;
    }

    /* 9. confRev OPTIONAL */
    if (opt_present[5] && pdu->conf_rev) {
        err = cms_int32u_decode_stream(&s, pdu->conf_rev);
        if (err)
            return err;
    }

    /* 10. entry */
    if (!pdu->entry)
        return CMS_ERR;
    err = cms_report_entry_decode_stream(&s, pdu->entry);
    if (err)
        return err;

    return CMS_OK;
}
