#include "svc/control/cms_time_activated_operate.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/control/cms_originator.h"
#include "data/control/cms_check.h"
#include "data/control/cms_add_cause.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int8u.h"

int cms_time_activated_operate_request_encode(const cms_time_activated_operate_request_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->reference)return CMS_ERR;err=cms_object_reference_encode_stream(&s,pdu->reference);if(err)return err;
    if(!pdu->ctl_val)return CMS_ERR;err=cms_data_encode_stream(&s,pdu->ctl_val);if(err)return err;
    if(!pdu->oper_tm)return CMS_ERR;err=cms_time_stamp_encode_stream(&s,pdu->oper_tm);if(err)return err;
    if(!pdu->origin)return CMS_ERR;err=cms_originator_encode_stream(&s,pdu->origin);if(err)return err;
    if(!pdu->ctl_num)return CMS_ERR;err=cms_int8u_encode_stream(&s,pdu->ctl_num);if(err)return err;
    if(!pdu->t)return CMS_ERR;err=cms_time_stamp_encode_stream(&s,pdu->t);if(err)return err;
    if(!pdu->test)return CMS_ERR;err=cms_boolean_encode_stream(&s,pdu->test);if(err)return err;
    if(!pdu->check)return CMS_ERR;err=cms_check_encode_stream(&s,pdu->check);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_time_activated_operate_request_decode(cms_time_activated_operate_request_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->reference)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->reference);if(err)return err;
    if(!pdu->ctl_val)return CMS_ERR;err=cms_data_decode_stream(&s,pdu->ctl_val);if(err)return err;
    if(!pdu->oper_tm)return CMS_ERR;err=cms_time_stamp_decode_stream(&s,pdu->oper_tm);if(err)return err;
    if(!pdu->origin)return CMS_ERR;err=cms_originator_decode_stream(&s,pdu->origin);if(err)return err;
    if(!pdu->ctl_num)return CMS_ERR;err=cms_int8u_decode_stream(&s,pdu->ctl_num);if(err)return err;
    if(!pdu->t)return CMS_ERR;err=cms_time_stamp_decode_stream(&s,pdu->t);if(err)return err;
    if(!pdu->test)return CMS_ERR;err=cms_boolean_decode_stream(&s,pdu->test);if(err)return err;
    if(!pdu->check)return CMS_ERR;err=cms_check_decode_stream(&s,pdu->check);if(err)return err;
    return CMS_OK;
}
int cms_time_activated_operate_response_encode(const cms_time_activated_operate_response_t*pdu,uint8_t*out_buf,int*out_len){return cms_time_activated_operate_request_encode(pdu,out_buf,out_len);}
int cms_time_activated_operate_response_decode(cms_time_activated_operate_response_t*pdu,const uint8_t*in_buf,int in_len){return cms_time_activated_operate_request_decode(pdu,in_buf,in_len);}
int cms_time_activated_operate_error_encode(const cms_time_activated_operate_error_t*pdu,uint8_t*out_buf,int*out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->reference)return CMS_ERR;err=cms_object_reference_encode_stream(&s,pdu->reference);if(err)return err;
    if(!pdu->ctl_val)return CMS_ERR;err=cms_data_encode_stream(&s,pdu->ctl_val);if(err)return err;
    if(!pdu->oper_tm)return CMS_ERR;err=cms_time_stamp_encode_stream(&s,pdu->oper_tm);if(err)return err;
    if(!pdu->origin)return CMS_ERR;err=cms_originator_encode_stream(&s,pdu->origin);if(err)return err;
    if(!pdu->ctl_num)return CMS_ERR;err=cms_int8u_encode_stream(&s,pdu->ctl_num);if(err)return err;
    if(!pdu->t)return CMS_ERR;err=cms_time_stamp_encode_stream(&s,pdu->t);if(err)return err;
    if(!pdu->test)return CMS_ERR;err=cms_boolean_encode_stream(&s,pdu->test);if(err)return err;
    if(!pdu->check)return CMS_ERR;err=cms_check_encode_stream(&s,pdu->check);if(err)return err;
    if(!pdu->add_cause)return CMS_ERR;err=cms_add_cause_encode_stream(&s,pdu->add_cause);if(err)return err;
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_time_activated_operate_error_decode(cms_time_activated_operate_error_t*pdu,const uint8_t*in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->reference)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->reference);if(err)return err;
    if(!pdu->ctl_val)return CMS_ERR;err=cms_data_decode_stream(&s,pdu->ctl_val);if(err)return err;
    if(!pdu->oper_tm)return CMS_ERR;err=cms_time_stamp_decode_stream(&s,pdu->oper_tm);if(err)return err;
    if(!pdu->origin)return CMS_ERR;err=cms_originator_decode_stream(&s,pdu->origin);if(err)return err;
    if(!pdu->ctl_num)return CMS_ERR;err=cms_int8u_decode_stream(&s,pdu->ctl_num);if(err)return err;
    if(!pdu->t)return CMS_ERR;err=cms_time_stamp_decode_stream(&s,pdu->t);if(err)return err;
    if(!pdu->test)return CMS_ERR;err=cms_boolean_decode_stream(&s,pdu->test);if(err)return err;
    if(!pdu->check)return CMS_ERR;err=cms_check_decode_stream(&s,pdu->check);if(err)return err;
    if(!pdu->add_cause)return CMS_ERR;err=cms_add_cause_decode_stream(&s,pdu->add_cause);if(err)return err;
    return CMS_OK;
}
