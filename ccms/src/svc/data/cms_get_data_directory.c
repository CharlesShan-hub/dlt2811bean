#include "svc/data/cms_get_data_directory.h"
#include "svc/other/cms_req_id.h"
#include "svc/data/cms_sub_ref_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_data_directory_request_encode(const cms_get_data_directory_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: refAfter) */
    bool opt[1] = {
        (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after
    };
    err = (int)per_encode_optional_bitmap(&s, opt, 1);
    if (err) return err;

    /* 2. dataReference — ObjectReference */
    if (!pdu->data_reference) return CMS_ERR;
    err = cms_object_reference_encode_stream(&s, pdu->data_reference);
    if (err) return err;

    /* 3. refAfter — ObjectReference OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_object_reference_encode_stream(&s, pdu->ref_after);
        if (err) return err;
    }

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_data_directory_request_decode(cms_get_data_directory_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: refAfter) */
    bool opt[1] = {false};
    err = (int)per_decode_optional_bitmap(&s, opt, 1);
    if (err) return err;
    if (pdu->ref_after_present)
        pdu->ref_after_present->value = opt[0] ? 1 : 0;

    /* 2. dataReference */
    if (!pdu->data_reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->data_reference);
    if (err) return err;

    /* 3. refAfter OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (!pdu->ref_after) return CMS_ERR;
        err = cms_object_reference_decode_stream(&s, pdu->ref_after);
        if (err) return err;
    }

    return CMS_OK;
}

/* ── Response (no OPTIONAL) ── */

int cms_get_data_directory_response_encode(const cms_get_data_directory_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. dataAttribute — SEQUENCE OF SubRefEntry */
    if (!pdu->data_attribute) return CMS_ERR;
    {
        uint32_t cnt = (uint32_t)pdu->data_attribute->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_sub_ref_entry_t *e = (cms_sub_ref_entry_t*)pdu->data_attribute->elements[i];
            if (!e) return CMS_ERR;
            err = cms_sub_ref_entry_encode_stream(&s, e);
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

int cms_get_data_directory_response_decode(cms_get_data_directory_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. dataAttribute */
    if (!pdu->data_attribute) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        pdu->data_attribute->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_sub_ref_entry_t *e = (cms_sub_ref_entry_t*)pdu->data_attribute->elements[i];
            if (!e) return CMS_ERR;
            err = cms_sub_ref_entry_decode_stream(&s, e);
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

int cms_get_data_directory_error_encode(const cms_get_data_directory_error_t *pdu, uint8_t *out_buf, int *out_len) {
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

int cms_get_data_directory_error_decode(cms_get_data_directory_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
