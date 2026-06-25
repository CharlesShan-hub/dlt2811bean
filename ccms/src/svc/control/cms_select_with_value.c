#include "svc/control/cms_select_with_value.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/control/cms_originator.h"
#include "data/control/cms_check.h"
#include "data/control/cms_add_cause.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int8u.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_select_with_value_request_encode(const cms_select_with_value_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: operTm) */
    bool opt[1] = {
        (pdu->oper_tm_present && pdu->oper_tm_present->value) && pdu->oper_tm
    };
    err = (int)per_encode_optional_bitmap(&s, opt, 1);
    if (err) return err;

    /* 2. reference — ObjectReference */
    if (!pdu->reference) return CMS_ERR;
    err = cms_object_reference_encode_stream(&s, pdu->reference);
    if (err) return err;

    /* 3. ctlVal — Data */
    if (!pdu->ctl_val) return CMS_ERR;
    err = cms_data_encode_stream(&s, pdu->ctl_val);
    if (err) return err;

    /* 4. operTm — TimeStamp OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_time_stamp_encode_stream(&s, pdu->oper_tm);
        if (err) return err;
    }

    /* 5. origin — Originator */
    if (!pdu->origin) return CMS_ERR;
    err = cms_originator_encode_stream(&s, pdu->origin);
    if (err) return err;

    /* 6. ctlNum — INT8U */
    if (!pdu->ctl_num) return CMS_ERR;
    err = cms_int8u_encode_stream(&s, pdu->ctl_num);
    if (err) return err;

    /* 7. t — TimeStamp */
    if (!pdu->t) return CMS_ERR;
    err = cms_time_stamp_encode_stream(&s, pdu->t);
    if (err) return err;

    /* 8. test — BOOLEAN */
    if (!pdu->test) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->test);
    if (err) return err;

    /* 9. check — Check */
    if (!pdu->check) return CMS_ERR;
    err = cms_check_encode_stream(&s, pdu->check);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_select_with_value_request_decode(cms_select_with_value_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: operTm) */
    bool opt[1] = {false};
    err = (int)per_decode_optional_bitmap(&s, opt, 1);
    if (err) return err;
    if (pdu->oper_tm_present)
        pdu->oper_tm_present->value = opt[0] ? 1 : 0;

    /* 2. reference */
    if (!pdu->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->reference);
    if (err) return err;

    /* 3. ctlVal */
    if (!pdu->ctl_val) return CMS_ERR;
    err = cms_data_decode_stream(&s, pdu->ctl_val);
    if (err) return err;

    /* 4. operTm OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (!pdu->oper_tm) return CMS_ERR;
        err = cms_time_stamp_decode_stream(&s, pdu->oper_tm);
        if (err) return err;
    }

    /* 5. origin */
    if (!pdu->origin) return CMS_ERR;
    err = cms_originator_decode_stream(&s, pdu->origin);
    if (err) return err;

    /* 6. ctlNum */
    if (!pdu->ctl_num) return CMS_ERR;
    err = cms_int8u_decode_stream(&s, pdu->ctl_num);
    if (err) return err;

    /* 7. t */
    if (!pdu->t) return CMS_ERR;
    err = cms_time_stamp_decode_stream(&s, pdu->t);
    if (err) return err;

    /* 8. test */
    if (!pdu->test) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->test);
    if (err) return err;

    /* 9. check */
    if (!pdu->check) return CMS_ERR;
    err = cms_check_decode_stream(&s, pdu->check);
    if (err) return err;

    return CMS_OK;
}

/* ── Response (same as Request) ── */

int cms_select_with_value_response_encode(const cms_select_with_value_response_t *pdu, uint8_t *out_buf, int *out_len) {
    return cms_select_with_value_request_encode((const cms_select_with_value_request_t*)pdu, out_buf, out_len);
}

int cms_select_with_value_response_decode(cms_select_with_value_response_t *pdu, const uint8_t *in_buf, int in_len) {
    return cms_select_with_value_request_decode((cms_select_with_value_request_t*)pdu, in_buf, in_len);
}

/* ── Error ── */

int cms_select_with_value_error_encode(const cms_select_with_value_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: operTm) */
    bool opt[1] = {
        (pdu->oper_tm_present && pdu->oper_tm_present->value) && pdu->oper_tm
    };
    err = (int)per_encode_optional_bitmap(&s, opt, 1);
    if (err) return err;

    /* 2. reference — ObjectReference */
    if (!pdu->reference) return CMS_ERR;
    err = cms_object_reference_encode_stream(&s, pdu->reference);
    if (err) return err;

    /* 3. ctlVal — Data */
    if (!pdu->ctl_val) return CMS_ERR;
    err = cms_data_encode_stream(&s, pdu->ctl_val);
    if (err) return err;

    /* 4. operTm — TimeStamp OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_time_stamp_encode_stream(&s, pdu->oper_tm);
        if (err) return err;
    }

    /* 5. origin — Originator */
    if (!pdu->origin) return CMS_ERR;
    err = cms_originator_encode_stream(&s, pdu->origin);
    if (err) return err;

    /* 6. ctlNum — INT8U */
    if (!pdu->ctl_num) return CMS_ERR;
    err = cms_int8u_encode_stream(&s, pdu->ctl_num);
    if (err) return err;

    /* 7. t — TimeStamp */
    if (!pdu->t) return CMS_ERR;
    err = cms_time_stamp_encode_stream(&s, pdu->t);
    if (err) return err;

    /* 8. test — BOOLEAN */
    if (!pdu->test) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->test);
    if (err) return err;

    /* 9. check — Check */
    if (!pdu->check) return CMS_ERR;
    err = cms_check_encode_stream(&s, pdu->check);
    if (err) return err;

    /* 10. addCause — AddCause */
    if (!pdu->add_cause) return CMS_ERR;
    err = cms_add_cause_encode_stream(&s, pdu->add_cause);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_select_with_value_error_decode(cms_select_with_value_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: operTm) */
    bool opt[1] = {false};
    err = (int)per_decode_optional_bitmap(&s, opt, 1);
    if (err) return err;
    if (pdu->oper_tm_present)
        pdu->oper_tm_present->value = opt[0] ? 1 : 0;

    /* 2. reference */
    if (!pdu->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->reference);
    if (err) return err;

    /* 3. ctlVal */
    if (!pdu->ctl_val) return CMS_ERR;
    err = cms_data_decode_stream(&s, pdu->ctl_val);
    if (err) return err;

    /* 4. operTm OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (!pdu->oper_tm) return CMS_ERR;
        err = cms_time_stamp_decode_stream(&s, pdu->oper_tm);
        if (err) return err;
    }

    /* 5. origin */
    if (!pdu->origin) return CMS_ERR;
    err = cms_originator_decode_stream(&s, pdu->origin);
    if (err) return err;

    /* 6. ctlNum */
    if (!pdu->ctl_num) return CMS_ERR;
    err = cms_int8u_decode_stream(&s, pdu->ctl_num);
    if (err) return err;

    /* 7. t */
    if (!pdu->t) return CMS_ERR;
    err = cms_time_stamp_decode_stream(&s, pdu->t);
    if (err) return err;

    /* 8. test */
    if (!pdu->test) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->test);
    if (err) return err;

    /* 9. check */
    if (!pdu->check) return CMS_ERR;
    err = cms_check_decode_stream(&s, pdu->check);
    if (err) return err;

    /* 10. addCause */
    if (!pdu->add_cause) return CMS_ERR;
    err = cms_add_cause_decode_stream(&s, pdu->add_cause);
    if (err) return err;

    return CMS_OK;
}
