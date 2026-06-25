#include "svc/dataset/cms_get_data_set_values.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_data_set_values_request_encode(const cms_get_data_set_values_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: referenceAfter) */
    bool opt[1] = {
        (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after
    };
    err = (int)per_encode_optional_bitmap(&s, opt, 1);
    if (err) return err;

    /* 2. datasetReference — ObjectReference */
    if (!pdu->dataset_reference) return CMS_ERR;
    err = cms_object_reference_encode_stream(&s, pdu->dataset_reference);
    if (err) return err;

    /* 3. referenceAfter — ObjectReference OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_object_reference_encode_stream(&s, pdu->ref_after);
        if (err) return err;
    }

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_data_set_values_request_decode(cms_get_data_set_values_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: referenceAfter) */
    bool opt[1] = {false};
    err = (int)per_decode_optional_bitmap(&s, opt, 1);
    if (err) return err;
    if (pdu->ref_after_present)
        pdu->ref_after_present->value = opt[0] ? 1 : 0;

    /* 2. datasetReference */
    if (!pdu->dataset_reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->dataset_reference);
    if (err) return err;

    /* 3. referenceAfter OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (!pdu->ref_after) return CMS_ERR;
        err = cms_object_reference_decode_stream(&s, pdu->ref_after);
        if (err) return err;
    }

    return CMS_OK;
}

/* ── Response (no OPTIONAL) ── */

int cms_get_data_set_values_response_encode(const cms_get_data_set_values_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. value — SEQUENCE OF Data */
    if (!pdu->value) return CMS_ERR;
    {
        uint32_t cnt = (uint32_t)pdu->value->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->value->elements[i];
            if (!e) return CMS_ERR;
            err = cms_data_encode_stream(&s, e);
            if (err) return err;
        }
    }

    /* 2. moreFollows — BOOLEAN */
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_data_set_values_response_decode(cms_get_data_set_values_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. value */
    if (!pdu->value) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        pdu->value->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_data_t *e = (cms_data_t*)pdu->value->elements[i];
            if (!e) return CMS_ERR;
            err = cms_data_decode_stream(&s, e);
            if (err) return err;
        }
    }

    /* 2. moreFollows */
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err) return err;

    return CMS_OK;
}

/* ── Error (no OPTIONAL) ── */

int cms_get_data_set_values_error_encode(const cms_get_data_set_values_error_t *pdu, uint8_t *out_buf, int *out_len) {
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

int cms_get_data_set_values_error_decode(cms_get_data_set_values_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
