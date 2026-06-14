#include "svc/goose/cms_get_go_cb_values.h"
#include "svc/other/cms_req_id.h"
#include "svc/goose/cms_gocb_value_choice.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"

int cms_get_go_cb_values_request_encode(const cms_get_go_cb_values_request_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->reference)return CMS_ERR;{uint32_t cnt=(uint32_t)pdu->reference->count;per_error_t perr=per_encode_length(&s,cnt);if(perr)return CMS_ERR;for(uint32_t i=0;i<cnt;i++){cms_object_reference_t*e=(cms_object_reference_t*)pdu->reference->elements[i];if(!e)return CMS_ERR;err=cms_object_reference_encode_stream(&s,e);if(err)return err;}}
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_go_cb_values_request_decode(cms_get_go_cb_values_request_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->reference)return CMS_ERR;{uint32_t cnt;per_error_t perr=per_decode_length(&s,&cnt);if(perr)return CMS_ERR;pdu->reference->count=(int32_t)cnt;for(uint32_t i=0;i<cnt;i++){cms_object_reference_t*e=(cms_object_reference_t*)pdu->reference->elements[i];if(!e)return CMS_ERR;err=cms_object_reference_decode_stream(&s,e);if(err)return err;}}
    return CMS_OK;
}
int cms_get_go_cb_values_response_encode(const cms_get_go_cb_values_response_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->gocb)return CMS_ERR;{uint32_t cnt=(uint32_t)pdu->gocb->count;per_error_t perr=per_encode_length(&s,cnt);if(perr)return CMS_ERR;for(uint32_t i=0;i<cnt;i++){cms_gocb_value_choice_t*e=(cms_gocb_value_choice_t*)pdu->gocb->elements[i];if(!e)return CMS_ERR;err=cms_gocb_value_choice_encode_stream(&s,e);if(err)return err;}}
    if(!pdu->more_follows)return CMS_ERR;err=cms_boolean_encode_stream(&s,pdu->more_follows);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_go_cb_values_response_decode(cms_get_go_cb_values_response_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->gocb)return CMS_ERR;{uint32_t cnt;per_error_t perr=per_decode_length(&s,&cnt);if(perr)return CMS_ERR;pdu->gocb->count=(int32_t)cnt;for(uint32_t i=0;i<cnt;i++){cms_gocb_value_choice_t*e=(cms_gocb_value_choice_t*)pdu->gocb->elements[i];if(!e)return CMS_ERR;err=cms_gocb_value_choice_decode_stream(&s,e);if(err)return err;}}
    if(!pdu->more_follows)return CMS_ERR;err=cms_boolean_decode_stream(&s,pdu->more_follows);if(err)return err;
    return CMS_OK;
}
int cms_get_go_cb_values_error_encode(const cms_get_go_cb_values_error_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_encode_stream(&s,pdu->service_error);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_go_cb_values_error_decode(cms_get_go_cb_values_error_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_decode_stream(&s,pdu->service_error);if(err)return err;
    return CMS_OK;
}
