#include "svc/rpc/cms_get_rpc_interface_definition.h"
#include "svc/other/cms_req_id.h"
#include "svc/rpc/cms_rpc_method_entry.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_rpc_interface_definition_request_encode(const cms_get_rpc_interface_definition_request_t *pdu,
                                                    uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 1. OPTIONAL bitmap (1 field: refAfter) */
    bool opt_present[1] = {(pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after};
    err = (int) per_encode_optional_bitmap(&s, opt_present, 1);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 2. interfaceName — VisibleString */
    if (!pdu->interface_name) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_visible_string_encode_stream(&s, pdu->interface_name, UINT32_MAX);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 3. refAfter — VisibleString OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_visible_string_encode_stream(&s, pdu->ref_after, UINT32_MAX);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_rpc_interface_definition_request_decode(cms_get_rpc_interface_definition_request_t *pdu,
                                                    const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 1. OPTIONAL bitmap (1 field) */
    bool opt_present[1];
    err = (int) per_decode_optional_bitmap(&s, opt_present, 1);
    if (err)
        return err;
    if (pdu->ref_after_present)
        pdu->ref_after_present->value = opt_present[0];

    /* 2. interfaceName */
    if (!pdu->interface_name)
        return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->interface_name, UINT32_MAX);
    if (err)
        return err;

    /* 3. refAfter OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->ref_after)
            return CMS_ERR;
        err = cms_visible_string_decode_stream(&s, pdu->ref_after, UINT32_MAX);
        if (err)
            return err;
    }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_rpc_interface_definition_response_encode(const cms_get_rpc_interface_definition_response_t *pdu,
                                                     uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init)
        return (int) err_init;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 1. method — SEQUENCE OF RpcMethodEntry */
    if (!pdu->method) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    {
        uint32_t cnt = (uint32_t) pdu->method->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) {
            per_stream_free(&s);
            return CMS_ERR;
        }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_rpc_method_entry_t *e = (cms_rpc_method_entry_t *) pdu->method->elements[i];
            if (!e) {
                per_stream_free(&s);
                return CMS_ERR;
            }
            err = cms_rpc_method_entry_encode_stream(&s, e);
            if (err) {
                per_stream_free(&s);
                return err;
            }
        }
    }

    /* 2. moreFollows — BOOLEAN DEFAULT TRUE */
    if (!pdu->more_follows) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_rpc_interface_definition_response_decode(cms_get_rpc_interface_definition_response_t *pdu,
                                                     const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;
    int retry_needed = 0;

    /* 0. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 1. method */
    if (!pdu->method)
        return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr)
            return CMS_ERR;
        if (pdu->method->count < (int32_t) cnt)
            retry_needed = 1;
        pdu->method->count = (int32_t) cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_rpc_method_entry_decode_stream(&s, retry_needed ? NULL : pdu->method->elements[i]);
            if (err == CMS_RETRY)
                inner_retry_needed = 1;
            else if (err)
                return err;
        }
        if (inner_retry_needed)
            retry_needed = 1;
    }

    /* 2. moreFollows */
    if (!pdu->more_follows)
        return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err)
        return err;

    return retry_needed ? CMS_RETRY : CMS_OK;
}

/* ── Error ── */

int cms_get_rpc_interface_definition_error_encode(const cms_get_rpc_interface_definition_error_t *pdu,
                                                  uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 1. serviceError — ServiceError */
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

int cms_get_rpc_interface_definition_error_decode(cms_get_rpc_interface_definition_error_t *pdu, const uint8_t *in_buf,
                                                  int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    /* 1. serviceError */
    if (!pdu->service_error)
        return CMS_ERR;
    err = cms_service_error_decode_stream(&s, pdu->service_error);
    if (err)
        return err;

    return CMS_OK;
}
