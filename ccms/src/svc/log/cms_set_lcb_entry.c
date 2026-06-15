#include "svc/log/cms_set_lcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_lcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"

int cms_set_lcb_entry_encode_stream(per_stream_t *s, const cms_set_lcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. logEna — BOOLEAN OPTIONAL */
    {
        int present = (v->log_ena_present && v->log_ena_present->value) && v->log_ena;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_boolean_encode_stream(s, v->log_ena);
            if (err) return err;
        }
    }

    /* 3. datSet — ObjectReference OPTIONAL */
    {
        int present = (v->dat_set_present && v->dat_set_present->value) && v->dat_set;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_object_reference_encode_stream(s, v->dat_set);
            if (err) return err;
        }
    }

    /* 4. trgOps — TriggerConditions OPTIONAL */
    {
        int present = (v->trg_ops_present && v->trg_ops_present->value) && v->trg_ops;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_trigger_conditions_encode_stream(s, v->trg_ops);
            if (err) return err;
        }
    }

    /* 5. intgPd — INT32U OPTIONAL */
    {
        int present = (v->intg_pd_present && v->intg_pd_present->value) && v->intg_pd;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_int32u_encode_stream(s, v->intg_pd);
            if (err) return err;
        }
    }

    /* 6. logRef — ObjectReference OPTIONAL */
    {
        int present = (v->log_ref_present && v->log_ref_present->value) && v->log_ref;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_object_reference_encode_stream(s, v->log_ref);
            if (err) return err;
        }
    }

    /* 7. optFlds — LcbOptFlds OPTIONAL */
    {
        int present = (v->opt_flds_present && v->opt_flds_present->value) && v->opt_flds;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_lcb_opt_flds_encode_stream(s, v->opt_flds);
            if (err) return err;
        }
    }

    /* 8. bufTm — INT32U OPTIONAL */
    {
        int present = (v->buf_tm_present && v->buf_tm_present->value) && v->buf_tm;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_int32u_encode_stream(s, v->buf_tm);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_set_lcb_entry_decode_stream(per_stream_t *s, cms_set_lcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference */
    err = cms_object_reference_decode_stream(s, v->reference);
    if (err) return err;

    /* 2. logEna OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->log_ena_present) v->log_ena_present->value = bit.value;
        if (bit.value) {
            if (!v->log_ena) return CMS_ERR;
            err = cms_boolean_decode_stream(s, v->log_ena);
            if (err) return err;
        }
    }

    /* 3. datSet OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->dat_set_present) v->dat_set_present->value = bit.value;
        if (bit.value) {
            if (!v->dat_set) return CMS_ERR;
            err = cms_object_reference_decode_stream(s, v->dat_set);
            if (err) return err;
        }
    }

    /* 4. trgOps OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->trg_ops_present) v->trg_ops_present->value = bit.value;
        if (bit.value) {
            if (!v->trg_ops) return CMS_ERR;
            err = cms_trigger_conditions_decode_stream(s, v->trg_ops);
            if (err) return err;
        }
    }

    /* 5. intgPd OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->intg_pd_present) v->intg_pd_present->value = bit.value;
        if (bit.value) {
            if (!v->intg_pd) return CMS_ERR;
            err = cms_int32u_decode_stream(s, v->intg_pd);
            if (err) return err;
        }
    }

    /* 6. logRef OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->log_ref_present) v->log_ref_present->value = bit.value;
        if (bit.value) {
            if (!v->log_ref) return CMS_ERR;
            err = cms_object_reference_decode_stream(s, v->log_ref);
            if (err) return err;
        }
    }

    /* 7. optFlds OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->opt_flds_present) v->opt_flds_present->value = bit.value;
        if (bit.value) {
            if (!v->opt_flds) return CMS_ERR;
            err = cms_lcb_opt_flds_decode_stream(s, v->opt_flds);
            if (err) return err;
        }
    }

    /* 8. bufTm OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->buf_tm_present) v->buf_tm_present->value = bit.value;
        if (bit.value) {
            if (!v->buf_tm) return CMS_ERR;
            err = cms_int32u_decode_stream(s, v->buf_tm);
            if (err) return err;
        }
    }

    return CMS_OK;
}
