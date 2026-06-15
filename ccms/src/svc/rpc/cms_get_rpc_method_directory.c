#include "svc/rpc/cms_get_rpc_method_directory.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_get_rpc_method_directory_request_encode(const cms_get_rpc_method_directory_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;

    /* 1. reqId — Int16U */
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;

    /* 2. interface — VisibleString OPTIONAL */
    {
        int present = (pdu->interface_present && pdu->interface_present->value) && pdu->interface_name;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(&s, &bit);
        if (err) return err;
        if (present) {
            err = cms_visible_string_encode_stream(&s, pdu->interface_name, UINT32_MAX);
            if (err) return err;
        }
    }

    /* 3. referenceAfter — VisibleString OPTIONAL */
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

    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}

int cms_get_rpc_method_directory_request_decode(cms_get_rpc_method_directory_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;

    /* 1. reqId */
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;

    /* 2. interface OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->interface_present)pdu->interface_present->value=b.value; if(b.value&&pdu->interface_name){err=cms_visible_string_decode_stream(&s,pdu->interface_name,UINT32_MAX);if(err)return err;} }

    /* 3. referenceAfter OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->ref_after_present)pdu->ref_after_present->value=b.value; if(b.value&&pdu->ref_after){err=cms_visible_string_decode_stream(&s,pdu->ref_after,UINT32_MAX);if(err)return err;} }

    return CMS_OK;
}

/* ── Response ── */

int cms_get_rpc_method_directory_response_encode(const cms_get_rpc_method_directory_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;

    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;

    if (!pdu->reference) return CMS_ERR;
    { uint32_t cnt=(uint32_t)pdu->reference->count; per_error_t perr=per_encode_length(&s,cnt); if(perr)return CMS_ERR; for(uint32_t i=0;i<cnt;i++){cms_uint8_array_t*e=(cms_uint8_array_t*)pdu->reference->elements[i];if(!e)return CMS_ERR;err=cms_visible_string_encode_stream(&s,e,UINT32_MAX);if(err)return err;} }

    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_encode_stream(&s, pdu->more_follows); if (err) return err;

    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}

int cms_get_rpc_method_directory_response_decode(cms_get_rpc_method_directory_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;

    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;

    if (!pdu->reference) return CMS_ERR;
    { uint32_t cnt; per_error_t perr=per_decode_length(&s,&cnt); if(perr)return CMS_ERR; pdu->reference->count=(int32_t)cnt; for(uint32_t i=0;i<cnt;i++){cms_uint8_array_t*e=(cms_uint8_array_t*)pdu->reference->elements[i];if(!e)return CMS_ERR;err=cms_visible_string_decode_stream(&s,e,UINT32_MAX);if(err)return err;} }

    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_decode_stream(&s, pdu->more_follows); if (err) return err;

    return CMS_OK;
}

/* ── Error ── */

int cms_get_rpc_method_directory_error_encode(const cms_get_rpc_method_directory_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_encode_stream(&s, pdu->service_error); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}

int cms_get_rpc_method_directory_error_decode(cms_get_rpc_method_directory_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_decode_stream(&s, pdu->service_error); if (err) return err;
    return CMS_OK;
}
