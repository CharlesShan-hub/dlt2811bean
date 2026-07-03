#include "svc/data/cms_set_data_values.h"
#include "svc/other/cms_req_id.h"
#include "svc/data/cms_data_ref_value_entry.h"
#include "data/common/cms_service_error.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_set_data_values_request_encode(const cms_set_data_values_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* reqId — mandatory */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* data — SEQUENCE OF DataRefValueEntry */
    if (!pdu->data) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->data->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_ref_value_entry_t *e = (cms_data_ref_value_entry_t*)pdu->data->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_data_ref_value_entry_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_set_data_values_request_decode(cms_set_data_values_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;
    int retry_needed = 0;

    /* reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* data */
    if (!pdu->data) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->data->count < (int32_t)cnt) retry_needed = 1;
        pdu->data->count = (int32_t)cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_data_ref_value_entry_decode_stream(&s, retry_needed ? NULL : pdu->data->elements[i]);
            if (err == CMS_RETRY) inner_retry_needed = 1;
            else if (err) return err;
        }
        if (inner_retry_needed) retry_needed = 1;
    }

    return retry_needed ? CMS_RETRY : CMS_OK;
}

/* ── Response ── */

int cms_set_data_values_response_encode(const cms_set_data_values_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;

    /* reqId — only field */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    int err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_set_data_values_response_decode(cms_set_data_values_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    if (!pdu->req_id) return CMS_ERR;
    return cms_req_id_decode_stream(&s, pdu->req_id);
}

/* ── Error ── */

int cms_set_data_values_error_encode(const cms_set_data_values_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;
    int retry_needed = 0;

    /* reqId */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* result — SEQUENCE OF ServiceError */
    if (!pdu->result) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->result->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_service_error_t *e = (cms_service_error_t*)pdu->result->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_service_error_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_set_data_values_error_decode(cms_set_data_values_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;
    int retry_needed = 0;

    /* reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* result */
    if (!pdu->result) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->result->count < (int32_t)cnt) retry_needed = 1;
        pdu->result->count = (int32_t)cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_service_error_decode_stream(&s, retry_needed ? NULL : pdu->result->elements[i]);
            if (err == CMS_RETRY) inner_retry_needed = 1;
            else if (err) return err;
        }
        if (inner_retry_needed) retry_needed = 1;
    }

    return retry_needed ? CMS_RETRY : CMS_OK;
}
