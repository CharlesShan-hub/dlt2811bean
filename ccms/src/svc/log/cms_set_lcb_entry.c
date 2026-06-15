#include "svc/log/cms_set_lcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_lcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"

int cms_set_lcb_entry_encode_stream(per_stream_t *s, const cms_set_lcb_entry_t *v) {
    if(!v||!v->reference) return CMS_ERR; int err;
    err=cms_object_reference_encode_stream(s,v->reference); if(err)return err;
    { int p=(v->log_ena_present&&v->log_ena_present->value)&&v->log_ena; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(s,v->log_ena);if(err)return err;} }
    { int p=(v->dat_set_present&&v->dat_set_present->value)&&v->dat_set; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(s,v->dat_set);if(err)return err;} }
    { int p=(v->trg_ops_present&&v->trg_ops_present->value)&&v->trg_ops; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_trigger_conditions_encode_stream(s,v->trg_ops);if(err)return err;} }
    { int p=(v->intg_pd_present&&v->intg_pd_present->value)&&v->intg_pd; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_int32u_encode_stream(s,v->intg_pd);if(err)return err;} }
    { int p=(v->log_ref_present&&v->log_ref_present->value)&&v->log_ref; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(s,v->log_ref);if(err)return err;} }
    { int p=(v->opt_flds_present&&v->opt_flds_present->value)&&v->opt_flds; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_lcb_opt_flds_encode_stream(s,v->opt_flds);if(err)return err;} }
    { int p=(v->buf_tm_present&&v->buf_tm_present->value)&&v->buf_tm; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_int32u_encode_stream(s,v->buf_tm);if(err)return err;} }
    return CMS_OK;
}
int cms_set_lcb_entry_decode_stream(per_stream_t *s, cms_set_lcb_entry_t *v) {
    if(!v||!v->reference) return CMS_ERR; int err;
    err=cms_object_reference_decode_stream(s,v->reference); if(err)return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->log_ena_present)v->log_ena_present->value=b.value; if(b.value&&v->log_ena){err=cms_boolean_decode_stream(s,v->log_ena);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->dat_set_present)v->dat_set_present->value=b.value; if(b.value&&v->dat_set){err=cms_object_reference_decode_stream(s,v->dat_set);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->trg_ops_present)v->trg_ops_present->value=b.value; if(b.value&&v->trg_ops){err=cms_trigger_conditions_decode_stream(s,v->trg_ops);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->intg_pd_present)v->intg_pd_present->value=b.value; if(b.value&&v->intg_pd){err=cms_int32u_decode_stream(s,v->intg_pd);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->log_ref_present)v->log_ref_present->value=b.value; if(b.value&&v->log_ref){err=cms_object_reference_decode_stream(s,v->log_ref);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->opt_flds_present)v->opt_flds_present->value=b.value; if(b.value&&v->opt_flds){err=cms_lcb_opt_flds_decode_stream(s,v->opt_flds);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->buf_tm_present)v->buf_tm_present->value=b.value; if(b.value&&v->buf_tm){err=cms_int32u_decode_stream(s,v->buf_tm);if(err)return err;} }
    return CMS_OK;
}
