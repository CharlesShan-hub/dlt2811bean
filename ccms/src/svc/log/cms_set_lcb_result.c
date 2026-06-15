#include "svc/log/cms_set_lcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"

int cms_set_lcb_result_encode_stream(per_stream_t *s, const cms_set_lcb_result_t *v) {
    if(!v)return CMS_ERR; int err;
    { int p=(v->error_present&&v->error_present->value)&&v->error; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->error);if(err)return err;} }
    { int p=(v->log_ena_err_present&&v->log_ena_err_present->value)&&v->log_ena_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->log_ena_err);if(err)return err;} }
    { int p=(v->dat_set_err_present&&v->dat_set_err_present->value)&&v->dat_set_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->dat_set_err);if(err)return err;} }
    { int p=(v->trg_ops_err_present&&v->trg_ops_err_present->value)&&v->trg_ops_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->trg_ops_err);if(err)return err;} }
    { int p=(v->intg_pd_err_present&&v->intg_pd_err_present->value)&&v->intg_pd_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->intg_pd_err);if(err)return err;} }
    { int p=(v->log_ref_err_present&&v->log_ref_err_present->value)&&v->log_ref_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->log_ref_err);if(err)return err;} }
    { int p=(v->opt_flds_err_present&&v->opt_flds_err_present->value)&&v->opt_flds_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->opt_flds_err);if(err)return err;} }
    { int p=(v->buf_tm_err_present&&v->buf_tm_err_present->value)&&v->buf_tm_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->buf_tm_err);if(err)return err;} }
    return CMS_OK;
}
int cms_set_lcb_result_decode_stream(per_stream_t *s, cms_set_lcb_result_t *v) {
    if(!v)return CMS_ERR; int err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->error_present)v->error_present->value=b.value; if(b.value&&v->error){err=cms_service_error_decode_stream(s,v->error);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->log_ena_err_present)v->log_ena_err_present->value=b.value; if(b.value&&v->log_ena_err){err=cms_service_error_decode_stream(s,v->log_ena_err);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->dat_set_err_present)v->dat_set_err_present->value=b.value; if(b.value&&v->dat_set_err){err=cms_service_error_decode_stream(s,v->dat_set_err);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->trg_ops_err_present)v->trg_ops_err_present->value=b.value; if(b.value&&v->trg_ops_err){err=cms_service_error_decode_stream(s,v->trg_ops_err);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->intg_pd_err_present)v->intg_pd_err_present->value=b.value; if(b.value&&v->intg_pd_err){err=cms_service_error_decode_stream(s,v->intg_pd_err);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->log_ref_err_present)v->log_ref_err_present->value=b.value; if(b.value&&v->log_ref_err){err=cms_service_error_decode_stream(s,v->log_ref_err);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->opt_flds_err_present)v->opt_flds_err_present->value=b.value; if(b.value&&v->opt_flds_err){err=cms_service_error_decode_stream(s,v->opt_flds_err);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->buf_tm_err_present)v->buf_tm_err_present->value=b.value; if(b.value&&v->buf_tm_err){err=cms_service_error_decode_stream(s,v->buf_tm_err);if(err)return err;} }
    return CMS_OK;
}
