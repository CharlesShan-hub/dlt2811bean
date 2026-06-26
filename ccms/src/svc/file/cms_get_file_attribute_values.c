#include "svc/file/cms_get_file_attribute_values.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_file_entry.h"
#include "data/common/cms_service_error.h"
#include "data/string/cms_visible_string.h"

/* ── Request ── */

int cms_get_file_attribute_values_request_encode(const cms_get_file_attribute_values_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. filename — VisibleString(255) */
    if (!pdu->filename) { per_stream_free(&s); return CMS_ERR; }
    err = cms_visible_string_encode_stream(&s, pdu->filename, 255);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_file_attribute_values_request_decode(cms_get_file_attribute_values_request_t *pdu, const uint8_t *in_buf, int in_len) {
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

/* ── Response ── */

int cms_get_file_attribute_values_response_encode(const cms_get_file_attribute_values_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. fileEntry — FileEntry */
    if (!pdu->file_entry) { per_stream_free(&s); return CMS_ERR; }
    err = cms_file_entry_encode_stream(&s, pdu->file_entry);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_file_attribute_values_response_decode(cms_get_file_attribute_values_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. fileEntry */
    if (!pdu->file_entry) return CMS_ERR;
    err = cms_file_entry_decode_stream(&s, pdu->file_entry);
    if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_get_file_attribute_values_error_encode(const cms_get_file_attribute_values_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
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

int cms_get_file_attribute_values_error_decode(cms_get_file_attribute_values_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
