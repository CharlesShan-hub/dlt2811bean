#include "svc/goose/cms_get_goose_element_number.h"
#include "svc/other/cms_req_id.h"
#include "svc/goose/cms_go_ref_fc_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_get_goose_element_number_request_encode(const cms_get_goose_element_number_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. gocbReference — ObjectReference */
    if (!pdu->gocb_reference) { per_stream_free(&s); return CMS_ERR; }
    err = cms_object_reference_encode_stream(&s, pdu->gocb_reference);
    if (err) { per_stream_free(&s); return err; }

    /* 3. memberData — SEQUENCE OF GoRefFcEntry */
    if (!pdu->member_data) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->member_data->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_go_ref_fc_entry_t *e = (cms_go_ref_fc_entry_t*)pdu->member_data->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_go_ref_fc_entry_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_goose_element_number_request_decode(cms_get_goose_element_number_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;
    int retry_needed = 0;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. gocbReference */
    if (!pdu->gocb_reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->gocb_reference);
    if (err) return err;

    /* 3. memberData */
    if (!pdu->member_data) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->member_data->count < (int32_t)cnt) retry_needed = 1;
        pdu->member_data->count = (int32_t)cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_go_ref_fc_entry_decode_stream(&s, retry_needed ? NULL : pdu->member_data->elements[i]);
            if (err == CMS_RETRY) inner_retry_needed = 1;
            else if (err) return err;
        }
        if (inner_retry_needed) retry_needed = 1;
    }

    return retry_needed ? CMS_RETRY : CMS_OK;
}

/* ── Response ── */

int cms_get_goose_element_number_response_encode(const cms_get_goose_element_number_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. gocbReference — ObjectReference */
    if (!pdu->gocb_reference) { per_stream_free(&s); return CMS_ERR; }
    err = cms_object_reference_encode_stream(&s, pdu->gocb_reference);
    if (err) { per_stream_free(&s); return err; }

    /* 3. confRev — INT32U */
    if (!pdu->conf_rev) { per_stream_free(&s); return CMS_ERR; }
    err = cms_int32u_encode_stream(&s, pdu->conf_rev);
    if (err) { per_stream_free(&s); return err; }

    /* 4. datSet — ObjectReference */
    if (!pdu->dat_set) { per_stream_free(&s); return CMS_ERR; }
    err = cms_object_reference_encode_stream(&s, pdu->dat_set);
    if (err) { per_stream_free(&s); return err; }

    /* 5. memberOffset — SEQUENCE OF INT16U */
    if (!pdu->member_offset) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->member_offset->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_int16u_t *e = (cms_int16u_t*)pdu->member_offset->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_int16u_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_goose_element_number_response_decode(cms_get_goose_element_number_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;
    int retry_needed = 0;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. gocbReference */
    if (!pdu->gocb_reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->gocb_reference);
    if (err) return err;

    /* 3. confRev */
    if (!pdu->conf_rev) return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->conf_rev);
    if (err) return err;

    /* 4. datSet */
    if (!pdu->dat_set) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->dat_set);
    if (err) return err;

    /* 5. memberOffset */
    if (!pdu->member_offset) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->member_offset->count < (int32_t)cnt) retry_needed = 1;
        pdu->member_offset->count = (int32_t)cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_int16u_decode_stream(&s, retry_needed ? NULL : pdu->member_offset->elements[i]);
            if (err == CMS_RETRY) inner_retry_needed = 1;
            else if (err) return err;
        }
        if (inner_retry_needed) retry_needed = 1;
    }

    return retry_needed ? CMS_RETRY : CMS_OK;
}

/* ── Error ── */

int cms_get_goose_element_number_error_encode(const cms_get_goose_element_number_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. serviceError — ServiceError */
    if (!pdu->service_error) { per_stream_free(&s); return CMS_ERR; }
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_goose_element_number_error_decode(cms_get_goose_element_number_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. serviceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_decode_stream(&s, pdu->service_error);
    if (err) return err;

    return CMS_OK;
}
