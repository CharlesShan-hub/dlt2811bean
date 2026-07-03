#include "svc/goose/cms_set_go_cb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"
#include "per/cms_sequence.h"

int cms_set_go_cb_result_encode_stream(per_stream_t *s, const cms_set_go_cb_result_t *v) {
    if (!v)
        return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (4 fields: error, goEnaErr, goIdErr, datSetErr) */
    bool opt_present[4] = {(v->error_present && v->error_present->value) && v->error,
                           (v->go_ena_err_present && v->go_ena_err_present->value) && v->go_ena_err,
                           (v->go_id_err_present && v->go_id_err_present->value) && v->go_id_err,
                           (v->dat_set_err_present && v->dat_set_err_present->value) && v->dat_set_err};
    err = (int) per_encode_optional_bitmap(s, opt_present, 4);
    if (err)
        return err;

    /* 1. error — ServiceError OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_service_error_encode_stream(s, v->error);
        if (err)
            return err;
    }

    /* 2. goEnaErr — ServiceError OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_service_error_encode_stream(s, v->go_ena_err);
        if (err)
            return err;
    }

    /* 3. goIdErr — ServiceError OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_service_error_encode_stream(s, v->go_id_err);
        if (err)
            return err;
    }

    /* 4. datSetErr — ServiceError OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_service_error_encode_stream(s, v->dat_set_err);
        if (err)
            return err;
    }

    return CMS_OK;
}

int cms_set_go_cb_result_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_go_cb_result_t *v = (cms_set_go_cb_result_t *) ptr;
    int err;

    /* 0. OPTIONAL bitmap (4 fields) */
    bool opt_present[4];
    err = (int) per_decode_optional_bitmap(s, opt_present, 4);
    if (err)
        return err;
    if (v) {
        if (v->error_present)
            v->error_present->value = opt_present[0];
        if (v->go_ena_err_present)
            v->go_ena_err_present->value = opt_present[1];
        if (v->go_id_err_present)
            v->go_id_err_present->value = opt_present[2];
        if (v->dat_set_err_present)
            v->dat_set_err_present->value = opt_present[3];
    }

    /* 1. error OPTIONAL */ if (opt_present[0]) {
        if (v && !v->error)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->error : NULL);
        if (err)
            return err;
    }
    /* 2. goEnaErr OPTIONAL */ if (opt_present[1]) {
        if (v && !v->go_ena_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->go_ena_err : NULL);
        if (err)
            return err;
    }
    /* 3. goIdErr OPTIONAL */ if (opt_present[2]) {
        if (v && !v->go_id_err)
            return CMS_ERR;
        err = cms_service_error_decode_stream(s, v ? v->go_id_err : NULL);
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

    return CMS_OK;
}
