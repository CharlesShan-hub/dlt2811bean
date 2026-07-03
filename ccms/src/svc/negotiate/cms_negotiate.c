#include "svc/negotiate/cms_negotiate.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"

/* ── Request ── */

int cms_negotiate_request_encode(const cms_negotiate_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
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

    /* 2. apduSize — INT16U */
    if (!pdu->apdu_size) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_int16u_encode_stream(&s, pdu->apdu_size);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 3. asduSize — INT32U */
    if (!pdu->asdu_size) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_int32u_encode_stream(&s, pdu->asdu_size);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 4. protocolVersion — INT32U */
    if (!pdu->protocol_version) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_int32u_encode_stream(&s, pdu->protocol_version);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_negotiate_request_decode(cms_negotiate_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 2. apduSize */
    if (!pdu->apdu_size)
        return CMS_ERR;
    err = cms_int16u_decode_stream(&s, pdu->apdu_size);
    if (err)
        return err;

    /* 3. asduSize */
    if (!pdu->asdu_size)
        return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->asdu_size);
    if (err)
        return err;

    /* 4. protocolVersion */
    if (!pdu->protocol_version)
        return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->protocol_version);
    if (err)
        return err;

    return CMS_OK;
}

/* ── Response ── */

int cms_negotiate_response_encode(const cms_negotiate_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
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

    /* 2. apduSize — INT16U */
    if (!pdu->apdu_size) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_int16u_encode_stream(&s, pdu->apdu_size);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 3. asduSize — INT32U */
    if (!pdu->asdu_size) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_int32u_encode_stream(&s, pdu->asdu_size);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 4. protocolVersion — INT32U */
    if (!pdu->protocol_version) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_int32u_encode_stream(&s, pdu->protocol_version);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 5. modelVersion — VisibleString */
    if (!pdu->model_version) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_visible_string_encode_stream(&s, pdu->model_version, 255);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_negotiate_response_decode(cms_negotiate_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 2. apduSize */
    if (!pdu->apdu_size)
        return CMS_ERR;
    err = cms_int16u_decode_stream(&s, pdu->apdu_size);
    if (err)
        return err;

    /* 3. asduSize */
    if (!pdu->asdu_size)
        return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->asdu_size);
    if (err)
        return err;

    /* 4. protocolVersion */
    if (!pdu->protocol_version)
        return CMS_ERR;
    err = cms_int32u_decode_stream(&s, pdu->protocol_version);
    if (err)
        return err;

    /* 5. modelVersion */
    if (!pdu->model_version)
        return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->model_version, 255);
    if (err)
        return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_negotiate_error_encode(const cms_negotiate_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
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

int cms_negotiate_error_decode(cms_negotiate_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
