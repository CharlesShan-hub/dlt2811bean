#include "svc/directory/cms_get_server_directory.h"
#include "svc/directory/cms_object_class.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"

static int req_encode(per_stream_t *s, const cms_get_server_directory_request_t *pdu) {
    int err;
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;
    if (!pdu->object_class) return CMS_ERR;
    err = cms_object_class_encode_stream(&s, pdu->object_class);
    if (err) return err;
    {
        int present = (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(&s, &bit);
        if (err) return err;
        if (present) {
            err = cms_object_reference_encode_stream(&s, pdu->ref_after);
            if (err) return err;
        }
    }
    return CMS_OK;
}
static int req_decode(per_stream_t *s, cms_get_server_directory_request_t *pdu) {
    int err;
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;
    if (!pdu->object_class) return CMS_ERR;
    err = cms_object_class_decode_stream(&s, pdu->object_class);
    if (err) return err;
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(&s, &bit);
        if (err) return err;
        if (pdu->ref_after_present) pdu->ref_after_present->value = bit.value;
        if (bit.value) {
            if (!pdu->ref_after) return CMS_ERR;
            err = cms_object_reference_decode_stream(&s, pdu->ref_after);
            if (err) return err;
        }
    }
    return CMS_OK;
}
int cms_get_server_directory_request_encode(const cms_get_server_directory_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = req_encode(&s, pdu);
    if (rc) return rc; *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_server_directory_request_decode(cms_get_server_directory_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    return req_decode(&s, pdu);
}

static int resp_encode(per_stream_t *s, const cms_get_server_directory_response_t *pdu) {
    int err;
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_encode_stream(&s, pdu->req_id);
    if (err) return err;
    if (!pdu->reference) return CMS_ERR;
    {
        uint32_t count = (uint32_t)pdu->reference->count;
        per_error_t perr = per_encode_length(s, count);
        if (perr) return CMS_ERR;
        for (uint32_t i = 0; i < count; i++) {
            cms_object_reference_t *elem = (cms_object_reference_t*)pdu->reference->elements[i];
            if (!elem) return CMS_ERR;
            err = cms_object_reference_encode_stream(&s, elem);
            if (err) return err;
        }
    }
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_encode_stream(&s, pdu->more_follows);
    if (err) return err;
    return CMS_OK;
}
static int resp_decode(per_stream_t *s, cms_get_server_directory_response_t *pdu) {
    int err;
    if (!pdu->req_id) return CMS_ERR;
    err = cms_req_id_decode_stream(&s, pdu->req_id);
    if (err) return err;
    if (!pdu->reference) return CMS_ERR;
    {
        uint32_t count;
        per_error_t perr = per_decode_length(s, &count);
        if (perr) return CMS_ERR;
        pdu->reference->count = (int32_t)count;
        for (uint32_t i = 0; i < count; i++) {
            cms_object_reference_t *elem = (cms_object_reference_t*)pdu->reference->elements[i];
            if (!elem) return CMS_ERR;
            err = cms_object_reference_decode_stream(&s, elem);
            if (err) return err;
        }
    }
    if (!pdu->more_follows) return CMS_ERR;
    err = cms_boolean_decode_stream(&s, pdu->more_follows);
    if (err) return err;
    return CMS_OK;
}
int cms_get_server_directory_response_encode(const cms_get_server_directory_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = resp_encode(&s, pdu);
    if (rc) return rc; *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_server_directory_response_decode(cms_get_server_directory_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    return resp_decode(&s, pdu);
}

int cms_get_server_directory_error_encode(const cms_get_server_directory_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_encode_stream(&s, pdu->service_error); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_server_directory_error_decode(cms_get_server_directory_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_decode_stream(&s, pdu->service_error); if (err) return err;
    return CMS_OK;
}
