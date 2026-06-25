#include "svc/control/cms_select.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_reference.h"

/* ── Request ── */

int cms_select_request_encode(const cms_select_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. reference — ObjectReference */
    if (!pdu->reference) return CMS_ERR;
    err = cms_object_reference_encode_stream(&s, pdu->reference);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_select_request_decode(cms_select_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. reference */
    if (!pdu->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->reference);
    if (err) return err;

    return CMS_OK;
}

/* ── Response (same as Request) ── */

int cms_select_response_encode(const cms_select_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    return cms_select_request_encode((const cms_select_request_t*)pdu, out_buf, out_len);
}

int cms_select_response_decode(cms_select_response_t *pdu, const uint8_t *in_buf, int in_len) {
    return cms_select_request_decode((cms_select_request_t*)pdu, in_buf, in_len);
}

/* ── Error (same as Request) ── */

int cms_select_error_encode(const cms_select_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    return cms_select_request_encode((const cms_select_request_t*)pdu, out_buf, out_len);
}

int cms_select_error_decode(cms_select_error_t *pdu, const uint8_t *in_buf, int in_len) {
    return cms_select_request_decode((cms_select_request_t*)pdu, in_buf, in_len);
}
