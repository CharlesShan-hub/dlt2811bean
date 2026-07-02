#include "svc/log/cms_query_log_after.h"
#include "svc/other/cms_req_id.h"
#include "svc/log/cms_log_entry.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_query_log_after_request_encode(const cms_query_log_after_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 1. OPTIONAL bitmap (1 field: startTime) */
    bool opt_present[1] = {
        (pdu->start_time_present && pdu->start_time_present->value) && pdu->start_time
    };
    err = (int)per_encode_optional_bitmap(&s, opt_present, 1);
    if (err) { per_stream_free(&s); return err; }

    /* 2. log_reference — ObjectReference */
    if (!pdu->log_reference) { per_stream_free(&s); return CMS_ERR; }
    err = cms_object_reference_encode_stream(&s, pdu->log_reference);
    if (err) { per_stream_free(&s); return err; }

    /* 3. startTime — EntryTime OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_entry_time_encode_stream(&s, pdu->start_time);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 4. entry — EntryID */
    if (!pdu->entry) { per_stream_free(&s); return CMS_ERR; }
    err = cms_entry_id_encode_stream(&s, pdu->entry);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_query_log_after_request_decode(cms_query_log_after_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (1 field: startTime) */
    bool opt_present[1];
    err = (int)per_decode_optional_bitmap(&s, opt_present, 1);
    if (err) return err;
    if (pdu->start_time_present) pdu->start_time_present->value = opt_present[0];

    /* 2. log_reference */
    if (!pdu->log_reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(&s, pdu->log_reference);
    if (err) return err;

    /* 3. startTime OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->start_time) return CMS_ERR;
        err = cms_entry_time_decode_stream(&s, pdu->start_time);
        if (err) return err;
    }

    /* 4. entry */
    if (!pdu->entry) return CMS_ERR;
    err = cms_entry_id_decode_stream(&s, pdu->entry);
    if (err) return err;

    return CMS_OK;
}

/* ── Response ── */

int cms_query_log_after_response_encode(const cms_query_log_after_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    if (!pdu->log_entry) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->log_entry->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_log_entry_t *e = (cms_log_entry_t*)pdu->log_entry->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_log_entry_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    if (!pdu->more_follows) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_query_log_after_response_decode(cms_query_log_after_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    if (!pdu->log_entry) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->log_entry->count < (int32_t)cnt) {
            pdu->log_entry->count = (int32_t)cnt;
            return CMS_RETRY;
        }
        pdu->log_entry->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_log_entry_t *e = (cms_log_entry_t*)pdu->log_entry->elements[i];
            if (!e) return CMS_ERR;
            err = cms_log_entry_decode_stream(&s, e);
            if (err) return err;
        }
    }

    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_query_log_after_error_encode(const cms_query_log_after_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
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

int cms_query_log_after_error_decode(cms_query_log_after_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
