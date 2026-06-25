#include "svc/data/cms_data_ref_value_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/choice/cms_data.h"
#include "data/scalar/cms_boolean.h"
#include "per/cms_sequence.h"

int cms_data_ref_value_entry_encode_stream(per_stream_t *s, const cms_data_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (1 field: fc) */
    bool opt[1] = {
        (v->fc_present && v->fc_present->value) && v->fc
    };
    err = (int)per_encode_optional_bitmap(s, opt, 1);
    if (err) return err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. fc — FunctionalConstraint OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_functional_constraint_encode_stream(s, v->fc);
        if (err) return err;
    }

    /* 3. value — Data */
    err = cms_data_encode_stream(s, v->value);
    if (err) return err;

    return CMS_OK;
}

int cms_data_ref_value_entry_decode_stream(per_stream_t *s, cms_data_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (1 field: fc) */
    bool opt[1] = {false};
    err = (int)per_decode_optional_bitmap(s, opt, 1);
    if (err) return err;
    if (v->fc_present)
        v->fc_present->value = opt[0] ? 1 : 0;

    /* 1. reference */
    err = cms_object_reference_decode_stream(s, v->reference);
    if (err) return err;

    /* 2. fc OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (!v->fc) return CMS_ERR;
        err = cms_functional_constraint_decode_stream(s, v->fc);
        if (err) return err;
    }

    /* 3. value */
    err = cms_data_decode_stream(s, v->value);
    if (err) return err;

    return CMS_OK;
}
