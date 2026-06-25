#include "svc/connection/cms_release.h"
#include "svc/other/cms_association_id.h"
#include "data/common/cms_service_error.h"

/* ── Request ── */

int cms_release_request_encode_stream(per_stream_t *s, const cms_release_request_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(s, pdu->req_id);
    if (err) return err;

    /* 2. assocId — AssociationId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_encode_stream(s, pdu->assoc_id);
    if (err) return err;

    return CMS_OK;
}

int cms_release_request_decode_stream(per_stream_t *s, cms_release_request_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(s, pdu->req_id);
    if (err) return err;

    /* 2. assocId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_decode_stream(s, pdu->assoc_id);
    if (err) return err;

    return CMS_OK;
}

int cms_release_request_encode(const cms_release_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int rc = cms_release_request_encode_stream(&s, pdu);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_release_request_decode(cms_release_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_release_request_decode_stream(&s, pdu);
}

/* ── Response ── */

int cms_release_response_encode_stream(per_stream_t *s, const cms_release_response_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(s, pdu->req_id);
    if (err) return err;

    /* 2. assocId — AssociationId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_encode_stream(s, pdu->assoc_id);
    if (err) return err;

    /* 3. serviceError — ServiceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_encode_stream(s, pdu->service_error);
    if (err) return err;

    return CMS_OK;
}

int cms_release_response_decode_stream(per_stream_t *s, cms_release_response_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(s, pdu->req_id);
    if (err) return err;

    /* 2. assocId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_decode_stream(s, pdu->assoc_id);
    if (err) return err;

    /* 3. serviceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_decode_stream(s, pdu->service_error);
    if (err) return err;

    return CMS_OK;
}

int cms_release_response_encode(const cms_release_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int rc = cms_release_response_encode_stream(&s, pdu);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_release_response_decode(cms_release_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_release_response_decode_stream(&s, pdu);
}

/* ── Error ── */

int cms_release_error_encode(const cms_release_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { err = CMS_ERR; goto cleanup; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) goto cleanup;

    /* 2. serviceError — ServiceError */
    if (!pdu->service_error) { err = CMS_ERR; goto cleanup; }
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) goto cleanup;

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;

cleanup:
    per_stream_free(&s);
    return err;
}

int cms_release_error_decode(cms_release_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
