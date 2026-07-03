#include "svc/log/cms_set_lcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_lcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "per/cms_sequence.h"

int cms_set_lcb_entry_encode_stream(per_stream_t *s, const cms_set_lcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 0. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 1. OPTIONAL bitmap (7 fields: logEna, datSet, trgOps, intgPd, logRef, optFlds, bufTm) */
    bool opt_present[7] = {
        (v->log_ena_present && v->log_ena_present->value) && v->log_ena,
        (v->dat_set_present && v->dat_set_present->value) && v->dat_set,
        (v->trg_ops_present && v->trg_ops_present->value) && v->trg_ops,
        (v->intg_pd_present && v->intg_pd_present->value) && v->intg_pd,
        (v->log_ref_present && v->log_ref_present->value) && v->log_ref,
        (v->opt_flds_present && v->opt_flds_present->value) && v->opt_flds,
        (v->buf_tm_present && v->buf_tm_present->value) && v->buf_tm
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 7);
    if (err) return err;

    /* 2. logEna — BOOLEAN OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_boolean_encode_stream(s, v->log_ena);
        if (err) return err;
    }

    /* 3. datSet — ObjectReference OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_object_reference_encode_stream(s, v->dat_set);
        if (err) return err;
    }

    /* 4. trgOps — TriggerConditions OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_trigger_conditions_encode_stream(s, v->trg_ops);
        if (err) return err;
    }

    /* 5. intgPd — INT32U OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_int32u_encode_stream(s, v->intg_pd);
        if (err) return err;
    }

    /* 6. logRef — ObjectReference OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_object_reference_encode_stream(s, v->log_ref);
        if (err) return err;
    }

    /* 7. optFlds — LcbOptFlds OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_lcb_opt_flds_encode_stream(s, v->opt_flds);
        if (err) return err;
    }

    /* 8. bufTm — INT32U OPTIONAL (bitmap[6]) */
    if (opt_present[6]) {
        err = cms_int32u_encode_stream(s, v->buf_tm);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_set_lcb_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_lcb_entry_t *v = (cms_set_lcb_entry_t*)ptr;
    int err;

    /* 0. reference */
    if (v && !v->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err) return err;

    /* 1. OPTIONAL bitmap (7 fields) */
    bool opt_present[7];
    err = (int)per_decode_optional_bitmap(s, opt_present, 7);
    if (err) return err;
    if (v) {
        if (v->log_ena_present)  v->log_ena_present->value  = opt_present[0];
        if (v->dat_set_present)  v->dat_set_present->value  = opt_present[1];
        if (v->trg_ops_present)  v->trg_ops_present->value  = opt_present[2];
        if (v->intg_pd_present)  v->intg_pd_present->value  = opt_present[3];
        if (v->log_ref_present)  v->log_ref_present->value  = opt_present[4];
        if (v->opt_flds_present) v->opt_flds_present->value = opt_present[5];
        if (v->buf_tm_present)   v->buf_tm_present->value   = opt_present[6];
    }

    #define DECODE_OPT(field, fn, arg) do { if (opt_present[field]) { if (v && !(arg)) return CMS_ERR; err = fn(s, v ? (arg) : NULL); if (err) return err; } } while(0)

    DECODE_OPT(0, cms_boolean_decode_stream, v->log_ena);
    DECODE_OPT(1, cms_object_reference_decode_stream, v->dat_set);
    DECODE_OPT(2, cms_trigger_conditions_decode_stream, v->trg_ops);
    DECODE_OPT(3, cms_int32u_decode_stream, v->intg_pd);
    DECODE_OPT(4, cms_object_reference_decode_stream, v->log_ref);
    DECODE_OPT(5, cms_lcb_opt_flds_decode_stream, v->opt_flds);
    DECODE_OPT(6, cms_int32u_decode_stream, v->buf_tm);

    #undef DECODE_OPT
    return CMS_OK;
}
