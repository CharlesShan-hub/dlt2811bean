#include "svc/directory/cms_get_server_directory.h"
#include "svc/directory/cms_object_class.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_server_directory_request_encode(const cms_get_server_directory_request_t *pdu, uint8_t *out_buf, int *out_len)
{
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

    /* 2. objectClass — ObjectClass */
    if (!pdu->object_class) return CMS_ERR;
    err = cms_object_class_encode_stream(&s, pdu->object_class);
    if (err) return err;

    /* 3. refAfter — ObjectReference OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_object_reference_encode_stream(&s, pdu->ref_after);
        if (err) return err;
    }

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_server_directory_request_decode(cms_get_server_directory_request_t *pdu, const uint8_t *in_buf, int in_len)
{
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

    /* 2. objectClass */
    if (!pdu->object_class) return CMS_ERR;
    err = cms_object_class_decode_stream(&s, pdu->object_class);
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

int cms_get_server_directory_response_encode(const cms_get_server_directory_response_t *pdu, uint8_t *out_buf, int *out_len)
{
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. reference — SEQUENCE OF ObjectReference */
    if (!pdu->reference) return CMS_ERR;
    {
        uint32_t count = (uint32_t)pdu->reference->count;
        per_error_t perr = per_encode_length(&s, count);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < count; i++) {
            cms_object_reference_t *elem = (cms_object_reference_t*)pdu->reference->elements[i];
            if (!elem) return CMS_ERR;
            err = cms_object_reference_encode_stream(&s, elem);
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

int cms_get_server_directory_response_decode(cms_get_server_directory_response_t *pdu, const uint8_t *in_buf, int in_len)
{
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. reference */
    if (!pdu->reference) return CMS_ERR;
    {
        uint32_t count;
        per_error_t perr = per_decode_length(&s, &count);
        if (perr) return CMS_ERR;
        pdu->reference->count = (int32_t)count;
        for (uint32_t i = 0; i < count; i++) {
            cms_object_reference_t *elem = (cms_object_reference_t*)pdu->reference->elements[i];
            if (!elem) return CMS_ERR;
            err = cms_object_reference_decode_stream(&s, elem);
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

int cms_get_server_directory_error_encode(const cms_get_server_directory_error_t *pdu, uint8_t *out_buf, int *out_len)
{
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. serviceError */
    if (!pdu->service_error) return CMS_ERR;
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_server_directory_error_decode(cms_get_server_directory_error_t *pdu, const uint8_t *in_buf, int in_len)
{
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
