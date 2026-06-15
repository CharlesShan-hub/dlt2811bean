#include "svc/report/cms_set_brcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"

int cms_set_brcb_result_encode_stream(per_stream_t *s, const cms_set_brcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 1. error — ServiceError OPTIONAL */
    { int p = (v->error_present && v->error_present->value) && v->error; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->error);if(err)return err;} }

    /* 2. rptIdErr — ServiceError OPTIONAL */
    { int p = (v->rpt_id_err_present && v->rpt_id_err_present->value) && v->rpt_id_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->rpt_id_err);if(err)return err;} }

    /* 3. rptEnaErr — ServiceError OPTIONAL */
    { int p = (v->rpt_ena_err_present && v->rpt_ena_err_present->value) && v->rpt_ena_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->rpt_ena_err);if(err)return err;} }

    /* 4. datSetErr — ServiceError OPTIONAL */
    { int p = (v->dat_set_err_present && v->dat_set_err_present->value) && v->dat_set_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->dat_set_err);if(err)return err;} }

    /* 5. optFldsErr — ServiceError OPTIONAL */
    { int p = (v->opt_flds_err_present && v->opt_flds_err_present->value) && v->opt_flds_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->opt_flds_err);if(err)return err;} }

    /* 6. bufTmErr — ServiceError OPTIONAL */
    { int p = (v->buf_tm_err_present && v->buf_tm_err_present->value) && v->buf_tm_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->buf_tm_err);if(err)return err;} }

    /* 7. trgOpsErr — ServiceError OPTIONAL */
    { int p = (v->trg_ops_err_present && v->trg_ops_err_present->value) && v->trg_ops_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->trg_ops_err);if(err)return err;} }

    /* 8. intgPdErr — ServiceError OPTIONAL */
    { int p = (v->intg_pd_err_present && v->intg_pd_err_present->value) && v->intg_pd_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->intg_pd_err);if(err)return err;} }

    /* 9. giErr — ServiceError OPTIONAL */
    { int p = (v->gi_err_present && v->gi_err_present->value) && v->gi_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->gi_err);if(err)return err;} }

    /* 10. purgeBufErr — ServiceError OPTIONAL */
    { int p = (v->purge_buf_err_present && v->purge_buf_err_present->value) && v->purge_buf_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->purge_buf_err);if(err)return err;} }

    /* 11. entryIdErr — ServiceError OPTIONAL */
    { int p = (v->entry_id_err_present && v->entry_id_err_present->value) && v->entry_id_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->entry_id_err);if(err)return err;} }

    /* 12. resvTmsErr — ServiceError OPTIONAL */
    { int p = (v->resv_tms_err_present && v->resv_tms_err_present->value) && v->resv_tms_err; cms_boolean_t b={.value=p}; err=cms_boolean_encode_stream(s,&b); if(err)return err; if(p){err=cms_service_error_encode_stream(s,v->resv_tms_err);if(err)return err;} }

    return CMS_OK;
}

int cms_set_brcb_result_decode_stream(per_stream_t *s, cms_set_brcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 1. error OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->error_present)v->error_present->value=b.value; if(b.value&&v->error){err=cms_service_error_decode_stream(s,v->error);if(err)return err;} }

    /* 2. rptIdErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->rpt_id_err_present)v->rpt_id_err_present->value=b.value; if(b.value&&v->rpt_id_err){err=cms_service_error_decode_stream(s,v->rpt_id_err);if(err)return err;} }

    /* 3. rptEnaErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->rpt_ena_err_present)v->rpt_ena_err_present->value=b.value; if(b.value&&v->rpt_ena_err){err=cms_service_error_decode_stream(s,v->rpt_ena_err);if(err)return err;} }

    /* 4. datSetErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->dat_set_err_present)v->dat_set_err_present->value=b.value; if(b.value&&v->dat_set_err){err=cms_service_error_decode_stream(s,v->dat_set_err);if(err)return err;} }

    /* 5. optFldsErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->opt_flds_err_present)v->opt_flds_err_present->value=b.value; if(b.value&&v->opt_flds_err){err=cms_service_error_decode_stream(s,v->opt_flds_err);if(err)return err;} }

    /* 6. bufTmErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->buf_tm_err_present)v->buf_tm_err_present->value=b.value; if(b.value&&v->buf_tm_err){err=cms_service_error_decode_stream(s,v->buf_tm_err);if(err)return err;} }

    /* 7. trgOpsErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->trg_ops_err_present)v->trg_ops_err_present->value=b.value; if(b.value&&v->trg_ops_err){err=cms_service_error_decode_stream(s,v->trg_ops_err);if(err)return err;} }

    /* 8. intgPdErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->intg_pd_err_present)v->intg_pd_err_present->value=b.value; if(b.value&&v->intg_pd_err){err=cms_service_error_decode_stream(s,v->intg_pd_err);if(err)return err;} }

    /* 9. giErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->gi_err_present)v->gi_err_present->value=b.value; if(b.value&&v->gi_err){err=cms_service_error_decode_stream(s,v->gi_err);if(err)return err;} }

    /* 10. purgeBufErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->purge_buf_err_present)v->purge_buf_err_present->value=b.value; if(b.value&&v->purge_buf_err){err=cms_service_error_decode_stream(s,v->purge_buf_err);if(err)return err;} }

    /* 11. entryIdErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->entry_id_err_present)v->entry_id_err_present->value=b.value; if(b.value&&v->entry_id_err){err=cms_service_error_decode_stream(s,v->entry_id_err);if(err)return err;} }

    /* 12. resvTmsErr OPTIONAL */
    { cms_boolean_t b={0}; err=cms_boolean_decode_stream(s,&b); if(err)return err; if(v->resv_tms_err_present)v->resv_tms_err_present->value=b.value; if(b.value&&v->resv_tms_err){err=cms_service_error_decode_stream(s,v->resv_tms_err);if(err)return err;} }

    return CMS_OK;
}
