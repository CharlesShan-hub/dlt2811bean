#include "svc/rpc/cms_get_rpc_method_definition.h"
#include "svc/other/cms_req_id.h"
#include "svc/rpc/cms_rpc_method_def_choice.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_get_rpc_method_definition_request_encode(const cms_get_rpc_method_definition_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. reference — SEQUENCE OF VisibleString */
    if (!pdu->reference) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->reference->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_uint8_array_t *e = (cms_uint8_array_t*)pdu->reference->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_visible_string_encode_stream(&s, e, UINT32_MAX);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_rpc_method_definition_request_decode(cms_get_rpc_method_definition_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;

    /* 2. reference */
    if (!pdu->reference) return CMS_ERR;
    { uint32_t cnt; per_error_t perr = per_decode_length(&s, &cnt); if (perr) return CMS_ERR; pdu->reference->count = (int32_t)cnt; for (uint32_t i = 0; i < cnt; i++) { cms_uint8_array_t *e = (cms_uint8_array_t*)pdu->reference->elements[i]; if (!e) return CMS_ERR; err = cms_visible_string_decode_stream(&s, e, UINT32_MAX); if (err) return err; } }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_rpc_method_definition_response_encode(const cms_get_rpc_method_definition_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 2. reference — SEQUENCE OF RpcMethodDefChoice */
    if (!pdu->reference) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->reference->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_rpc_method_def_choice_t *e = (cms_rpc_method_def_choice_t*)pdu->reference->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_rpc_method_def_choice_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    /* 3. moreFollows — BOOLEAN DEFAULT TRUE */
    if (!pdu->more_follows) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_rpc_method_definition_response_decode(cms_get_rpc_method_definition_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;

    /* 2. reference */
    if (!pdu->reference) return CMS_ERR;
    { uint32_t cnt; per_error_t perr = per_decode_length(&s, &cnt); if (perr) return CMS_ERR; pdu->reference->count = (int32_t)cnt; for (uint32_t i = 0; i < cnt; i++) { cms_rpc_method_def_choice_t *e = (cms_rpc_method_def_choice_t*)pdu->reference->elements[i]; if (!e) return CMS_ERR; err = cms_rpc_method_def_choice_decode_stream(&s, e); if (err) return err; } }

    /* 3. moreFollows */
    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_decode_stream(&s, pdu->more_follows); if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_get_rpc_method_definition_error_encode(const cms_get_rpc_method_definition_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
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

int cms_get_rpc_method_definition_error_decode(cms_get_rpc_method_definition_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_decode_stream(&s, pdu->service_error); if (err) return err;
    return CMS_OK;
}
