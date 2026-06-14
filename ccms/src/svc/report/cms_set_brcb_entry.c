#include "svc/report/cms_set_brcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/common/cms_entry_id.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/scalar/cms_int16.h"
#include "data/string/cms_visible_string.h"

int cms_set_brcb_entry_encode_stream(per_stream_t *s, const cms_set_brcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR; int err;
    err = cms_object_reference_encode_stream(s, v->reference); if (err) return err;

    { int p = (v->rpt_id_present && v->rpt_id_present->value) && v->rpt_id; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_visible_string_encode_stream(s,v->rpt_id,129);if(err)return err;} }
    { int p = (v->rpt_ena_present && v->rpt_ena_present->value) && v->rpt_ena; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(s,v->rpt_ena);if(err)return err;} }
    { int p = (v->dat_set_present && v->dat_set_present->value) && v->dat_set; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_object_reference_encode_stream(s,v->dat_set);if(err)return err;} }
    { int p = (v->opt_flds_present && v->opt_flds_present->value) && v->opt_flds; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_rcb_opt_flds_encode_stream(s,v->opt_flds);if(err)return err;} }
    { int p = (v->buf_tm_present && v->buf_tm_present->value) && v->buf_tm; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_int32u_encode_stream(s,v->buf_tm);if(err)return err;} }
    { int p = (v->trg_ops_present && v->trg_ops_present->value) && v->trg_ops; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_trigger_conditions_encode_stream(s,v->trg_ops);if(err)return err;} }
    { int p = (v->intg_pd_present && v->intg_pd_present->value) && v->intg_pd; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_int32u_encode_stream(s,v->intg_pd);if(err)return err;} }
    { int p = (v->gi_present && v->gi_present->value) && v->gi; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(s,v->gi);if(err)return err;} }
    { int p = (v->purge_buf_present && v->purge_buf_present->value) && v->purge_buf; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_boolean_encode_stream(s,v->purge_buf);if(err)return err;} }
    { int p = (v->entry_id_present && v->entry_id_present->value) && v->entry_id; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_entry_id_encode_stream(s,v->entry_id);if(err)return err;} }
    { int p = (v->resv_tms_present && v->resv_tms_present->value) && v->resv_tms; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(&s,&b); if(err)return err; if(p){err=cms_int16_encode_stream(s,v->resv_tms);if(err)return err;} }

    return CMS_OK;
}
int cms_set_brcb_entry_decode_stream(per_stream_t *s, cms_set_brcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR; int err;
    err = cms_object_reference_decode_stream(s, v->reference); if (err) return err;

    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->rpt_id_present)v->rpt_id_present->value=b.value; if(b.value&&v->rpt_id){err=cms_visible_string_decode_stream(s,v->rpt_id,129);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->rpt_ena_present)v->rpt_ena_present->value=b.value; if(b.value&&v->rpt_ena){err=cms_boolean_decode_stream(s,v->rpt_ena);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->dat_set_present)v->dat_set_present->value=b.value; if(b.value&&v->dat_set){err=cms_object_reference_decode_stream(s,v->dat_set);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->opt_flds_present)v->opt_flds_present->value=b.value; if(b.value&&v->opt_flds){err=cms_rcb_opt_flds_decode_stream(s,v->opt_flds);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->buf_tm_present)v->buf_tm_present->value=b.value; if(b.value&&v->buf_tm){err=cms_int32u_decode_stream(s,v->buf_tm);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->trg_ops_present)v->trg_ops_present->value=b.value; if(b.value&&v->trg_ops){err=cms_trigger_conditions_decode_stream(s,v->trg_ops);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->intg_pd_present)v->intg_pd_present->value=b.value; if(b.value&&v->intg_pd){err=cms_int32u_decode_stream(s,v->intg_pd);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->gi_present)v->gi_present->value=b.value; if(b.value&&v->gi){err=cms_boolean_decode_stream(s,v->gi);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->purge_buf_present)v->purge_buf_present->value=b.value; if(b.value&&v->purge_buf){err=cms_boolean_decode_stream(s,v->purge_buf);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->entry_id_present)v->entry_id_present->value=b.value; if(b.value&&v->entry_id){err=cms_entry_id_decode_stream(s,v->entry_id);if(err)return err;} }
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(&s,&b); if(err)return err; if(v->resv_tms_present)v->resv_tms_present->value=b.value; if(b.value&&v->resv_tms){err=cms_int16_decode_stream(s,v->resv_tms);if(err)return err;} }

    return CMS_OK;
}
