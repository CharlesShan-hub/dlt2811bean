#include "svc/directory/cms_get_logical_device_directory.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_sub_reference.h"
#include "data/common/cms_service_error.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_logical_device_directory_request_encode(const cms_get_logical_device_directory_request_t *pdu,
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

    /* 1. OPTIONAL bitmap (2 fields: ldName, refAfter) */
    bool opt[2] = {(pdu->ld_name_present && pdu->ld_name_present->value) && pdu->ld_name,
                   (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after};
    err = (int) per_encode_optional_bitmap(&s, opt, 2);
    if (err) {
        per_stream_free(&s);
        return err;
    }

    /* 2. ldName — ObjectName OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_object_name_encode_stream(&s, pdu->ld_name);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    /* 3. refAfter — ObjectReference OPTIONAL (bitmap[1]) */
    if (opt[1]) {
        err = cms_object_reference_encode_stream(&s, pdu->ref_after);
        if (err) {
            per_stream_free(&s);
            return err;
        }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_logical_device_directory_request_decode(cms_get_logical_device_directory_request_t *pdu,
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

    /* 1. OPTIONAL bitmap (2 fields: ldName, refAfter) */
    bool opt[2] = {false, false};
    err = (int) per_decode_optional_bitmap(&s, opt, 2);
    if (err)
        return err;
    if (pdu->ld_name_present)
        pdu->ld_name_present->value = opt[0] ? 1 : 0;
    if (pdu->ref_after_present)
        pdu->ref_after_present->value = opt[1] ? 1 : 0;

    /* 2. ldName OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (!pdu->ld_name)
            return CMS_ERR;
        err = cms_object_name_decode_stream(&s, pdu->ld_name);
        if (err)
            return err;
    }

    /* 3. refAfter OPTIONAL (bitmap[1]) */
    if (opt[1]) {
        if (!pdu->ref_after)
            return CMS_ERR;
        err = cms_object_reference_decode_stream(&s, pdu->ref_after);
        if (err)
            return err;
    }

    return CMS_OK;
}

/* ── Response (no OPTIONAL) ── */

int cms_get_logical_device_directory_response_encode(const cms_get_logical_device_directory_response_t *pdu,
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

    /* 1. lnReference — SEQUENCE OF SubReference */
    if (!pdu->ln_reference) {
        per_stream_free(&s);
        return CMS_ERR;
    }
    {
        uint32_t cnt = (uint32_t) pdu->ln_reference->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) {
            per_stream_free(&s);
            return CMS_ERR;
        }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_sub_reference_t *e = (cms_sub_reference_t *) pdu->ln_reference->elements[i];
            if (!e) {
                per_stream_free(&s);
                return CMS_ERR;
            }
            err = cms_sub_reference_encode_stream(&s, e);
            if (err) {
                per_stream_free(&s);
                return err;
            }
        }
    }

    /* 2. moreFollows — BOOLEAN */
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

int cms_get_logical_device_directory_response_decode(cms_get_logical_device_directory_response_t *pdu,
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

    /* 1. lnReference */
    if (!pdu->ln_reference)
        return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr)
            return CMS_ERR;
        if (pdu->ln_reference->count < (int32_t) cnt)
            retry_needed = 1;
        pdu->ln_reference->count = (int32_t) cnt;
        int inner_retry_needed = 0;
        for (uint32_t i = 0; i < cnt; i++) {
            err = cms_sub_reference_decode_stream(&s, retry_needed ? NULL : pdu->ln_reference->elements[i]);
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

/* ── Error (no OPTIONAL) ── */

int cms_get_logical_device_directory_error_encode(const cms_get_logical_device_directory_error_t *pdu,
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

int cms_get_logical_device_directory_error_decode(cms_get_logical_device_directory_error_t *pdu, const uint8_t *in_buf,
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
