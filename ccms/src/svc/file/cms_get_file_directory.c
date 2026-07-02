#include "svc/file/cms_get_file_directory.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_file_entry.h"
#include "data/common/cms_service_error.h"
#include "data/common/cms_time_stamp.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"
#include "per/cms_sequence.h"

/* ── Request ── */

int cms_get_file_directory_request_encode(const cms_get_file_directory_request_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_init = per_stream_init_write(&s, 64);
    if (err_init) return (int)err_init;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 1. OPTIONAL bitmap (3 fields: startTime, stopTime, fileAfter) */
    bool opt_present[3] = {
        (pdu->start_time_present && pdu->start_time_present->value) && pdu->start_time,
        (pdu->stop_time_present && pdu->stop_time_present->value) && pdu->stop_time,
        (pdu->file_after_present && pdu->file_after_present->value) && pdu->file_after
    };
    err = (int)per_encode_optional_bitmap(&s, opt_present, 3);
    if (err) { per_stream_free(&s); return err; }

    /* 2. pathName — VisibleString(255) */
    if (!pdu->path_name) { per_stream_free(&s); return CMS_ERR; }
    err = cms_visible_string_encode_stream(&s, pdu->path_name, 255);
    if (err) { per_stream_free(&s); return err; }

    /* 3. startTime — TimeStamp OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_time_stamp_encode_stream(&s, pdu->start_time);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 4. stopTime — TimeStamp OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_time_stamp_encode_stream(&s, pdu->stop_time);
        if (err) { per_stream_free(&s); return err; }
    }

    /* 5. fileAfter — VisibleString(255) OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_visible_string_encode_stream(&s, pdu->file_after, 255);
        if (err) { per_stream_free(&s); return err; }
    }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_file_directory_request_decode(cms_get_file_directory_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. OPTIONAL bitmap (3 fields) */
    bool opt_present[3];
    err = (int)per_decode_optional_bitmap(&s, opt_present, 3);
    if (err) return err;
    if (pdu->start_time_present) pdu->start_time_present->value = opt_present[0];
    if (pdu->stop_time_present) pdu->stop_time_present->value = opt_present[1];
    if (pdu->file_after_present) pdu->file_after_present->value = opt_present[2];

    /* 2. pathName */
    if (!pdu->path_name) return CMS_ERR;
    err = cms_visible_string_decode_stream(&s, pdu->path_name, 255);
    if (err) return err;

    /* 3. startTime OPTIONAL */
    if (opt_present[0]) {
        if (!pdu->start_time) return CMS_ERR;
        err = cms_time_stamp_decode_stream(&s, pdu->start_time);
        if (err) return err;
    }

    /* 4. stopTime OPTIONAL */
    if (opt_present[1]) {
        if (!pdu->stop_time) return CMS_ERR;
        err = cms_time_stamp_decode_stream(&s, pdu->stop_time);
        if (err) return err;
    }

    /* 5. fileAfter OPTIONAL */
    if (opt_present[2]) {
        if (!pdu->file_after) return CMS_ERR;
        err = cms_visible_string_decode_stream(&s, pdu->file_after, 255);
        if (err) return err;
    }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_file_directory_response_encode(const cms_get_file_directory_response_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 1. fileEntry — SEQUENCE OF FileEntry */
    if (!pdu->file_entry) { per_stream_free(&s); return CMS_ERR; }
    {
        uint32_t cnt = (uint32_t)pdu->file_entry->count;
        per_error_t perr = per_encode_length(&s, cnt);
        if (perr) { per_stream_free(&s); return CMS_ERR; }
        for (uint32_t i = 0; i < cnt; i++) {
            cms_file_entry_t *e = (cms_file_entry_t*)pdu->file_entry->elements[i];
            if (!e) { per_stream_free(&s); return CMS_ERR; }
            err = cms_file_entry_encode_stream(&s, e);
            if (err) { per_stream_free(&s); return err; }
        }
    }

    /* 2. moreFollows — BOOLEAN DEFAULT TRUE */
    if (!pdu->more_follows) { per_stream_free(&s); return CMS_ERR; }
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_file_directory_response_decode(cms_get_file_directory_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err;

    /* 0. reqId */
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;

    /* 1. fileEntry */
    if (!pdu->file_entry) return CMS_ERR;
    {
        uint32_t cnt;
        per_error_t perr = per_decode_length(&s, &cnt);
        if (perr) return CMS_ERR;
        if (pdu->file_entry->count < (int32_t)cnt) {
            pdu->file_entry->count = (int32_t)cnt;
            return CMS_RETRY;
        }
        pdu->file_entry->count = (int32_t)cnt;
        for (uint32_t i = 0; i < cnt; i++) {
            cms_file_entry_t *e = (cms_file_entry_t*)pdu->file_entry->elements[i];
            if (!e) return CMS_ERR;
            err = cms_file_entry_decode_stream(&s, e);
            if (err) return err;
        }
    }

    /* 2. moreFollows */
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_get_file_directory_error_encode(const cms_get_file_directory_error_t *pdu, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err_i = per_stream_init_write(&s, 64);
    if (err_i) return (int)err_i;
    int err;

    /* 0. reqId — Int16U */
    if (!pdu->req_id) { per_stream_free(&s); return CMS_ERR; }
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) { per_stream_free(&s); return err; }

    /* 1. serviceError — ServiceError */
    if (!pdu->service_error) { per_stream_free(&s); return CMS_ERR; }
    err = cms_service_error_encode_stream(&s, pdu->service_error);
    if (err) { per_stream_free(&s); return err; }

    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_get_file_directory_error_decode(cms_get_file_directory_error_t *pdu, const uint8_t *in_buf, int in_len) {
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
