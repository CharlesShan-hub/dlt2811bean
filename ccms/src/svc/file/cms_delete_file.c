#include "svc/file/cms_delete_file.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_service_error.h"
#include "data/string/cms_visible_string.h"

/* ── Request ── */

int cms_delete_file_request_encode(const cms_delete_file_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. filename — VisibleString(255) */
    if (!pdu->filename) return CMS_ERR;
    err = cms_visible_string_encode_stream(&s, pdu->filename, 255);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_delete_file_request_decode(cms_delete_file_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. filename */
    if (!pdu->filename) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->filename, 255);
    if (err) return err;

    return CMS_OK;
}

/* ── Response (reqId only) ── */

int cms_delete_file_response_encode(const cms_delete_file_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    int err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_delete_file_response_decode(cms_delete_file_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    return cms_req_id_decode_stream(&s, pdu->req_id);
}

/* ── Error ── */

int cms_delete_file_error_encode(const cms_delete_file_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. serviceError — ServiceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_delete_file_error_decode(cms_delete_file_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
