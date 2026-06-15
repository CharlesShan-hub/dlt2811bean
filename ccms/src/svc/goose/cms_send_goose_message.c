#include "svc/goose/cms_send_goose_message.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

int cms_send_goose_message_encode(const cms_send_goose_message_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. goID — VisibleString(129) */
    if (!pdu->go_id) return CMS_ERR;
    err = cms_visible_string_encode_stream(&s, pdu->go_id, 129);
    if (err) return err;

    /* 3. datSet — ObjectReference OPTIONAL */
    {
        int present = (pdu->dat_set_present && pdu->dat_set_present->value) && pdu->dat_set;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(&s, &bit);
        if (err) return err;
        if (present) {
            err = cms_object_reference_encode_stream(&s, pdu->dat_set);
            if (err) return err;
        }
    }

    /* 4. goRef — ObjectReference OPTIONAL */
    {
        int present = (pdu->go_ref_present && pdu->go_ref_present->value) && pdu->go_ref;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(&s, &bit);
        if (err) return err;
        if (present) {
            err = cms_object_reference_encode_stream(&s, pdu->go_ref);
            if (err) return err;
        }
    }

    /* 5. t — TimeStamp */
    if (!pdu->t) return CMS_ERR;
    err = cms_time_stamp_encode_stream(&s, pdu->t);
    if (err) return err;

    /* 6. stNum — INT32U */
    if (!pdu->st_num) return CMS_ERR;
    err = cms_int32u_encode_stream(&s, pdu->st_num);
    if (err) return err;

    /* 7. sqNum — INT32U */
    if (!pdu->sq_num) return CMS_ERR;
    err = cms_int32u_encode_stream(&s, pdu->sq_num);
    if (err) return err;

    /* 8. simulation — BOOLEAN */
    if (!pdu->simulation) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->simulation);
    if (err) return err;

    /* 9. confRev — INT32U */
    if (!pdu->conf_rev) return CMS_ERR;
    err = cms_int32u_encode_stream(&s, pdu->conf_rev);
    if (err) return err;

    /* 10. ndsCom — BOOLEAN */
    if (!pdu->nds_com) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->nds_com);
    if (err) return err;

    /* 11. data — SEQUENCE OF Data */
    if (!pdu->data) return CMS_ERR;
    {
        uint32_t cnt = (uint32_t)pdu->data->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->data->elements[i];
            if (!e) return CMS_ERR;
            err = cms_data_encode_stream(&s, e);
            if (err) return err;
        }
    }

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_send_goose_message_decode(cms_send_goose_message_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. goID */
    if (!pdu->go_id) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->go_id, 129);
    if (err) return err;

    /* 3. datSet OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(&s, &bit);
        if (err) return err;
        if (pdu->dat_set_present) pdu->dat_set_present->value = bit.value;
        if (bit.value) {
            if (!pdu->dat_set) return CMS_ERR;
            err = cms_object_reference_decode_stream(&s, pdu->dat_set);
            if (err) return err;
        }
    }

    /* 4. goRef OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(&s, &bit);
        if (err) return err;
        if (pdu->go_ref_present) pdu->go_ref_present->value = bit.value;
        if (bit.value) {
            if (!pdu->go_ref) return CMS_ERR;
            err = cms_object_reference_decode_stream(&s, pdu->go_ref);
            if (err) return err;
        }
    }

    /* 5. t */
    if (!pdu->t) return CMS_ERR;
    err = cms_time_stamp_decode_stream(&s, pdu->t);
    if (err) return err;

    /* 6. stNum */
    if (!pdu->st_num) return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->st_num);
    if (err) return err;

    /* 7. sqNum */
    if (!pdu->sq_num) return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->sq_num);
    if (err) return err;

    /* 8. simulation */
    if (!pdu->simulation) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->simulation);
    if (err) return err;

    /* 9. confRev */
    if (!pdu->conf_rev) return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->conf_rev);
    if (err) return err;

    /* 10. ndsCom */
    if (!pdu->nds_com) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->nds_com);
    if (err) return err;

    /* 11. data */
    if (!pdu->data) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        pdu->data->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->data->elements[i];
            if (!e) return CMS_ERR;
            err = cms_data_decode_stream(&s, e);
            if (err) return err;
        }
    }

    return CMS_OK;
}
