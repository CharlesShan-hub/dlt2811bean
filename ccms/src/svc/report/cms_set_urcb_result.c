#include "svc/report/cms_set_urcb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"
#include "per/cms_sequence.h"

int cms_set_urcb_result_encode_stream(per_stream_t *s, const cms_set_urcb_result_t *v) {
    if (!v)
        return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (10 fields: error, rptID, rptEna, datSet, optFlds, bufTm, trgOps, intgPd, gi, resv) */
    bool opt_present[10] = {(v->error_present && v->error_present->value) && v->error,
                            (v->rpt_id_err_present && v->rpt_id_err_present->value) && v->rpt_id_err,
                            (v->rpt_ena_err_present && v->rpt_ena_err_present->value) && v->rpt_ena_err,
                            (v->dat_set_err_present && v->dat_set_err_present->value) && v->dat_set_err,
                            (v->opt_flds_err_present && v->opt_flds_err_present->value) && v->opt_flds_err,
                            (v->buf_tm_err_present && v->buf_tm_err_present->value) && v->buf_tm_err,
                            (v->trg_ops_err_present && v->trg_ops_err_present->value) && v->trg_ops_err,
                            (v->intg_pd_err_present && v->intg_pd_err_present->value) && v->intg_pd_err,
                            (v->gi_err_present && v->gi_err_present->value) && v->gi_err,
                            (v->resv_err_present && v->resv_err_present->value) && v->resv_err};
    err = (int) per_encode_optional_bitmap(s, opt_present, 10);
    if (err)
        return err;

    /* 1. error — ServiceError OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_service_error_encode_stream(s, v->error);
        if (err)
            return err;
    }

    /* 2. rptIdErr — ServiceError OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_service_error_encode_stream(s, v->rpt_id_err);
        if (err)
            return err;
    }

    /* 3. rptEnaErr — ServiceError OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_service_error_encode_stream(s, v->rpt_ena_err);
        if (err)
            return err;
    }

    /* 4. datSetErr — ServiceError OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_service_error_encode_stream(s, v->dat_set_err);
        if (err)
            return err;
    }

    /* 5. optFldsErr — ServiceError OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_service_error_encode_stream(s, v->opt_flds_err);
        if (err)
            return err;
    }

    /* 6. bufTmErr — ServiceError OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_service_error_encode_stream(s, v->buf_tm_err);
        if (err)
            return err;
    }

    /* 7. trgOpsErr — ServiceError OPTIONAL (bitmap[6]) */
    if (opt_present[6]) {
        err = cms_service_error_encode_stream(s, v->trg_ops_err);
        if (err)
            return err;
    }

    /* 8. intgPdErr — ServiceError OPTIONAL (bitmap[7]) */
    if (opt_present[7]) {
        err = cms_service_error_encode_stream(s, v->intg_pd_err);
        if (err)
            return err;
    }

    /* 9. giErr — ServiceError OPTIONAL (bitmap[8]) */
    if (opt_present[8]) {
        err = cms_service_error_encode_stream(s, v->gi_err);
        if (err)
            return err;
    }

    /* 10. resvErr — ServiceError OPTIONAL (bitmap[9]) */
    if (opt_present[9]) {
        err = cms_service_error_encode_stream(s, v->resv_err);
        if (err)
            return err;
    }

    return CMS_OK;
}

int cms_set_urcb_result_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_urcb_result_t *v = (cms_set_urcb_result_t *) ptr;
    int err;

    /* 0. OPTIONAL bitmap (10 fields) */
    bool opt_present[10];
    err = (int) per_decode_optional_bitmap(s, opt_present, 10);
    if (err)
        return err;
    if (v) {
        if (v->error_present)
            v->error_present->value = opt_present[0];
        if (v->rpt_id_err_present)
            v->rpt_id_err_present->value = opt_present[1];
        if (v->rpt_ena_err_present)
            v->rpt_ena_err_present->value = opt_present[2];
        if (v->dat_set_err_present)
            v->dat_set_err_present->value = opt_present[3];
        if (v->opt_flds_err_present)
            v->opt_flds_err_present->value = opt_present[4];
        if (v->buf_tm_err_present)
            v->buf_tm_err_present->value = opt_present[5];
        if (v->trg_ops_err_present)
            v->trg_ops_err_present->value = opt_present[6];
        if (v->intg_pd_err_present)
            v->intg_pd_err_present->value = opt_present[7];
        if (v->gi_err_present)
            v->gi_err_present->value = opt_present[8];
        if (v->resv_err_present)
            v->resv_err_present->value = opt_present[9];
    }

    /* 1. error OPTIONAL */ if (opt_present[0]) {
        if (v && !v->error)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->error : NULL);
        if (err)
            return err;
    }
    /* 2. rptIdErr OPTIONAL */ if (opt_present[1]) {
        if (v && !v->rpt_id_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->rpt_id_err : NULL);
        if (err)
            return err;
    }
    /* 3. rptEnaErr OPTIONAL */ if (opt_present[2]) {
        if (v && !v->rpt_ena_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->rpt_ena_err : NULL);
        if (err)
            return err;
    }
    /* 4. datSetErr OPTIONAL */ if (opt_present[3]) {
        if (v && !v->dat_set_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->dat_set_err : NULL);
        if (err)
            return err;
    }
    /* 5. optFldsErr OPTIONAL */ if (opt_present[4]) {
        if (v && !v->opt_flds_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->opt_flds_err : NULL);
        if (err)
            return err;
    }
    /* 6. bufTmErr OPTIONAL */ if (opt_present[5]) {
        if (v && !v->buf_tm_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->buf_tm_err : NULL);
        if (err)
            return err;
    }
    /* 7. trgOpsErr OPTIONAL */ if (opt_present[6]) {
        if (v && !v->trg_ops_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->trg_ops_err : NULL);
        if (err)
            return err;
    }
    /* 8. intgPdErr OPTIONAL */ if (opt_present[7]) {
        if (v && !v->intg_pd_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->intg_pd_err : NULL);
        if (err)
            return err;
    }
    /* 9. giErr OPTIONAL */ if (opt_present[8]) {
        if (v && !v->gi_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->gi_err : NULL);
        if (err)
            return err;
    }
    /* 10. resvErr OPTIONAL */ if (opt_present[9]) {
        if (v && !v->resv_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->resv_err : NULL);
        if (err)
            return err;
    }

    return CMS_OK;
}
