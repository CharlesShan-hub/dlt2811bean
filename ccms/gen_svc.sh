#!/bin/bash
# Generate all svc .c files with the standard pattern

SRC="/Users/charles/workspace/project/dlt2811bean/ccms/src/svc"

# === directory ===
cat > "$SRC/directory/cms_get_logical_device_directory.c" << 'CEOF'
#include "svc/directory/cms_get_logical_device_directory.h"
#include "svc/other/cms_req_id.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_sub_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"

int cms_get_logical_device_directory_request_encode(const cms_get_logical_device_directory_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    { int p = (pdu->ld_name_present && pdu->ld_name_present->value) && pdu->ld_name; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_object_name_encode_stream(&s,pdu->ld_name);if(err)return err;} }
    { int p = (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(&s,pdu->ref_after);if(err)return err;} }
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_logical_device_directory_request_decode(cms_get_logical_device_directory_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->ld_name_present)pdu->ld_name_present->value=b.value; if(b.value){if(!pdu->ld_name)return CMS_ERR;err=cms_object_name_decode_stream(&s,pdu->ld_name);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->ref_after_present)pdu->ref_after_present->value=b.value; if(b.value){if(!pdu->ref_after)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->ref_after);if(err)return err;} }
    return CMS_OK;
}
int cms_get_logical_device_directory_response_encode(const cms_get_logical_device_directory_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->ln_reference) return CMS_ERR; { uint32_t cnt=(uint32_t)pdu->ln_reference->count; per_error_t perr=per_encode_length(&s,cnt); if(perr)return CMS_ERR; for(uint32_t i=0;i<cnt;i++){cms_sub_reference_t*e=(cms_sub_reference_t*)pdu->ln_reference->elements[i];if(!e)return CMS_ERR;err=cms_sub_reference_encode_stream(&s,e);if(err)return err;} }
    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_encode_stream(&s, pdu->more_follows); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_logical_device_directory_response_decode(cms_get_logical_device_directory_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->ln_reference) return CMS_ERR; { uint32_t cnt; per_error_t perr=per_decode_length(&s,&cnt); if(perr)return CMS_ERR; pdu->ln_reference->count=(int32_t)cnt; for(uint32_t i=0;i<cnt;i++){cms_sub_reference_t*e=(cms_sub_reference_t*)pdu->ln_reference->elements[i];if(!e)return CMS_ERR;err=cms_sub_reference_decode_stream(&s,e);if(err)return err;} }
    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_decode_stream(&s, pdu->more_follows); if (err) return err;
    return CMS_OK;
}
int cms_get_logical_device_directory_error_encode(const cms_get_logical_device_directory_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_encode_stream(&s, pdu->service_error); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_logical_device_directory_error_decode(cms_get_logical_device_directory_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_decode_stream(&s, pdu->service_error); if (err) return err;
    return CMS_OK;
}
CEOF

cat > "$SRC/directory/cms_get_logical_node_directory.c" << 'CEOF'
#include "svc/directory/cms_get_logical_node_directory.h"
#include "svc/other/cms_req_id.h"
#include "svc/other/cms_reference_choice.h"
#include "svc/directory/cms_acsi_class.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_sub_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"

int cms_get_logical_node_directory_request_encode(const cms_get_logical_node_directory_request_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->reference) return CMS_ERR; err = cms_reference_choice_encode_stream(&s, pdu->reference); if (err) return err;
    if (!pdu->acsi_class) return CMS_ERR; err = cms_acsi_class_encode_stream(&s, pdu->acsi_class); if (err) return err;
    { int p = (pdu->ref_after_present && pdu->ref_after_present->value) && pdu->ref_after; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(&s,pdu->ref_after);if(err)return err;} }
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_logical_node_directory_request_decode(cms_get_logical_node_directory_request_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->reference) return CMS_ERR; err = cms_reference_choice_decode_stream(&s, pdu->reference); if (err) return err;
    if (!pdu->acsi_class) return CMS_ERR; err = cms_acsi_class_decode_stream(&s, pdu->acsi_class); if (err) return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(pdu->ref_after_present)pdu->ref_after_present->value=b.value; if(b.value){if(!pdu->ref_after)return CMS_ERR;err=cms_object_reference_decode_stream(&s,pdu->ref_after);if(err)return err;} }
    return CMS_OK;
}
int cms_get_logical_node_directory_response_encode(const cms_get_logical_node_directory_response_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->reference) return CMS_ERR; { uint32_t cnt=(uint32_t)pdu->reference->count; per_error_t perr=per_encode_length(&s,cnt); if(perr)return CMS_ERR; for(uint32_t i=0;i<cnt;i++){cms_sub_reference_t*e=(cms_sub_reference_t*)pdu->reference->elements[i];if(!e)return CMS_ERR;err=cms_sub_reference_encode_stream(&s,e);if(err)return err;} }
    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_encode_stream(&s, pdu->more_follows); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_logical_node_directory_response_decode(cms_get_logical_node_directory_response_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->reference) return CMS_ERR; { uint32_t cnt; per_error_t perr=per_decode_length(&s,&cnt); if(perr)return CMS_ERR; pdu->reference->count=(int32_t)cnt; for(uint32_t i=0;i<cnt;i++){cms_sub_reference_t*e=(cms_sub_reference_t*)pdu->reference->elements[i];if(!e)return CMS_ERR;err=cms_sub_reference_decode_stream(&s,e);if(err)return err;} }
    if (!pdu->more_follows) return CMS_ERR; err = cms_boolean_decode_stream(&s, pdu->more_follows); if (err) return err;
    return CMS_OK;
}
int cms_get_logical_node_directory_error_encode(const cms_get_logical_node_directory_error_t *pdu, uint8_t *out_buf, int *out_len) {
    per_stream_t s; per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_encode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_encode_stream(&s, pdu->service_error); if (err) return err;
    *out_len = (int)per_stream_bytes_written(&s); return CMS_OK;
}
int cms_get_logical_node_directory_error_decode(cms_get_logical_node_directory_error_t *pdu, const uint8_t *in_buf, int in_len) {
    per_stream_t s; per_stream_init_read(&s, in_buf, (size_t)in_len);
    int err; if (!pdu->req_id) return CMS_ERR; err = cms_req_id_decode_stream(&s, pdu->req_id); if (err) return err;
    if (!pdu->service_error) return CMS_ERR; err = cms_service_error_decode_stream(&s, pdu->service_error); if (err) return err;
    return CMS_OK;
}
CEOF

# === data module entry types ===
cat > "$SRC/data/cms_data_ref_entry.c" << 'CEOF'
#include "svc/data/cms_data_ref_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"

int cms_data_ref_entry_encode_stream(per_stream_t *s, const cms_data_ref_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR; int err;
    err = cms_object_reference_encode_stream(s, v->reference); if (err) return err;
    { int p = (v->fc_present && v->fc_present->value) && v->fc; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_functional_constraint_encode_stream(s,v->fc);if(err)return err;} }
    return CMS_OK;
}
int cms_data_ref_entry_decode_stream(per_stream_t *s, cms_data_ref_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR; int err;
    err = cms_object_reference_decode_stream(s, v->reference); if (err) return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->fc_present)v->fc_present->value=b.value; if(b.value){if(!v->fc)return CMS_ERR;err=cms_functional_constraint_decode_stream(s,v->fc);if(err)return err;} }
    return CMS_OK;
}
CEOF

cat > "$SRC/data/cms_sub_ref_entry.c" << 'CEOF'
#include "svc/data/cms_sub_ref_entry.h"
#include "data/common/cms_sub_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"

int cms_sub_ref_entry_encode_stream(per_stream_t *s, const cms_sub_ref_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR; int err;
    err = cms_sub_reference_encode_stream(s, v->reference); if (err) return err;
    { int p = (v->fc_present && v->fc_present->value) && v->fc; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_functional_constraint_encode_stream(s,v->fc);if(err)return err;} }
    return CMS_OK;
}
int cms_sub_ref_entry_decode_stream(per_stream_t *s, cms_sub_ref_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR; int err;
    err = cms_sub_reference_decode_stream(s, v->reference); if (err) return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->fc_present)v->fc_present->value=b.value; if(b.value){if(!v->fc)return CMS_ERR;err=cms_functional_constraint_decode_stream(s,v->fc);if(err)return err;} }
    return CMS_OK;
}
CEOF

cat > "$SRC/data/cms_data_ref_value_entry.c" << 'CEOF'
#include "svc/data/cms_data_ref_value_entry.h"
#include "svc/data/cms_data_ref_entry.h"
#include "data/choice/cms_data.h"

int cms_data_ref_value_entry_encode_stream(per_stream_t *s, const cms_data_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR; int err;
    err = cms_object_reference_encode_stream(s, v->reference); if (err) return err;
    { int p = (v->fc_present && v->fc_present->value) && v->fc; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_functional_constraint_encode_stream(s,v->fc);if(err)return err;} }
    err = cms_data_encode_stream(s, v->value); if (err) return err;
    return CMS_OK;
}
int cms_data_ref_value_entry_decode_stream(per_stream_t *s, cms_data_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR; int err;
    err = cms_object_reference_decode_stream(s, v->reference); if (err) return err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->fc_present)v->fc_present->value=b.value; if(b.value){if(!v->fc)return CMS_ERR;err=cms_functional_constraint_decode_stream(s,v->fc);if(err)return err;} }
    err = cms_data_decode_stream(s, v->value); if (err) return err;
    return CMS_OK;
}
CEOF

cat > "$SRC/data/cms_data_def_result_entry.c" << 'CEOF'
#include "svc/data/cms_data_def_result_entry.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "data/choice/cms_data_definition.h"

int cms_data_def_result_entry_encode_stream(per_stream_t *s, const cms_data_def_result_entry_t *v) {
    if (!v || !v->definition) return CMS_ERR; int err;
    { int p = (v->cdc_type_present && v->cdc_type_present->value) && v->cdc_type; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_visible_string_encode_stream(s,v->cdc_type,129);if(err)return err;} }
    err = cms_data_definition_encode_stream(s, v->definition); if (err) return err;
    return CMS_OK;
}
int cms_data_def_result_entry_decode_stream(per_stream_t *s, cms_data_def_result_entry_t *v) {
    if (!v || !v->definition) return CMS_ERR; int err;
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->cdc_type_present)v->cdc_type_present->value=b.value; if(b.value){if(!v->cdc_type)return CMS_ERR;err=cms_visible_string_decode_stream(s,v->cdc_type,129);if(err)return err;} }
    err = cms_data_definition_decode_stream(s, v->definition); if (err) return err;
    return CMS_OK;
}
CEOF

echo "Generated directory + data entry .c files"
