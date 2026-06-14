#include "svc/goose/cms_get_go_reference.h"
#include "svc/other/cms_req_id.h"
#include "svc/goose/cms_go_ref_fc_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "per/cms_integer.h"

int cms_get_go_reference_request_encode(const cms_get_go_reference_request_t *pdu,uint8_t *out_buf,int *out_len) {
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->gocb_reference)return CMS_ERR;err=cms_object_reference_encode_stream(&s,pdu->gocb_reference);if(err)return err;
    if(!pdu->member_ofs)return CMS_ERR;{uint32_t cnt=(uint32_t)pdu->member_ofs->count;per_error_t perr=per_encode_length(&s,cnt);if(perr)return CMS_ERR;for(uint32_t i=0;i<cnt;i++){cms_int16u_t*e=(cms_int16u_t*)pdu->member_ofs->elements[i];if(!e)return CMS_ERR;err=cms_int16u_encode_stream(&s,e);if(err)return err;}}
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_go_reference_request_decode(cms_get_go_reference_request_t *pdu,const uint8_t *in_buf,int in_len) {
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->gocb_reference)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->gocb_reference);if(err)return err;
    if(!pdu->member_ofs)return CMS_ERR;{uint32_t cnt;per_error_t perr=per_decode_length(&s,&cnt);if(perr)return CMS_ERR;pdu->member_ofs->count=(int32_t)cnt;for(uint32_t i=0;i<cnt;i++){cms_int16u_t*e=(cms_int16u_t*)pdu->member_ofs->elements[i];if(!e)return CMS_ERR;err=cms_int16u_decode_stream(&s,e);if(err)return err;}}
    return CMS_OK;
}
int cms_get_go_reference_response_encode(const cms_get_go_reference_response_t *pdu,uint8_t *out_buf,int *out_len) {
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->gocb_reference)return CMS_ERR;err=cms_object_reference_encode_stream(&s,pdu->gocb_reference);if(err)return err;
    if(!pdu->conf_rev)return CMS_ERR;err=cms_int32u_encode_stream(&s,pdu->conf_rev);if(err)return err;
    if(!pdu->dat_set)return CMS_ERR;err=cms_object_reference_encode_stream(&s,pdu->dat_set);if(err)return err;
    if(!pdu->member_data)return CMS_ERR;{uint32_t cnt=(uint32_t)pdu->member_data->count;per_error_t perr=per_encode_length(&s,cnt);if(perr)return CMS_ERR;for(uint32_t i=0;i<cnt;i++){cms_go_ref_fc_entry_t*e=(cms_go_ref_fc_entry_t*)pdu->member_data->elements[i];if(!e)return CMS_ERR;err=cms_go_ref_fc_entry_encode_stream(&s,e);if(err)return err;}}
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_go_reference_response_decode(cms_get_go_reference_response_t *pdu,const uint8_t *in_buf,int in_len) {
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->gocb_reference)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->gocb_reference);if(err)return err;
    if(!pdu->conf_rev)return CMS_ERR;err=cms_int32u_decode_stream(&s,pdu->conf_rev);if(err)return err;
    if(!pdu->dat_set)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->dat_set);if(err)return err;
    if(!pdu->member_data)return CMS_ERR;{uint32_t cnt;per_error_t perr=per_decode_length(&s,&cnt);if(perr)return CMS_ERR;pdu->member_data->count=(int32_t)cnt;for(uint32_t i=0;i<cnt;i++){cms_go_ref_fc_entry_t*e=(cms_go_ref_fc_entry_t*)pdu->member_data->elements[i];if(!e)return CMS_ERR;err=cms_go_ref_fc_entry_decode_stream(&s,e);if(err)return err;}}
    return CMS_OK;
}
int cms_get_go_reference_error_encode(const cms_get_go_reference_error_t *pdu,uint8_t *out_buf,int *out_len) {
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_encode_stream(&s,pdu->service_error);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_go_reference_error_decode(cms_get_go_reference_error_t *pdu,const uint8_t *in_buf,int in_len) {
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_decode_stream(&s,pdu->service_error);if(err)return err;
    return CMS_OK;
}
