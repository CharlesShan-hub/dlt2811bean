#include "svc/log/cms_set_lcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"
#include "per/cms_sequence.h"

int cms_set_lcb_result_encode_stream(per_stream_t *s, const cms_set_lcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (8 fields: error, logEnaErr, datSetErr, trgOpsErr, intgPdErr, logRefErr, optFldsErr, bufTmErr) */
    bool opt_present[8] = {
        (v->error_present && v->error_present->value) && v->error,
        (v->log_ena_err_present && v->log_ena_err_present->value) && v->log_ena_err,
        (v->dat_set_err_present && v->dat_set_err_present->value) && v->dat_set_err,
        (v->trg_ops_err_present && v->trg_ops_err_present->value) && v->trg_ops_err,
        (v->intg_pd_err_present && v->intg_pd_err_present->value) && v->intg_pd_err,
        (v->log_ref_err_present && v->log_ref_err_present->value) && v->log_ref_err,
        (v->opt_flds_err_present && v->opt_flds_err_present->value) && v->opt_flds_err,
        (v->buf_tm_err_present && v->buf_tm_err_present->value) && v->buf_tm_err
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 8);
    if (err) return err;

    /* 1. error — ServiceError OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_service_error_encode_stream(s, v->error);
        if (err) return err;
    }

    /* 2. logEnaErr — ServiceError OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_service_error_encode_stream(s, v->log_ena_err);
        if (err) return err;
    }

    /* 3. datSetErr — ServiceError OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_service_error_encode_stream(s, v->dat_set_err);
        if (err) return err;
    }

    /* 4. trgOpsErr — ServiceError OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_service_error_encode_stream(s, v->trg_ops_err);
        if (err) return err;
    }

    /* 5. intgPdErr — ServiceError OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_service_error_encode_stream(s, v->intg_pd_err);
        if (err) return err;
    }

    /* 6. logRefErr — ServiceError OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_service_error_encode_stream(s, v->log_ref_err);
        if (err) return err;
    }

    /* 7. optFldsErr — ServiceError OPTIONAL (bitmap[6]) */
    if (opt_present[6]) {
        err = cms_service_error_encode_stream(s, v->opt_flds_err);
        if (err) return err;
    }

    /* 8. bufTmErr — ServiceError OPTIONAL (bitmap[7]) */
    if (opt_present[7]) {
        err = cms_service_error_encode_stream(s, v->buf_tm_err);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_set_lcb_result_decode_stream(per_stream_t *s, cms_set_lcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (8 fields) */
    bool opt_present[8];
    err = (int)per_decode_optional_bitmap(s, opt_present, 8);
    if (err) return err;
    if (v->error_present) v->error_present->value = opt_present[0];
    if (v->log_ena_err_present) v->log_ena_err_present->value = opt_present[1];
    if (v->dat_set_err_present) v->dat_set_err_present->value = opt_present[2];
    if (v->trg_ops_err_present) v->trg_ops_err_present->value = opt_present[3];
    if (v->intg_pd_err_present) v->intg_pd_err_present->value = opt_present[4];
    if (v->log_ref_err_present) v->log_ref_err_present->value = opt_present[5];
    if (v->opt_flds_err_present) v->opt_flds_err_present->value = opt_present[6];
    if (v->buf_tm_err_present) v->buf_tm_err_present->value = opt_present[7];

    /* 1. error OPTIONAL */
    if (opt_present[0]) {
        if (!v->error) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->error);
        if (err) return err;
    }

    /* 2. logEnaErr OPTIONAL */
    if (opt_present[1]) {
        if (!v->log_ena_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->log_ena_err);
        if (err) return err;
    }

    /* 3. datSetErr OPTIONAL */
    if (opt_present[2]) {
        if (!v->dat_set_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->dat_set_err);
        if (err) return err;
    }

    /* 4. trgOpsErr OPTIONAL */
    if (opt_present[3]) {
        if (!v->trg_ops_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->trg_ops_err);
        if (err) return err;
    }

    /* 5. intgPdErr OPTIONAL */
    if (opt_present[4]) {
        if (!v->intg_pd_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->intg_pd_err);
        if (err) return err;
    }

    /* 6. logRefErr OPTIONAL */
    if (opt_present[5]) {
        if (!v->log_ref_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->log_ref_err);
        if (err) return err;
    }

    /* 7. optFldsErr OPTIONAL */
    if (opt_present[6]) {
        if (!v->opt_flds_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->opt_flds_err);
        if (err) return err;
    }

    /* 8. bufTmErr OPTIONAL */
    if (opt_present[7]) {
        if (!v->buf_tm_err) return CMS_ERR;
        err = cms_service_error_decode_stream(s, v->buf_tm_err);
        if (err) return err;
    }

    return CMS_OK;
}
