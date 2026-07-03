#include "svc/report/cms_set_urcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_trigger_conditions.h"
#include "data/block/cms_rcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_sequence.h"

int cms_set_urcb_entry_encode_stream(per_stream_t *s, const cms_set_urcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 0. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 1. OPTIONAL bitmap (9 fields: rptID, rptEna, datSet, optFlds, bufTm, trgOps, intgPd, gi, resv) */
    bool opt_present[9] = {
        (v->rpt_id_present && v->rpt_id_present->value) && v->rpt_id,
        (v->rpt_ena_present && v->rpt_ena_present->value) && v->rpt_ena,
        (v->dat_set_present && v->dat_set_present->value) && v->dat_set,
        (v->opt_flds_present && v->opt_flds_present->value) && v->opt_flds,
        (v->buf_tm_present && v->buf_tm_present->value) && v->buf_tm,
        (v->trg_ops_present && v->trg_ops_present->value) && v->trg_ops,
        (v->intg_pd_present && v->intg_pd_present->value) && v->intg_pd,
        (v->gi_present && v->gi_present->value) && v->gi,
        (v->resv_present && v->resv_present->value) && v->resv
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 9);
    if (err) return err;

    /* 2. rptID — VisibleString(129) OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_visible_string_encode_stream(s, v->rpt_id, 129);
        if (err) return err;
    }

    /* 3. rptEna — BOOLEAN OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_boolean_encode_stream(s, v->rpt_ena);
        if (err) return err;
    }

    /* 4. datSet — ObjectReference OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_object_reference_encode_stream(s, v->dat_set);
        if (err) return err;
    }

    /* 5. optFlds — RCBOptFlds OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_rcb_opt_flds_encode_stream(s, v->opt_flds);
        if (err) return err;
    }

    /* 6. bufTm — INT32U OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_int32u_encode_stream(s, v->buf_tm);
        if (err) return err;
    }

    /* 7. trgOps — TriggerConditions OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_trigger_conditions_encode_stream(s, v->trg_ops);
        if (err) return err;
    }

    /* 8. intgPd — INT32U OPTIONAL (bitmap[6]) */
    if (opt_present[6]) {
        err = cms_int32u_encode_stream(s, v->intg_pd);
        if (err) return err;
    }

    /* 9. gi — BOOLEAN OPTIONAL (bitmap[7]) */
    if (opt_present[7]) {
        err = cms_boolean_encode_stream(s, v->gi);
        if (err) return err;
    }

    /* 10. resv — BOOLEAN OPTIONAL (bitmap[8]) */
    if (opt_present[8]) {
        err = cms_boolean_encode_stream(s, v->resv);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_set_urcb_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_urcb_entry_t *v = (cms_set_urcb_entry_t*)ptr;
    int err;

    /* 0. reference */
    if (v && !v->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err) return err;

    /* 1. OPTIONAL bitmap (9 fields) */
    bool opt_present[9];
    err = (int)per_decode_optional_bitmap(s, opt_present, 9);
    if (err) return err;
    if (v) {
        if (v->rpt_id_present)    v->rpt_id_present->value    = opt_present[0];
        if (v->rpt_ena_present)   v->rpt_ena_present->value   = opt_present[1];
        if (v->dat_set_present)   v->dat_set_present->value   = opt_present[2];
        if (v->opt_flds_present)  v->opt_flds_present->value  = opt_present[3];
        if (v->buf_tm_present)    v->buf_tm_present->value    = opt_present[4];
        if (v->trg_ops_present)   v->trg_ops_present->value   = opt_present[5];
        if (v->intg_pd_present)   v->intg_pd_present->value   = opt_present[6];
        if (v->gi_present)        v->gi_present->value        = opt_present[7];
        if (v->resv_present)      v->resv_present->value      = opt_present[8];
    }

    /* 2. rptID OPTIONAL */   if (opt_present[0]) { if (v && !v->rpt_id) return CMS_ERR; err = cms_visible_string_decode_stream(s, v ? v->rpt_id : NULL, 129); if (err) return err; }
    /* 3. rptEna OPTIONAL */  if (opt_present[1]) { if (v && !v->rpt_ena) return CMS_ERR; err = cms_boolean_decode_stream(s, v ? v->rpt_ena : NULL); if (err) return err; }
    /* 4. datSet OPTIONAL */  if (opt_present[2]) { if (v && !v->dat_set) return CMS_ERR; err = cms_object_reference_decode_stream(s, v ? v->dat_set : NULL); if (err) return err; }
    /* 5. optFlds OPTIONAL */ if (opt_present[3]) { if (v && !v->opt_flds) return CMS_ERR; err = cms_rcb_opt_flds_decode_stream(s, v ? v->opt_flds : NULL); if (err) return err; }
    /* 6. bufTm OPTIONAL */   if (opt_present[4]) { if (v && !v->buf_tm) return CMS_ERR; err = cms_int32u_decode_stream(s, v ? v->buf_tm : NULL); if (err) return err; }
    /* 7. trgOps OPTIONAL */  if (opt_present[5]) { if (v && !v->trg_ops) return CMS_ERR; err = cms_trigger_conditions_decode_stream(s, v ? v->trg_ops : NULL); if (err) return err; }
    /* 8. intgPd OPTIONAL */  if (opt_present[6]) { if (v && !v->intg_pd) return CMS_ERR; err = cms_int32u_decode_stream(s, v ? v->intg_pd : NULL); if (err) return err; }
    /* 9. gi OPTIONAL */      if (opt_present[7]) { if (v && !v->gi) return CMS_ERR; err = cms_boolean_decode_stream(s, v ? v->gi : NULL); if (err) return err; }
    /* 10. resv OPTIONAL */   if (opt_present[8]) { if (v && !v->resv) return CMS_ERR; err = cms_boolean_decode_stream(s, v ? v->resv : NULL); if (err) return err; }

    return CMS_OK;
}
