#include "svc/msv/cms_set_msvcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"
#include "per/cms_sequence.h"

int cms_set_msvcb_result_encode_stream(per_stream_t *s, const cms_set_msvcb_result_t *v) {
    if (!v) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (7 fields: error, svEnaErr, msvIdErr, datSetErr, smpModErr, smpRateErr, optFldsErr) */
    bool opt_present[7] = {
        (v->error_present && v->error_present->value) && v->error,
        (v->sv_ena_err_present && v->sv_ena_err_present->value) && v->sv_ena_err,
        (v->msv_id_err_present && v->msv_id_err_present->value) && v->msv_id_err,
        (v->dat_set_err_present && v->dat_set_err_present->value) && v->dat_set_err,
        (v->smp_mod_err_present && v->smp_mod_err_present->value) && v->smp_mod_err,
        (v->smp_rate_err_present && v->smp_rate_err_present->value) && v->smp_rate_err,
        (v->opt_flds_err_present && v->opt_flds_err_present->value) && v->opt_flds_err
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 7);
    if (err) return err;

    /* 1. error — ServiceError OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_service_error_encode_stream(s, v->error);
        if (err) return err;
    }

    /* 2. svEnaErr — ServiceError OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_service_error_encode_stream(s, v->sv_ena_err);
        if (err) return err;
    }

    /* 3. msvIdErr — ServiceError OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_service_error_encode_stream(s, v->msv_id_err);
        if (err) return err;
    }

    /* 4. datSetErr — ServiceError OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_service_error_encode_stream(s, v->dat_set_err);
        if (err) return err;
    }

    /* 5. smpModErr — ServiceError OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_service_error_encode_stream(s, v->smp_mod_err);
        if (err) return err;
    }

    /* 6. smpRateErr — ServiceError OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_service_error_encode_stream(s, v->smp_rate_err);
        if (err) return err;
    }

    /* 7. optFldsErr — ServiceError OPTIONAL (bitmap[6]) */
    if (opt_present[6]) {
        err = cms_service_error_encode_stream(s, v->opt_flds_err);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_set_msvcb_result_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_msvcb_result_t *v = (cms_set_msvcb_result_t*)ptr;
    int err;

    /* 0. OPTIONAL bitmap (7 fields) */
    bool opt_present[7];
    err = (int)per_decode_optional_bitmap(s, opt_present, 7);
    if (err) return err;
    if (v) {
        if (v->error_present)       v->error_present->value       = opt_present[0];
        if (v->sv_ena_err_present)  v->sv_ena_err_present->value  = opt_present[1];
        if (v->msv_id_err_present)  v->msv_id_err_present->value  = opt_present[2];
        if (v->dat_set_err_present) v->dat_set_err_present->value = opt_present[3];
        if (v->smp_mod_err_present) v->smp_mod_err_present->value = opt_present[4];
        if (v->smp_rate_err_present) v->smp_rate_err_present->value = opt_present[5];
        if (v->opt_flds_err_present) v->opt_flds_err_present->value = opt_present[6];
    }

    /* 1. error OPTIONAL */     if (opt_present[0]) { if (v && !v->error) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->error : NULL); if (err) return err; }
    /* 2. svEnaErr OPTIONAL */  if (opt_present[1]) { if (v && !v->sv_ena_err) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->sv_ena_err : NULL); if (err) return err; }
    /* 3. msvIdErr OPTIONAL */  if (opt_present[2]) { if (v && !v->msv_id_err) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->msv_id_err : NULL); if (err) return err; }
    /* 4. datSetErr OPTIONAL */ if (opt_present[3]) { if (v && !v->dat_set_err) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->dat_set_err : NULL); if (err) return err; }
    /* 5. smpModErr OPTIONAL */ if (opt_present[4]) { if (v && !v->smp_mod_err) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->smp_mod_err : NULL); if (err) return err; }
    /* 6. smpRateErr OPTIONAL */ if (opt_present[5]) { if (v && !v->smp_rate_err) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->smp_rate_err : NULL); if (err) return err; }
    /* 7. optFldsErr OPTIONAL */ if (opt_present[6]) { if (v && !v->opt_flds_err) return CMS_ERR; err = cms_service_error_decode_stream(s, v ? v->opt_flds_err : NULL); if (err) return err; }

    return CMS_OK;
}
