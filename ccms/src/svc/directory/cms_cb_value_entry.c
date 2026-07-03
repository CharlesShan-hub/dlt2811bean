#include "svc/directory/cms_cb_value_entry.h"
#include "svc/directory/cms_cb_value_choice.h"
#include "data/common/cms_sub_reference.h"

int cms_cb_value_entry_encode_stream(per_stream_t *s, const cms_cb_value_entry_t *v) {
    if (!v || !v->reference || !v->value) return CMS_ERR;
    int err;

    /* 1. reference — SubReference */
    err = cms_sub_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. value — CbValueChoice */
    err = cms_cb_value_choice_encode_stream(s, v->value);
    if (err) return err;

    return CMS_OK;
}

int cms_cb_value_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_cb_value_entry_t *v = (cms_cb_value_entry_t*)ptr;
    int err;

    /* 1. reference */
    if (v && !v->reference) return CMS_ERR;
    err = cms_sub_reference_decode_stream(s, v ? v->reference : NULL);
    if (err) return err;

    /* 2. value */
    if (v && !v->value) return CMS_ERR;
    err = cms_cb_value_choice_decode_stream(s, v ? v->value : NULL);
    if (err) return err;

    return CMS_OK;
}
