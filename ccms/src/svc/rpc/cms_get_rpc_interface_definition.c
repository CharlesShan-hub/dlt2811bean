#include "svc/rpc/cms_get_rpc_interface_definition.h"
#include "svc/other/cms_req_id.h"
#include "svc/rpc/cms_rpc_method_entry.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_get_rpc_interface_definition_request_encode(const cms_get_rpc_interface_definition_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. interfaceName — VisibleString */
    if (!pdu->interface_name) return CMS_ERR;
    err = cms_visible_string_encode_stream(&s, pdu->interface_name, UINT32_MAX);
    if (err) return err;

    /* 3. refAfter — VisibleString OPTIONAL */
    {
        int present = (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(&s, &bit);
        if (err) return err;
        if (present) {
            err = cms_visible_string_encode_stream(&s, pdu->ref_after, UINT32_MAX);
            if (err) return err;
        }
    }

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_rpc_interface_definition_request_decode(cms_get_rpc_interface_definition_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. interfaceName */
    if (!pdu->interface_name) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->interface_name, UINT32_MAX);
    if (err) return err;

    /* 3. refAfter OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(&s, &bit);
        if (err) return err;
        if (pdu->ref_after_present) pdu->ref_after_present->value = bit.value;
        if (bit.value) {
            if (!pdu->ref_after) return CMS_ERR;
            err = cms_visible_string_decode_stream(&s, pdu->ref_after, UINT32_MAX);
            if (err) return err;
        }
    }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_rpc_interface_definition_response_encode(const cms_get_rpc_interface_definition_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. method — SEQUENCE OF RpcMethodEntry */
    if (!pdu->method) return CMS_ERR;
    {
        uint32_t cnt = (uint32_t)pdu->method->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_rpc_method_entry_t *e = (cms_rpc_method_entry_t*)pdu->method->elements[i];
            if (!e) return CMS_ERR;
            err = cms_rpc_method_entry_encode_stream(&s, e);
            if (err) return err;
        }
    }

    /* 3. moreFollows — BOOLEAN DEFAULT TRUE */
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_get_rpc_interface_definition_response_decode(cms_get_rpc_interface_definition_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 2. method */
    if (!pdu->method) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        pdu->method->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_rpc_method_entry_t *e = (cms_rpc_method_entry_t*)pdu->method->elements[i];
            if (!e) return CMS_ERR;
            err = cms_rpc_method_entry_decode_stream(&s, e);
            if (err) return err;
        }
    }

    /* 3. moreFollows */
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_get_rpc_interface_definition_error_encode(const cms_get_rpc_interface_definition_error_t *pdu, uint8_t *out_buf, int *out_len) {
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

int cms_get_rpc_interface_definition_error_decode(cms_get_rpc_interface_definition_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
