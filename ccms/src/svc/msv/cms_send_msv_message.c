#include "svc/msv/cms_send_msv_message.h"
#include "svc/other/cms_req_id.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_time_stamp.h"
#include "data/block/cms_smp_mod.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int8u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_integer.h"

int cms_send_msv_message_encode(const cms_send_msv_message_t *pdu,uint8_t *out_buf,int *out_len){
    per_stream_t s;per_stream_init_write(&s,out_buf,(size_t)*out_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_encode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->msv_id)return CMS_ERR;err=cms_visible_string_encode_stream(&s,pdu->msv_id,129);if(err)return err;
    {int p=(pdu->dat_set_present&&pdu->dat_set_present->value)&&pdu->dat_set;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_object_reference_encode_stream(&s,pdu->dat_set);if(err)return err;}}
    if(!pdu->smp_cnt)return CMS_ERR;err=cms_int16u_encode_stream(&s,pdu->smp_cnt);if(err)return err;
    if(!pdu->conf_rev)return CMS_ERR;err=cms_int32u_encode_stream(&s,pdu->conf_rev);if(err)return err;
    {int p=(pdu->ref_tm_present&&pdu->ref_tm_present->value)&&pdu->ref_tm;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_time_stamp_encode_stream(&s,pdu->ref_tm);if(err)return err;}}
    if(!pdu->smp_synch)return CMS_ERR;err=cms_int8u_encode_stream(&s,pdu->smp_synch);if(err)return err;
    {int p=(pdu->smp_rate_present&&pdu->smp_rate_present->value)&&pdu->smp_rate;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_int16u_encode_stream(&s,pdu->smp_rate);if(err)return err;}}
    if(!pdu->simulation)return CMS_ERR;err=cms_boolean_encode_stream(&s,pdu->simulation);if(err)return err;
    if(!pdu->sample)return CMS_ERR;{uint32_t cnt=(uint32_t)pdu->sample->count;per_error_t perr=per_encode_length(&s,cnt);if(perr)return CMS_ERR;for(uint32_t i=0;i<cnt;i++){cms_data_t*e=(cms_data_t*)pdu->sample->elements[i];if(!e)return CMS_ERR;err=cms_data_encode_stream(&s,e);if(err)return err;}}
    {int p=(pdu->smp_mod_present&&pdu->smp_mod_present->value)&&pdu->smp_mod;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_smp_mod_encode_stream(&s,pdu->smp_mod);if(err)return err;}}
    *out_len=(int)per_stream_bytes_written(&s);return CMS_OK;
}
int cms_send_msv_message_decode(cms_send_msv_message_t *pdu,const uint8_t *in_buf,int in_len){
    per_stream_t s;per_stream_init_read(&s,in_buf,(size_t)in_len);int err;
    if(!pdu->req_id)return CMS_ERR;err=cms_req_id_decode_stream(&s,pdu->req_id);if(err)return err;
    if(!pdu->msv_id)return CMS_ERR;err=cms_visible_string_decode_stream(&s,pdu->msv_id,129);if(err)return err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->dat_set_present)pdu->dat_set_present->value=b.value;if(b.value&&pdu->dat_set){err=cms_object_reference_decode_stream(&s,pdu->dat_set);if(err)return err;}}
    if(!pdu->smp_cnt)return CMS_ERR;err=cms_int16u_decode_stream(&s,pdu->smp_cnt);if(err)return err;
    if(!pdu->conf_rev)return CMS_ERR;err=cms_int32u_decode_stream(&s,pdu->conf_rev);if(err)return err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->ref_tm_present)pdu->ref_tm_present->value=b.value;if(b.value&&pdu->ref_tm){err=cms_time_stamp_decode_stream(&s,pdu->ref_tm);if(err)return err;}}
    if(!pdu->smp_synch)return CMS_ERR;err=cms_int8u_decode_stream(&s,pdu->smp_synch);if(err)return err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->smp_rate_present)pdu->smp_rate_present->value=b.value;if(b.value&&pdu->smp_rate){err=cms_int16u_decode_stream(&s,pdu->smp_rate);if(err)return err;}}
    if(!pdu->simulation)return CMS_ERR;err=cms_boolean_decode_stream(&s,pdu->simulation);if(err)return err;
    if(!pdu->sample)return CMS_ERR;{uint32_t cnt;per_error_t perr=per_decode_length(&s,&cnt);if(perr)return CMS_ERR;pdu->sample->count=(int32_t)cnt;for(uint32_t i=0;i<cnt;i++){cms_data_t*e=(cms_data_t*)pdu->sample->elements[i];if(!e)return CMS_ERR;err=cms_data_decode_stream(&s,e);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(pdu->smp_mod_present)pdu->smp_mod_present->value=b.value;if(b.value&&pdu->smp_mod){err=cms_smp_mod_decode_stream(&s,pdu->smp_mod);if(err)return err;}}
    return CMS_OK;
}
