#include "svc/sg/cms_select_active_sg.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int8u.h"

/* ── Request ── */

int cms_select_active_sg_request_encode(const cms_select_active_sg_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init)
        return (int) err_init;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 2. sgcbReference — ObjectReference */
    if (!pdu->sgcb_reference)
        return CMS_ERR;
    err = cms_object_reference_encode_stream(&s, pdu->sgcb_reference);
    if (err)
        return err;

    /* 3. settingGroupNumber — INT8U */
    if (!pdu->setting_group_number)
        return CMS_ERR;
    err = cms_int8u_encode_stream(&s, pdu->setting_group_number);
    if (err)
        return err;

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_select_active_sg_request_decode(cms_select_active_sg_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 2. sgcbReference */
    if (!pdu->sgcb_reference)
        return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->sgcb_reference);
    if (err)
        return err;

    /* 3. settingGroupNumber */
    if (!pdu->setting_group_number)
        return CMS_ERR;
    err = cms_int8u_decode_stream(&s, pdu->setting_group_number);
    if (err)
        return err;

    return CMS_OK;
}

/* ── Response (reqId only) ── */

int cms_select_active_sg_response_encode(const cms_select_active_sg_response_t *pdu, uint8_t **out_buf,
                                         size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init)
        return (int) err_init;

    /* 1. reqId — Int16U */
    if (!pdu->req_id)
        return CMS_ERR;
    int err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err)
        return err;

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_select_active_sg_response_decode(cms_select_active_sg_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);

    /* 1. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    return cms_req_id_decode_stream(&s, pdu->req_id);
}

/* ── Error ── */

int cms_select_active_sg_error_encode(const cms_select_active_sg_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init)
        return (int) err_init;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 2. serviceError — ServiceError */
    if (!pdu->service_error) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_select_active_sg_error_decode(cms_select_active_sg_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 2. serviceError */
    if (!pdu->service_error)
        return CMS_ERR;
    err = cms_service_error_decode_stream(&s, pdu->service_error);
    if (err)
        return err;

    return CMS_OK;
}
