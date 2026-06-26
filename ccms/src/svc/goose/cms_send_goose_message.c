#include "svc/goose/cms_send_goose_message.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

int cms_send_goose_message_encode(const cms_send_goose_message_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 1. OPTIONAL bitmap (2 fields: datSet, goRef) */
    bool opt_present[2] = {
        (pdu->dat_set_present && pdu->dat_set_present->value) && pdu->dat_set,
        (pdu->go_ref_present && pdu->go_ref_present->value) && pdu->go_ref
    };
    err = (int)per_encode_optional_bitmap(&s, opt_present, 2);
    if (err) { per_stream_free(&s); return err; }

    /* 2. goID — VisibleString(129) */
    if (!pdu->go_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_visible_string_encode_stream(&s, pdu->go_id, 129);
    if (err) { per_stream_free(&s); return err; }

    /* 3. datSet — ObjectReference OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_object_reference_encode_stream(&s, pdu->dat_set);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 4. goRef — ObjectReference OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_object_reference_encode_stream(&s, pdu->go_ref);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 5. t — TimeStamp */
    if (!pdu->t) { per_stream_free(&s); return CMS_ERR; }
    err = cms_time_stamp_encode_stream(&s, pdu->t);
    if (err) { per_stream_free(&s); return err; }

    /* 6. stNum — INT32U */
    if (!pdu->st_num) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int32u_encode_stream(&s, pdu->st_num);
    if (err) { per_stream_free(&s); return err; }

    /* 7. sqNum — INT32U */
    if (!pdu->sq_num) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int32u_encode_stream(&s, pdu->sq_num);
    if (err) { per_stream_free(&s); return err; }

    /* 8. simulation — BOOLEAN */
    if (!pdu->simulation) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->simulation);
    if (err) { per_stream_free(&s); return err; }

    /* 9. confRev — INT32U */
    if (!pdu->conf_rev) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int32u_encode_stream(&s, pdu->conf_rev);
    if (err) { per_stream_free(&s); return err; }

    /* 10. ndsCom — BOOLEAN */
    if (!pdu->nds_com) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->nds_com);
    if (err) { per_stream_free(&s); return err; }

    /* 11. data — SEQUENCE OF Data */
    if (!pdu->data) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->data->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->data->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_data_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_send_goose_message_decode(cms_send_goose_message_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (2 fields) */
    bool opt_present[2];
    err = (int)per_decode_optional_bitmap(&s, opt_present, 2);
    if (err) return err;
    if (pdu->dat_set_present) pdu->dat_set_present->value = opt_present[0];
    if (pdu->go_ref_present) pdu->go_ref_present->value = opt_present[1];

    /* 2. goID */
    if (!pdu->go_id) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->go_id, 129);
    if (err) return err;

    /* 3. datSet OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->dat_set) return CMS_ERR;
        err = cms_object_reference_decode_stream(&s, pdu->dat_set);
        if (err) return err;
    }

    /* 4. goRef OPTIONAL */
    if (opt_present[1]) {
        if (!pdu->go_ref) return CMS_ERR;
        err = cms_object_reference_decode_stream(&s, pdu->go_ref);
        if (err) return err;
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
