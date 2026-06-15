#include "svc/msv/cms_set_msvcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_smp_mod.h"
#include "data/block/cms_msvcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/string/cms_visible_string.h"

int cms_set_msvcb_entry_encode_stream(per_stream_t*s,const cms_set_msvcb_entry_t*v){
    if(!v||!v->reference)return CMS_ERR;int err;
    err=cms_object_reference_encode_stream(s,v->reference);if(err)return err;
    {int p=(v->sv_ena_present&&v->sv_ena_present->value)&&v->sv_ena;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(s,&b);if(err)return err;if(p){err=cms_boolean_encode_stream(s,v->sv_ena);if(err)return err;}}
    {int p=(v->msv_id_present&&v->msv_id_present->value)&&v->msv_id;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(s,&b);if(err)return err;if(p){err=cms_visible_string_encode_stream(s,v->msv_id,129);if(err)return err;}}
    {int p=(v->dat_set_present&&v->dat_set_present->value)&&v->dat_set;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(s,&b);if(err)return err;if(p){err=cms_object_reference_encode_stream(s,v->dat_set);if(err)return err;}}
    {int p=(v->smp_mod_present&&v->smp_mod_present->value)&&v->smp_mod;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(s,&b);if(err)return err;if(p){err=cms_smp_mod_encode_stream(s,v->smp_mod);if(err)return err;}}
    {int p=(v->smp_rate_present&&v->smp_rate_present->value)&&v->smp_rate;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(s,&b);if(err)return err;if(p){err=cms_int16u_encode_stream(s,v->smp_rate);if(err)return err;}}
    {int p=(v->opt_flds_present&&v->opt_flds_present->value)&&v->opt_flds;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(s,&b);if(err)return err;if(p){err=cms_msvcb_opt_flds_encode_stream(s,v->opt_flds);if(err)return err;}}
    return CMS_OK;
}
int cms_set_msvcb_entry_decode_stream(per_stream_t*s,cms_set_msvcb_entry_t*v){
    if(!v||!v->reference)return CMS_ERR;int err;
    err=cms_object_reference_decode_stream(s,v->reference);if(err)return err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(s,&b);if(err)return err;if(v->sv_ena_present)v->sv_ena_present->value=b.value;if(b.value&&v->sv_ena){err=cms_boolean_decode_stream(s,v->sv_ena);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(s,&b);if(err)return err;if(v->msv_id_present)v->msv_id_present->value=b.value;if(b.value&&v->msv_id){err=cms_visible_string_decode_stream(s,v->msv_id,129);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(s,&b);if(err)return err;if(v->dat_set_present)v->dat_set_present->value=b.value;if(b.value&&v->dat_set){err=cms_object_reference_decode_stream(s,v->dat_set);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(s,&b);if(err)return err;if(v->smp_mod_present)v->smp_mod_present->value=b.value;if(b.value&&v->smp_mod){err=cms_smp_mod_decode_stream(s,v->smp_mod);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(s,&b);if(err)return err;if(v->smp_rate_present)v->smp_rate_present->value=b.value;if(b.value&&v->smp_rate){err=cms_int16u_decode_stream(s,v->smp_rate);if(err)return err;}}
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(s,&b);if(err)return err;if(v->opt_flds_present)v->opt_flds_present->value=b.value;if(b.value&&v->opt_flds){err=cms_msvcb_opt_flds_decode_stream(s,v->opt_flds);if(err)return err;}}
    return CMS_OK;
}
