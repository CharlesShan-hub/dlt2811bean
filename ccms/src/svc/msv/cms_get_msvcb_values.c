#include "svc/msv/cms_get_msvcb_values.h"
#include "svc/other/cms_req_id.h"
#include "svc/msv/cms_msvcb_value_choice.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_get_msvcb_values_request_encode(const cms_get_msvcb_values_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    if (!pdu->reference) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->reference->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_object_reference_t *e = (cms_object_reference_t*)pdu->reference->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_object_reference_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_msvcb_values_request_decode(cms_get_msvcb_values_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    if (!pdu->reference) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->reference->count < (int32_t)cnt) {
            pdu->reference->count = (int32_t)cnt;
            return CMS_RETRY;
        }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_object_reference_t *e = (cms_object_reference_t*)pdu->reference->elements[i];
            if (!e) return CMS_ERR;
            err = cms_object_reference_decode_stream(&s, e);
            if (err) return err;
        }
    }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_msvcb_values_response_encode(const cms_get_msvcb_values_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    if (!pdu->msvcb) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->msvcb->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_msvcb_value_choice_t *e = (cms_msvcb_value_choice_t*)pdu->msvcb->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_msvcb_value_choice_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    if (!pdu->more_follows) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_msvcb_values_response_decode(cms_get_msvcb_values_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    if (!pdu->msvcb) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->msvcb->count < (int32_t)cnt) {
            pdu->msvcb->count = (int32_t)cnt;
            return CMS_RETRY;
        }
        pdu->msvcb->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_msvcb_value_choice_t *e = (cms_msvcb_value_choice_t*)pdu->msvcb->elements[i];
            if (!e) return CMS_ERR;
            err = cms_msvcb_value_choice_decode_stream(&s, e);
            if (err) return err;
        }
    }

    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_get_msvcb_values_error_encode(const cms_get_msvcb_values_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    if (!pdu->service_error) { per_stream_free(&s); return CMS_ERR; }
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_msvcb_values_error_decode(cms_get_msvcb_values_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_decode_stream(&s, pdu->service_error);
    if (err) return err;

    return CMS_OK;
}
