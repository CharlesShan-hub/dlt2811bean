#include "svc/log/cms_set_lcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"

int cms_set_lcb_result_encode_stream(per_stream_t *s, const cms_set_lcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 1. error — ServiceError OPTIONAL */
    {
        int present = (v->error_present && v->error_present->value) && v->error;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->error);
            if (err) return err;
        }
    }

    /* 2. logEnaErr — ServiceError OPTIONAL */
    {
        int present = (v->log_ena_err_present && v->log_ena_err_present->value) && v->log_ena_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->log_ena_err);
            if (err) return err;
        }
    }

    /* 3. datSetErr — ServiceError OPTIONAL */
    {
        int present = (v->dat_set_err_present && v->dat_set_err_present->value) && v->dat_set_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->dat_set_err);
            if (err) return err;
        }
    }

    /* 4. trgOpsErr — ServiceError OPTIONAL */
    {
        int present = (v->trg_ops_err_present && v->trg_ops_err_present->value) && v->trg_ops_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->trg_ops_err);
            if (err) return err;
        }
    }

    /* 5. intgPdErr — ServiceError OPTIONAL */
    {
        int present = (v->intg_pd_err_present && v->intg_pd_err_present->value) && v->intg_pd_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->intg_pd_err);
            if (err) return err;
        }
    }

    /* 6. logRefErr — ServiceError OPTIONAL */
    {
        int present = (v->log_ref_err_present && v->log_ref_err_present->value) && v->log_ref_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->log_ref_err);
            if (err) return err;
        }
    }

    /* 7. optFldsErr — ServiceError OPTIONAL */
    {
        int present = (v->opt_flds_err_present && v->opt_flds_err_present->value) && v->opt_flds_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->opt_flds_err);
            if (err) return err;
        }
    }

    /* 8. bufTmErr — ServiceError OPTIONAL */
    {
        int present = (v->buf_tm_err_present && v->buf_tm_err_present->value) && v->buf_tm_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->buf_tm_err);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_set_lcb_result_decode_stream(per_stream_t *s, cms_set_lcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 1. error OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->error_present) v->error_present->value = bit.value;
        if (bit.value) {
            if (!v->error) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->error);
            if (err) return err;
        }
    }

    /* 2. logEnaErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->log_ena_err_present) v->log_ena_err_present->value = bit.value;
        if (bit.value) {
            if (!v->log_ena_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->log_ena_err);
            if (err) return err;
        }
    }

    /* 3. datSetErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->dat_set_err_present) v->dat_set_err_present->value = bit.value;
        if (bit.value) {
            if (!v->dat_set_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->dat_set_err);
            if (err) return err;
        }
    }

    /* 4. trgOpsErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->trg_ops_err_present) v->trg_ops_err_present->value = bit.value;
        if (bit.value) {
            if (!v->trg_ops_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->trg_ops_err);
            if (err) return err;
        }
    }

    /* 5. intgPdErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->intg_pd_err_present) v->intg_pd_err_present->value = bit.value;
        if (bit.value) {
            if (!v->intg_pd_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->intg_pd_err);
            if (err) return err;
        }
    }

    /* 6. logRefErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->log_ref_err_present) v->log_ref_err_present->value = bit.value;
        if (bit.value) {
            if (!v->log_ref_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->log_ref_err);
            if (err) return err;
        }
    }

    /* 7. optFldsErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->opt_flds_err_present) v->opt_flds_err_present->value = bit.value;
        if (bit.value) {
            if (!v->opt_flds_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->opt_flds_err);
            if (err) return err;
        }
    }

    /* 8. bufTmErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->buf_tm_err_present) v->buf_tm_err_present->value = bit.value;
        if (bit.value) {
            if (!v->buf_tm_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->buf_tm_err);
            if (err) return err;
        }
    }

    return CMS_OK;
}
