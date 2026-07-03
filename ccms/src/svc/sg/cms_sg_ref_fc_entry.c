#include "svc/sg/cms_sg_ref_fc_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"

int cms_sg_ref_fc_entry_encode_stream(per_stream_t *s, const cms_sg_ref_fc_entry_t *v) {
    if (!v || !v->reference || !v->fc)
        return CMS_ERR;
    int err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err)
        return err;

    /* 2. fc — FunctionalConstraint */
    err = cms_functional_constraint_encode_stream(s, v->fc);
    if (err)
        return err;

    return CMS_OK;
}

int cms_sg_ref_fc_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_sg_ref_fc_entry_t *v = (cms_sg_ref_fc_entry_t *) ptr;
    int err;

    /* 1. reference */
    if (v && !v->reference)
        return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err)
        return err;

    /* 2. fc */
    if (v && !v->fc)
        return CMS_ERR;
    err = cms_functional_constraint_decode_stream(s, v ? v->fc : NULL);
    if (err)
        return err;

    return CMS_OK;
}
