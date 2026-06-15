#include "svc/data/cms_sub_ref_entry.h"
#include "data/common/cms_sub_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"

int cms_sub_ref_entry_encode_stream(per_stream_t *s, const cms_sub_ref_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference — SubReference */
    err = cms_sub_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. fc — FunctionalConstraint OPTIONAL */
    {
        int present = (v->fc_present && v->fc_present->value) && v->fc;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_functional_constraint_encode_stream(s, v->fc);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_sub_ref_entry_decode_stream(per_stream_t *s, cms_sub_ref_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference */
    err = cms_sub_reference_decode_stream(s, v->reference);
    if (err) return err;

    /* 2. fc OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->fc_present) v->fc_present->value = bit.value;
        if (bit.value) {
            if (!v->fc) return CMS_ERR;
            err = cms_functional_constraint_decode_stream(s, v->fc);
            if (err) return err;
        }
    }

    return CMS_OK;
}
