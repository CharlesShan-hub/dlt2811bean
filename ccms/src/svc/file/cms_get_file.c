#include "svc/file/cms_get_file.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"
#include "data/string/cms_octet_string.h"

int cms_get_file_request_encode(const cms_get_file_request_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->filename)return CMS_ERR;err=cms_visible_string_encode_stream(&s,pdu->filename,255);if(err)return err;
    if(!pdu->start_position)return CMS_ERR;err=cms_int32u_encode_stream(&s,pdu->start_position);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_file_request_decode(cms_get_file_request_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->filename)return CMS_ERR;err=cms_visible_string_decode_stream(&s,pdu->filename,255);if(err)return err;
    if(!pdu->start_position)return CMS_ERR;err=cms_int32u_decode_stream(&s,pdu->start_position);if(err)return err;
    return CMS_OK;
}
int cms_get_file_response_encode(const cms_get_file_response_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->file_data)return CMS_ERR;err=cms_octet_string_encode_stream(&s,pdu->file_data,UINT32_MAX);if(err)return err;
    if(!pdu->end_of_file)return CMS_ERR;err=cms_boolean_encode_stream(&s,pdu->end_of_file);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_file_response_decode(cms_get_file_response_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->file_data)return CMS_ERR;err=cms_octet_string_decode_stream(&s,pdu->file_data,UINT32_MAX);if(err)return err;
    if(!pdu->end_of_file)return CMS_ERR;err=cms_boolean_decode_stream(&s,pdu->end_of_file);if(err)return err;
    return CMS_OK;
}
int cms_get_file_error_encode(const cms_get_file_error_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_encode_stream(&s,pdu->service_error);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_file_error_decode(cms_get_file_error_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_decode_stream(&s,pdu->service_error);if(err)return err;
    return CMS_OK;
}
