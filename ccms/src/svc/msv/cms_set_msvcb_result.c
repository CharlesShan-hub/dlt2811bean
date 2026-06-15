#include "svc/msv/cms_set_msvcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"

int cms_set_msvcb_result_encode_stream(per_stream_t *s, const cms_set_msvcb_result_t *v) {
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

    /* 2. svEnaErr — ServiceError OPTIONAL */
    {
        int present = (v->sv_ena_err_present && v->sv_ena_err_present->value) && v->sv_ena_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->sv_ena_err);
            if (err) return err;
        }
    }

    /* 3. msvIdErr — ServiceError OPTIONAL */
    {
        int present = (v->msv_id_err_present && v->msv_id_err_present->value) && v->msv_id_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->msv_id_err);
            if (err) return err;
        }
    }

    /* 4. datSetErr — ServiceError OPTIONAL */
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

    /* 5. smpModErr — ServiceError OPTIONAL */
    {
        int present = (v->smp_mod_err_present && v->smp_mod_err_present->value) && v->smp_mod_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->smp_mod_err);
            if (err) return err;
        }
    }

    /* 6. smpRateErr — ServiceError OPTIONAL */
    {
        int present = (v->smp_rate_err_present && v->smp_rate_err_present->value) && v->smp_rate_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->smp_rate_err);
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

    return CMS_OK;
}

int cms_set_msvcb_result_decode_stream(per_stream_t *s, cms_set_msvcb_result_t *v) {
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

    /* 2. svEnaErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->sv_ena_err_present) v->sv_ena_err_present->value = bit.value;
        if (bit.value) {
            if (!v->sv_ena_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->sv_ena_err);
            if (err) return err;
        }
    }

    /* 3. msvIdErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->msv_id_err_present) v->msv_id_err_present->value = bit.value;
        if (bit.value) {
            if (!v->msv_id_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->msv_id_err);
            if (err) return err;
        }
    }

    /* 4. datSetErr OPTIONAL */
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

    /* 5. smpModErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->smp_mod_err_present) v->smp_mod_err_present->value = bit.value;
        if (bit.value) {
            if (!v->smp_mod_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->smp_mod_err);
            if (err) return err;
        }
    }

    /* 6. smpRateErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->smp_rate_err_present) v->smp_rate_err_present->value = bit.value;
        if (bit.value) {
            if (!v->smp_rate_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->smp_rate_err);
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

    return CMS_OK;
}
