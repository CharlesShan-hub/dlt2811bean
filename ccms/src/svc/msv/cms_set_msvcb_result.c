#include "svc/msv/cms_set_msvcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"

int cms_set_msvcb_result_encode_stream(per_stream_t*s,const cms_set_msvcb_result_t*v){
    if(!v)return CMS_ERR;int err;
    {int p=(v->error_present&&v->error_present->value)&&v->error;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->error);if(err)return err;}}
    {int p=(v->sv_ena_err_present&&v->sv_ena_err_present->value)&&v->sv_ena_err;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->sv_ena_err);if(err)return err;}}
    {int p=(v->msv_id_err_present&&v->msv_id_err_present->value)&&v->msv_id_err;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->msv_id_err);if(err)return err;}}
    {int p=(v->dat_set_err_present&&v->dat_set_err_present->value)&&v->dat_set_err;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->dat_set_err);if(err)return err;}}
    {int p=(v->smp_mod_err_present&&v->smp_mod_err_present->value)&&v->smp_mod_err;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->smp_mod_err);if(err)return err;}}
    {int p=(v->smp_rate_err_present&&v->smp_rate_err_present->value)&&v->smp_rate_err;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->smp_rate_err);if(err)return err;}}
    {int p=(v->opt_flds_err_present&&v->opt_flds_err_present->value)&&v->opt_flds_err;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_service_error_encode_stream(s,v->opt_flds_err);if(err)return err;}}
    return CMS_OK;
}
int cms_set_msvcb_result_decode_stream(per_stream_t*s,cms_set_msvcb_result_t*v){
    if(!v)return CMS_ERR;int err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->error_present)v->error_present->value=b.value;if(b.value&&v->error){err=cms_service_error_decode_stream(s,v->error);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->sv_ena_err_present)v->sv_ena_err_present->value=b.value;if(b.value&&v->sv_ena_err){err=cms_service_error_decode_stream(s,v->sv_ena_err);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->msv_id_err_present)v->msv_id_err_present->value=b.value;if(b.value&&v->msv_id_err){err=cms_service_error_decode_stream(s,v->msv_id_err);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->dat_set_err_present)v->dat_set_err_present->value=b.value;if(b.value&&v->dat_set_err){err=cms_service_error_decode_stream(s,v->dat_set_err);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->smp_mod_err_present)v->smp_mod_err_present->value=b.value;if(b.value&&v->smp_mod_err){err=cms_service_error_decode_stream(s,v->smp_mod_err);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->smp_rate_err_present)v->smp_rate_err_present->value=b.value;if(b.value&&v->smp_rate_err){err=cms_service_error_decode_stream(s,v->smp_rate_err);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->opt_flds_err_present)v->opt_flds_err_present->value=b.value;if(b.value&&v->opt_flds_err){err=cms_service_error_decode_stream(s,v->opt_flds_err);if(err)return err;}}
    return CMS_OK;
}
