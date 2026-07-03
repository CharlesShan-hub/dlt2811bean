#include "svc/msv/cms_set_msvcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_smp_mod.h"
#include "data/block/cms_msvcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_sequence.h"

int cms_set_msvcb_entry_encode_stream(per_stream_t *s, const cms_set_msvcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 0. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 1. OPTIONAL bitmap (6 fields: svEna, msvID, datSet, smpMod, smpRate, optFlds) */
    bool opt_present[6] = {
        (v->sv_ena_present && v->sv_ena_present->value) && v->sv_ena,
        (v->msv_id_present && v->msv_id_present->value) && v->msv_id,
        (v->dat_set_present && v->dat_set_present->value) && v->dat_set,
        (v->smp_mod_present && v->smp_mod_present->value) && v->smp_mod,
        (v->smp_rate_present && v->smp_rate_present->value) && v->smp_rate,
        (v->opt_flds_present && v->opt_flds_present->value) && v->opt_flds
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 6);
    if (err) return err;

    /* 2. svEna — BOOLEAN OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_boolean_encode_stream(s, v->sv_ena);
        if (err) return err;
    }

    /* 3. msvID — VisibleString(129) OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_visible_string_encode_stream(s, v->msv_id, 129);
        if (err) return err;
    }

    /* 4. datSet — ObjectReference OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_object_reference_encode_stream(s, v->dat_set);
        if (err) return err;
    }

    /* 5. smpMod — SmpMod OPTIONAL (bitmap[3]) */
    if (opt_present[3]) {
        err = cms_smp_mod_encode_stream(s, v->smp_mod);
        if (err) return err;
    }

    /* 6. smpRate — INT16U OPTIONAL (bitmap[4]) */
    if (opt_present[4]) {
        err = cms_int16u_encode_stream(s, v->smp_rate);
        if (err) return err;
    }

    /* 7. optFlds — MsvcbOptFlds OPTIONAL (bitmap[5]) */
    if (opt_present[5]) {
        err = cms_msvcb_opt_flds_encode_stream(s, v->opt_flds);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_set_msvcb_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_msvcb_entry_t *v = (cms_set_msvcb_entry_t*)ptr;
    int err;

    /* 0. reference */
    if (v && !v->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err) return err;

    /* 1. OPTIONAL bitmap (6 fields) */
    bool opt_present[6];
    err = (int)per_decode_optional_bitmap(s, opt_present, 6);
    if (err) return err;
    if (v) {
        if (v->sv_ena_present)    v->sv_ena_present->value    = opt_present[0];
        if (v->msv_id_present)    v->msv_id_present->value    = opt_present[1];
        if (v->dat_set_present)   v->dat_set_present->value   = opt_present[2];
        if (v->smp_mod_present)   v->smp_mod_present->value   = opt_present[3];
        if (v->smp_rate_present)  v->smp_rate_present->value  = opt_present[4];
        if (v->opt_flds_present)  v->opt_flds_present->value  = opt_present[5];
    }

    /* 2. svEna OPTIONAL */   if (opt_present[0]) { if (v && !v->sv_ena) return CMS_ERR; err = cms_boolean_decode_stream(s, v ? v->sv_ena : NULL); if (err) return err; }
    /* 3. msvID OPTIONAL */   if (opt_present[1]) { if (v && !v->msv_id) return CMS_ERR; err = cms_visible_string_decode_stream(s, v ? v->msv_id : NULL, 129); if (err) return err; }
    /* 4. datSet OPTIONAL */  if (opt_present[2]) { if (v && !v->dat_set) return CMS_ERR; err = cms_object_reference_decode_stream(s, v ? v->dat_set : NULL); if (err) return err; }
    /* 5. smpMod OPTIONAL */  if (opt_present[3]) { if (v && !v->smp_mod) return CMS_ERR; err = cms_smp_mod_decode_stream(s, v ? v->smp_mod : NULL); if (err) return err; }
    /* 6. smpRate OPTIONAL */ if (opt_present[4]) { if (v && !v->smp_rate) return CMS_ERR; err = cms_int16u_decode_stream(s, v ? v->smp_rate : NULL); if (err) return err; }
    /* 7. optFlds OPTIONAL */ if (opt_present[5]) { if (v && !v->opt_flds) return CMS_ERR; err = cms_msvcb_opt_flds_decode_stream(s, v ? v->opt_flds : NULL); if (err) return err; }

    return CMS_OK;
}
