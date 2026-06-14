#include "svc/dataset/cms_delete_data_set.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"

int cms_delete_data_set_request_encode(const cms_delete_data_set_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->dataset_reference) return CMS_ERR; err = cms_object_reference_encode_stream(&s, pdu->dataset_reference); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_delete_data_set_request_decode(cms_delete_data_set_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->dataset_reference) return CMS_ERR; err = cms_object_reference_decode_stream(&s, pdu->dataset_reference); if (err) return err;
    return CMS_OK;
}
int cms_delete_data_set_response_encode(const cms_delete_data_set_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    if (!pdu->req_id) return CMS_ERR; int err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_delete_data_set_response_decode(cms_delete_data_set_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    if (!pdu->req_id) return CMS_ERR; return cms_req_id_decode_stream(&s, pdu->req_id);
}
int cms_delete_data_set_error_encode(const cms_delete_data_set_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_encode_stream(&s, pdu->service_error); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_delete_data_set_error_decode(cms_delete_data_set_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len); int err;
    if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_decode_stream(&s, pdu->service_error); if (err) return err;
    return CMS_OK;
}
