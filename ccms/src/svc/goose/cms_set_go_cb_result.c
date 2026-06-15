#include "svc/goose/cms_set_go_cb_result.h"
#include "data/scalar/cms_boolean.h"
#include "data/common/cms_service_error.h"

int cms_set_go_cb_result_encode_stream(per_stream_t *s, const cms_set_go_cb_result_t *v) {
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

    /* 2. goEnaErr — ServiceError OPTIONAL */
    {
        int present = (v->go_ena_err_present && v->go_ena_err_present->value) && v->go_ena_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->go_ena_err);
            if (err) return err;
        }
    }

    /* 3. goIdErr — ServiceError OPTIONAL */
    {
        int present = (v->go_id_err_present && v->go_id_err_present->value) && v->go_id_err;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_service_error_encode_stream(s, v->go_id_err);
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

    return CMS_OK;
}

int cms_set_go_cb_result_decode_stream(per_stream_t *s, cms_set_go_cb_result_t *v) {
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

    /* 2. goEnaErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->go_ena_err_present) v->go_ena_err_present->value = bit.value;
        if (bit.value) {
            if (!v->go_ena_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->go_ena_err);
            if (err) return err;
        }
    }

    /* 3. goIdErr OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->go_id_err_present) v->go_id_err_present->value = bit.value;
        if (bit.value) {
            if (!v->go_id_err) return CMS_ERR;
            err = cms_service_error_decode_stream(s, v->go_id_err);
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

    return CMS_OK;
}
