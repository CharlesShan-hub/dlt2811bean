#include "svc/rpc/cms_get_rpc_method_directory.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_rpc_method_directory_request_encode(const cms_get_rpc_method_directory_request_t *pdu, uint8_t **out_buf,
                                                size_t *out_len) {
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

    /* 1. OPTIONAL bitmap (2 fields: interface, refAfter) */
    bool opt_present[2] = {(pdu->interface_present && pdu->interface_present->value) && pdu->interface_name,
                           (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after};
    err = (int) per_encode_optional_bitmap(&s, opt_present, 2);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 2. interface — VisibleString OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_visible_string_encode_stream(&s, pdu->interface_name, UINT32_MAX);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 3. referenceAfter — VisibleString OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_visible_string_encode_stream(&s, pdu->ref_after, UINT32_MAX);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_rpc_method_directory_request_decode(cms_get_rpc_method_directory_request_t *pdu, const uint8_t *in_buf,
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

    /* 1. OPTIONAL bitmap (2 fields) */
    bool opt_present[2];
    err = (int) per_decode_optional_bitmap(&s, opt_present, 2);
    if (err)
        return err;
    if (pdu->interface_present)
        pdu->interface_present->value = opt_present[0];
    if (pdu->ref_after_present)
        pdu->ref_after_present->value = opt_present[1];

    /* 2. interface OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->interface_name)
            return CMS_ERR;
        err = cms_visible_string_decode_stream(&s, pdu->interface_name, UINT32_MAX);
        if (err)
            return err;
    }

    /* 3. referenceAfter OPTIONAL */
    if (opt_present[1]) {
        if (!pdu->ref_after)
            return CMS_ERR;
        err = cms_visible_string_decode_stream(&s, pdu->ref_after, UINT32_MAX);
        if (err)
            return err;
    }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_rpc_method_directory_response_encode(const cms_get_rpc_method_directory_response_t *pdu, uint8_t **out_buf,
                                                 size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
    int err;

    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    if (!pdu->reference) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    {
        uint32_t cnt = (uint32_t) pdu->reference->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) {
            per_stream_free(&s);
            return CMS_ERR;
        }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_uint8_array_t *e = (cms_uint8_array_t *) pdu->reference->elements[i];
            if (!e) {
                per_stream_free(&s);
                return CMS_ERR;
            }
            err = cms_visible_string_encode_stream(&s, e, UINT32_MAX);
            if (err) {
                per_stream_free(&s);
                return err;
            }
        }
    }

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

int cms_get_rpc_method_directory_response_decode(cms_get_rpc_method_directory_response_t *pdu, const uint8_t *in_buf,
                                                 int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;
    int retry_needed = 0;

    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;

    if (!pdu->reference)
        return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr)
            return CMS_ERR;
        if (pdu->reference->count < (int32_t) cnt)
            retry_needed = 1;
        pdu->reference->count = (int32_t) cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_visible_string_decode_stream(&s, retry_needed ? NULL : pdu->reference->elements[i], UINT32_MAX);
            if (err == CMS_RETRY)
                inner_retry_needed = 1;
            else if (err)
                return err;
        }
        if (inner_retry_needed)
            retry_needed = 1;
    }

    if (!pdu->more_follows)
        return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err)
        return err;

    return retry_needed ? CMS_RETRY : CMS_OK;
}

/* ── Error ── */

int cms_get_rpc_method_directory_error_encode(const cms_get_rpc_method_directory_error_t *pdu, uint8_t **out_buf,
                                              size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i)
        return (int) err_i;
    int err;

    if (!pdu->req_id) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) {
        per_stream_free(&s);
        return err;
    }

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

int cms_get_rpc_method_directory_error_decode(cms_get_rpc_method_directory_error_t *pdu, const uint8_t *in_buf,
                                              int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t) in_len);
    int err;
    if (!pdu->req_id)
        return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err)
        return err;
    if (!pdu->service_error)
        return CMS_ERR;
    err = cms_service_error_decode_stream(&s, pdu->service_error);
    if (err)
        return err;
    return CMS_OK;
}
