#include "svc/rpc/cms_rpc_call.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_service_error.h"
#include "data/string/cms_visible_string.h"
#include "data/string/cms_octet_string.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_rpc_call_request_encode(const cms_rpc_call_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. method — VisibleString */
    if (!pdu->method) return CMS_ERR;
    err = cms_visible_string_encode_stream(&s, pdu->method, UINT32_MAX);
    if (err) return err;

    /* 2. req — RpcCallReqChoice(Data/CallId) */
    if (!pdu->req) return CMS_ERR;
    err = cms_rpc_call_req_choice_encode_stream(&s, pdu->req);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_rpc_call_request_decode(cms_rpc_call_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. method */
    if (!pdu->method) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->method, UINT32_MAX);
    if (err) return err;

    /* 2. req */
    if (!pdu->req) return CMS_ERR;
    err = cms_rpc_call_req_choice_decode_stream(&s, pdu->req);
    if (err) return err;

    return CMS_OK;
}

/* ── Response ── */

int cms_rpc_call_response_encode(const cms_rpc_call_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: nextCallId) */
    bool opt_present[1] = {
        (pdu->next_call_id_present && pdu->next_call_id_present->value) && pdu->next_call_id
    };
    err = (int)per_encode_optional_bitmap(&s, opt_present, 1);
    if (err) return err;

    /* 2. rspData — Data */
    if (!pdu->rsp_data) return CMS_ERR;
    err = cms_data_encode_stream(&s, pdu->rsp_data);
    if (err) return err;

    /* 3. nextCallId — OCTET STRING OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_octet_string_encode_stream(&s, pdu->next_call_id, UINT32_MAX);
        if (err) return err;
    }

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_rpc_call_response_decode(cms_rpc_call_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field) */
    bool opt_present[1];
    err = (int)per_decode_optional_bitmap(&s, opt_present, 1);
    if (err) return err;
    if (pdu->next_call_id_present) pdu->next_call_id_present->value = opt_present[0];

    /* 2. rspData */
    if (!pdu->rsp_data) return CMS_ERR;
    err = cms_data_decode_stream(&s, pdu->rsp_data);
    if (err) return err;

    /* 3. nextCallId OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->next_call_id) return CMS_ERR;
        err = cms_octet_string_decode_stream(&s, pdu->next_call_id, UINT32_MAX);
        if (err) return err;
    }

    return CMS_OK;
}

/* ── Error ── */

int cms_rpc_call_error_encode(const cms_rpc_call_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. serviceError — ServiceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_rpc_call_error_decode(cms_rpc_call_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. serviceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_decode_stream(&s, pdu->service_error);
    if (err) return err;

    return CMS_OK;
}
