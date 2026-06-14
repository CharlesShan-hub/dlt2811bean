#include "svc/file/cms_get_file_directory.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_file_entry.h"
#include "data/common/cms_service_error.h"
#include "data/common/cms_time_stamp.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

int cms_get_file_directory_request_encode(const cms_get_file_directory_request_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->path_name)return CMS_ERR;err=cms_visible_string_encode_stream(&s,pdu->path_name,255);if(err)return err;
    {int p=(pdu->start_time_present&&pdu->start_time_present->value)&&pdu->start_time;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_time_stamp_encode_stream(&s,pdu->start_time);if(err)return err;}}
    {int p=(pdu->stop_time_present&&pdu->stop_time_present->value)&&pdu->stop_time;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_time_stamp_encode_stream(&s,pdu->stop_time);if(err)return err;}}
    {int p=(pdu->file_after_present&&pdu->file_after_present->value)&&pdu->file_after;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_visible_string_encode_stream(&s,pdu->file_after,255);if(err)return err;}}
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_file_directory_request_decode(cms_get_file_directory_request_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->path_name)return CMS_ERR;err=cms_visible_string_decode_stream(&s,pdu->path_name,255);if(err)return err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->start_time_present)pdu->start_time_present->value=b.value;if(b.value&&pdu->start_time){err=cms_time_stamp_decode_stream(&s,pdu->start_time);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->stop_time_present)pdu->stop_time_present->value=b.value;if(b.value&&pdu->stop_time){err=cms_time_stamp_decode_stream(&s,pdu->stop_time);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->file_after_present)pdu->file_after_present->value=b.value;if(b.value&&pdu->file_after){err=cms_visible_string_decode_stream(&s,pdu->file_after,255);if(err)return err;}}
    return CMS_OK;
}
int cms_get_file_directory_response_encode(const cms_get_file_directory_response_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->file_entry)return CMS_ERR;{uint32_t cnt=(uint32_t)pdu->file_entry->count;per_error_t perr=per_encode_length(&s,cnt);if(perr)return CMS_ERR;for(uint32_t i=0;i<cnt;i++){cms_file_entry_t*e=(cms_file_entry_t*)pdu->file_entry->elements[i];if(!e)return CMS_ERR;err=cms_file_entry_encode_stream(&s,e);if(err)return err;}}
    if(!pdu->more_follows)return CMS_ERR;err=cms_boolean_encode_stream(&s,pdu->more_follows);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_file_directory_response_decode(cms_get_file_directory_response_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->file_entry)return CMS_ERR;{uint32_t cnt;per_error_t perr=per_decode_length(&s,&cnt);if(perr)return CMS_ERR;pdu->file_entry->count=(int32_t)cnt;for(uint32_t i=0;i<cnt;i++){cms_file_entry_t*e=(cms_file_entry_t*)pdu->file_entry->elements[i];if(!e)return CMS_ERR;err=cms_file_entry_decode_stream(&s,e);if(err)return err;}}
    if(!pdu->more_follows)return CMS_ERR;err=cms_boolean_decode_stream(&s,pdu->more_follows);if(err)return err;
    return CMS_OK;
}
int cms_get_file_directory_error_encode(const cms_get_file_directory_error_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_encode_stream(&s,pdu->service_error);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_get_file_directory_error_decode(cms_get_file_directory_error_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->service_error)return CMS_ERR;err=cms_service_error_decode_stream(&s,pdu->service_error);if(err)return err;
    return CMS_OK;
}
