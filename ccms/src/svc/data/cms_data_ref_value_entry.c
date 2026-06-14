#include "svc/data/cms_data_ref_value_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/choice/cms_data.h"
#include "data/scalar/cms_boolean.h"

int cms_data_ref_value_entry_encode_stream(per_stream_t*s,const cms_data_ref_value_entry_t*v){
    if(!v||!v->reference||!v->value)return CMS_ERR;int err;
    err=cms_object_reference_encode_stream(s,v->reference);if(err)return err;
    {int p=(v->fc_present&&v->fc_present->value)&&v->fc;cms_boolean_t b={.value=p};err=cms_boolean_encode_stream(&s,&b);if(err)return err;if(p){err=cms_functional_constraint_encode_stream(s,v->fc);if(err)return err;}}
    err=cms_data_encode_stream(s,v->value);if(err)return err;
    return CMS_OK;
}
int cms_data_ref_value_entry_decode_stream(per_stream_t*s,cms_data_ref_value_entry_t*v){
    if(!v||!v->reference||!v->value)return CMS_ERR;int err;
    err=cms_object_reference_decode_stream(s,v->reference);if(err)return err;
    {cms_boolean_t b={0};err=cms_boolean_decode_stream(&s,&b);if(err)return err;if(v->fc_present)v->fc_present->value=b.value;if(b.value&&v->fc){err=cms_functional_constraint_decode_stream(s,v->fc);if(err)return err;}}
    err=cms_data_decode_stream(s,v->value);if(err)return err;
    return CMS_OK;
}
