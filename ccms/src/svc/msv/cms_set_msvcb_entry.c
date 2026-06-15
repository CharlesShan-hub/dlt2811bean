#include "svc/msv/cms_set_msvcb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/block/cms_smp_mod.h"
#include "data/block/cms_msvcb_opt_flds.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/string/cms_visible_string.h"

int cms_set_msvcb_entry_encode_stream(per_stream_t *s, const cms_set_msvcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. svEna — BOOLEAN OPTIONAL */
    {
        int present = (v->sv_ena_present && v->sv_ena_present->value) && v->sv_ena;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_boolean_encode_stream(s, v->sv_ena);
            if (err) return err;
        }
    }

    /* 3. msvID — VisibleString(129) OPTIONAL */
    {
        int present = (v->msv_id_present && v->msv_id_present->value) && v->msv_id;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_visible_string_encode_stream(s, v->msv_id, 129);
            if (err) return err;
        }
    }

    /* 4. datSet — ObjectReference OPTIONAL */
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

    /* 5. smpMod — SmpMod OPTIONAL */
    {
        int present = (v->smp_mod_present && v->smp_mod_present->value) && v->smp_mod;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_smp_mod_encode_stream(s, v->smp_mod);
            if (err) return err;
        }
    }

    /* 6. smpRate — INT16U OPTIONAL */
    {
        int present = (v->smp_rate_present && v->smp_rate_present->value) && v->smp_rate;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_int16u_encode_stream(s, v->smp_rate);
            if (err) return err;
        }
    }

    /* 7. optFlds — MsvcbOptFlds OPTIONAL */
    {
        int present = (v->opt_flds_present && v->opt_flds_present->value) && v->opt_flds;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_msvcb_opt_flds_encode_stream(s, v->opt_flds);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_set_msvcb_entry_decode_stream(per_stream_t *s, cms_set_msvcb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference */
    err = cms_object_reference_decode_stream(s, v->reference);
    if (err) return err;

    /* 2. svEna OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->sv_ena_present) v->sv_ena_present->value = bit.value;
        if (bit.value) {
            if (!v->sv_ena) return CMS_ERR;
            err = cms_boolean_decode_stream(s, v->sv_ena);
            if (err) return err;
        }
    }

    /* 3. msvID OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->msv_id_present) v->msv_id_present->value = bit.value;
        if (bit.value) {
            if (!v->msv_id) return CMS_ERR;
            err = cms_visible_string_decode_stream(s, v->msv_id, 129);
            if (err) return err;
        }
    }

    /* 4. datSet OPTIONAL */
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

    /* 5. smpMod OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->smp_mod_present) v->smp_mod_present->value = bit.value;
        if (bit.value) {
            if (!v->smp_mod) return CMS_ERR;
            err = cms_smp_mod_decode_stream(s, v->smp_mod);
            if (err) return err;
        }
    }

    /* 6. smpRate OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->smp_rate_present) v->smp_rate_present->value = bit.value;
        if (bit.value) {
            if (!v->smp_rate) return CMS_ERR;
            err = cms_int16u_decode_stream(s, v->smp_rate);
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
            err = cms_msvcb_opt_flds_decode_stream(s, v->opt_flds);
            if (err) return err;
        }
    }

    return CMS_OK;
}
