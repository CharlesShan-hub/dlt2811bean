#include "svc/report/cms_report_entry.h"
#include "svc/report/cms_report_data_entry.h"
#include "data/common/cms_entry_id.h"
#include "data/common/cms_entry_time.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_integer.h"

int cms_report_entry_encode_stream(per_stream_t *s, const cms_report_entry_t *v) {
    if (!v || !v->entry_data) return CMS_ERR; int err;

    /* timeOfEntry — EntryTime OPTIONAL */
    { int p = (v->time_of_entry_present && v->time_of_entry_present->value) && v->time_of_entry; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_entry_time_encode_stream(s,v->time_of_entry);if(err)return err;} }

    /* entryID — EntryID OPTIONAL */
    { int p = (v->entry_id_present && v->entry_id_present->value) && v->entry_id; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_entry_id_encode_stream(s,v->entry_id);if(err)return err;} }

    /* entryData — SEQUENCE OF ReportDataEntry */
    { uint32_t cnt=(uint32_t)v->entry_data->count; per_error_t perr=per_encode_length(&s,cnt); if(perr)return CMS_ERR; for(uint32_t i=0;i<cnt;i++){cms_report_data_entry_t*e=(cms_report_data_entry_t*)v->entry_data->elements[i];if(!e)return CMS_ERR;err=cms_report_data_entry_encode_stream(&s,e);if(err)return err;} }

    return CMS_OK;
}

int cms_report_entry_decode_stream(per_stream_t *s, cms_report_entry_t *v) {
    if (!v || !v->entry_data) return CMS_ERR; int err;

    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->time_of_entry_present)v->time_of_entry_present->value=b.value; if(b.value&&v->time_of_entry){err=cms_entry_time_decode_stream(s,v->time_of_entry);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->entry_id_present)v->entry_id_present->value=b.value; if(b.value&&v->entry_id){err=cms_entry_id_decode_stream(s,v->entry_id);if(err)return err;} }

    { uint32_t cnt; per_error_t perr=per_decode_length(&s,&cnt); if(perr)return CMS_ERR; v->entry_data->count=(int32_t)cnt; for(uint32_t i=0;i<cnt;i++){cms_report_data_entry_t*e=(cms_report_data_entry_t*)v->entry_data->elements[i];if(!e)return CMS_ERR;err=cms_report_data_entry_decode_stream(&s,e);if(err)return err;} }

    return CMS_OK;
}
