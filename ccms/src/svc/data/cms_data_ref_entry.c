#include "svc/data/cms_data_ref_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "per/cms_sequence.h"

int cms_data_ref_entry_encode_stream(per_stream_t *s, const cms_data_ref_entry_t *v) {
    if (!v || !v->reference)
        return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (1 field: fc) */
    bool opt[1] = {(v->fc_present && v->fc_present->value) && v->fc};
    err = (int) per_encode_optional_bitmap(s, opt, 1);
    if (err)
        return err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err)
        return err;

    /* 2. fc — FunctionalConstraint OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_functional_constraint_encode_stream(s, v->fc);
        if (err)
            return err;
    }

    return CMS_OK;
}

int cms_data_ref_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_data_ref_entry_t *v = (cms_data_ref_entry_t *) ptr;
    int err;

    /* 0. OPTIONAL bitmap (1 field: fc) */
    bool opt[1] = {false};
    err = (int) per_decode_optional_bitmap(s, opt, 1);
    if (err)
        return err;
    if (v) {
        if (v->fc_present)
            v->fc_present->value = opt[0] ? 1 : 0;
    }

    /* 1. reference */
    if (v && !v->reference)
        return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err)
        return err;

    /* 2. fc OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (v && !v->fc)
            return CMS_ERR;
        err = cms_functional_constraint_decode_stream(s, v ? v->fc : NULL);
        if (err)
            return err;
    }

    return CMS_OK;
}
