#include "svc/sg/cms_sg_ref_value_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/choice/cms_data.h"

int cms_sg_ref_value_entry_encode_stream(per_stream_t *s, const cms_sg_ref_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. value — Data */
    err = cms_data_encode_stream(s, v->value);
    if (err) return err;

    return CMS_OK;
}

int cms_sg_ref_value_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_sg_ref_value_entry_t *v = (cms_sg_ref_value_entry_t*)ptr;
    int err;

    /* 1. reference */
    if (v && !v->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err) return err;

    /* 2. value */
    if (v && !v->value) return CMS_ERR;
    err = cms_data_decode_stream(s, v ? v->value : NULL);
    if (err) return err;

    return CMS_OK;
}
