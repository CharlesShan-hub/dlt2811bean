#include "svc/report/cms_set_brcb_values.h"
#include "svc/other/cms_req_id.h"
#include "svc/report/cms_set_brcb_entry.h"
#include "svc/report/cms_set_brcb_result.h"
#include "per/cms_integer.h"

/* ── Request ── */

int cms_set_brcb_values_request_encode(const cms_set_brcb_values_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;

    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;

    if (!pdu->brcb) return CMS_ERR;
    { uint32_t cnt=(uint32_t)pdu->brcb->count; per_error_t perr=per_encode_length(&s,cnt); if(perr)return CMS_ERR; for(uint32_t i=0;i<cnt;i++){cms_set_brcb_entry_t*e=(cms_set_brcb_entry_t*)pdu->brcb->elements[i];if(!e)return CMS_ERR;err=cms_set_brcb_entry_encode_stream(&s,e);if(err)return err;} }

    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_set_brcb_values_request_decode(cms_set_brcb_values_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;

    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;

    if (!pdu->brcb) return CMS_ERR;
    { uint32_t cnt; per_error_t perr=per_decode_length(&s,&cnt); if(perr)return CMS_ERR; pdu->brcb->count=(int32_t)cnt; for(uint32_t i=0;i<cnt;i++){cms_set_brcb_entry_t*e=(cms_set_brcb_entry_t*)pdu->brcb->elements[i];if(!e)return CMS_ERR;err=cms_set_brcb_entry_decode_stream(&s,e);if(err)return err;} }

    return CMS_OK;
}
/* ── Response ── */
int cms_set_brcb_values_response_encode(const cms_set_brcb_values_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    if (!pdu->req_id) return CMS_ERR; int err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_set_brcb_values_response_decode(cms_set_brcb_values_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    if (!pdu->req_id) return CMS_ERR; return cms_req_id_decode_stream(&s, pdu->req_id);
}
/* ── Error ── */
int cms_set_brcb_values_error_encode(const cms_set_brcb_values_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->result) return CMS_ERR;
    { uint32_t cnt=(uint32_t)pdu->result->count; per_error_t perr=per_encode_length(&s,cnt); if(perr)return CMS_ERR; for(uint32_t i=0;i<cnt;i++){cms_set_brcb_result_t*e=(cms_set_brcb_result_t*)pdu->result->elements[i];if(!e)return CMS_ERR;err=cms_set_brcb_result_encode_stream(&s,e);if(err)return err;} }
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_set_brcb_values_error_decode(cms_set_brcb_values_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->result) return CMS_ERR;
    { uint32_t cnt; per_error_t perr=per_decode_length(&s,&cnt); if(perr)return CMS_ERR; pdu->result->count=(int32_t)cnt; for(uint32_t i=0;i<cnt;i++){cms_set_brcb_result_t*e=(cms_set_brcb_result_t*)pdu->result->elements[i];if(!e)return CMS_ERR;err=cms_set_brcb_result_decode_stream(&s,e);if(err)return err;} }
    return CMS_OK;
}
