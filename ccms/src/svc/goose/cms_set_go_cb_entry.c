#include "svc/goose/cms_set_go_cb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"

int cms_set_go_cb_entry_encode_stream(per_stream_t *s, const cms_set_go_cb_entry_t *v) {
    if (!v||!v->reference) return CMS_ERR; int err;
    err = cms_object_reference_encode_stream(s, v->reference); if (err) return err;
    { int p = (v->go_ena_present&&v->go_ena_present->value)&&v->go_ena; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(s,v->go_ena);if(err)return err;} }
    { int p = (v->go_id_present&&v->go_id_present->value)&&v->go_id; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_visible_string_encode_stream(s,v->go_id,129);if(err)return err;} }
    { int p = (v->dat_set_present&&v->dat_set_present->value)&&v->dat_set; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(s,v->dat_set);if(err)return err;} }
    return CMS_OK;
}
int cms_set_go_cb_entry_decode_stream(per_stream_t *s, cms_set_go_cb_entry_t *v) {
    if (!v||!v->reference) return CMS_ERR; int err;
    err = cms_object_reference_decode_stream(s, v->reference); if (err) return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->go_ena_present)v->go_ena_present->value=b.value; if(b.value&&v->go_ena){err=cms_boolean_decode_stream(s,v->go_ena);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->go_id_present)v->go_id_present->value=b.value; if(b.value&&v->go_id){err=cms_visible_string_decode_stream(s,v->go_id,129);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->dat_set_present)v->dat_set_present->value=b.value; if(b.value&&v->dat_set){err=cms_object_reference_decode_stream(s,v->dat_set);if(err)return err;} }
    return CMS_OK;
}
