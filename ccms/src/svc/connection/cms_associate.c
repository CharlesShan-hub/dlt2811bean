#include "svc/connection/cms_associate.h"
#include "svc/other/cms_association_id.h"
#include "svc/connection/cms_authentication_parameter.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_sequence.h"

#define SAP_REF_MAX_LEN 129

/* ── Request ── */

int cms_associate_request_encode_stream(per_stream_t *s, const cms_associate_request_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (2 fields: sapRef, authParam) */
    bool opt_present[2] = {
        (pdu->sap_ref_present && pdu->sap_ref_present->value) && pdu->sap_ref,
        (pdu->auth_param_present && pdu->auth_param_present->value) && pdu->auth_param
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 2);
    if (err) return err;

    /* 2. sapRef — VisibleString OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_visible_string_encode_stream(s, pdu->sap_ref, SAP_REF_MAX_LEN);
        if (err) return err;
    }

    /* 3. authParam — AuthenticationParameter OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_authentication_parameter_encode_stream(s, pdu->auth_param);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_associate_request_decode_stream(per_stream_t *s, cms_associate_request_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (2 fields: sapRef, authParam) */
    bool opt_present[2] = {false, false};
    err = (int)per_decode_optional_bitmap(s, opt_present, 2);
    if (err) return err;
    if (pdu->sap_ref_present)
        pdu->sap_ref_present->value = opt_present[0] ? 1 : 0;
    if (pdu->auth_param_present)
        pdu->auth_param_present->value = opt_present[1] ? 1 : 0;

    /* 2. sapRef OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        if (!pdu->sap_ref) return CMS_ERR;
        err = cms_visible_string_decode_stream(s, pdu->sap_ref, SAP_REF_MAX_LEN);
        if (err) return err;
    }

    /* 3. authParam OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        if (!pdu->auth_param) return CMS_ERR;
        err = cms_authentication_parameter_decode_stream(s, pdu->auth_param);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_associate_request_encode(const cms_associate_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_associate_request_encode_stream(&s, pdu);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_associate_request_decode(cms_associate_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_associate_request_decode_stream(&s, pdu);
}

/* ── Response ── */

int cms_associate_response_encode_stream(per_stream_t *s, const cms_associate_response_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: authParam) */
    bool res_opt[1] = {
        (pdu->auth_param_present && pdu->auth_param_present->value) && pdu->auth_param
    };
    err = (int)per_encode_optional_bitmap(s, res_opt, 1);
    if (err) return err;

    /* 2. assocId — AssociationId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_encode_stream(s, pdu->assoc_id);
    if (err) return err;

    /* 3. serviceError — ServiceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_encode_stream(s, pdu->service_error);
    if (err) return err;

    /* 4. authParam — AuthenticationParameter OPTIONAL (bitmap[0]) */
    if (res_opt[0]) {
        err = cms_authentication_parameter_encode_stream(s, pdu->auth_param);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_associate_response_decode_stream(per_stream_t *s, cms_associate_response_t *pdu) {
    if (!pdu) return CMS_ERR;
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: authParam) */
    bool res_opt[1] = {false};
    err = (int)per_decode_optional_bitmap(s, res_opt, 1);
    if (err) return err;
    if (pdu->auth_param_present)
        pdu->auth_param_present->value = res_opt[0] ? 1 : 0;

    /* 2. assocId */
    if (!pdu->assoc_id) return CMS_ERR;
    err = cms_association_id_decode_stream(s, pdu->assoc_id);
    if (err) return err;

    /* 3. serviceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_decode_stream(s, pdu->service_error);
    if (err) return err;

    /* 4. authParam OPTIONAL (bitmap[0]) */
    if (res_opt[0]) {
        if (!pdu->auth_param) return CMS_ERR;
        err = cms_authentication_parameter_decode_stream(s, pdu->auth_param);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_associate_response_encode(const cms_associate_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_associate_response_encode_stream(&s, pdu);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_associate_response_decode(cms_associate_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_associate_response_decode_stream(&s, pdu);
}

/* ── Error ── */

int cms_associate_error_encode(const cms_associate_error_t *pdu, uint8_t *out_buf, int *out_len) {
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

int cms_associate_error_decode(cms_associate_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
