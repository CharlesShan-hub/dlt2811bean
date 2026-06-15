#include "svc/log/cms_log_data_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/choice/cms_data.h"
#include "data/block/cms_reason_code.h"

int cms_log_data_entry_encode_stream(per_stream_t *s, const cms_log_data_entry_t *v) {
    if (!v || !v->reference || !v->fc || !v->value || !v->reason) return CMS_ERR;
    int err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. fc — FunctionalConstraint */
    err = cms_functional_constraint_encode_stream(s, v->fc);
    if (err) return err;

    /* 3. value — Data */
    err = cms_data_encode_stream(s, v->value);
    if (err) return err;

    /* 4. reason — ReasonCode */
    err = cms_reason_code_encode_stream(s, v->reason);
    if (err) return err;

    return CMS_OK;
}

int cms_log_data_entry_decode_stream(per_stream_t *s, cms_log_data_entry_t *v) {
    if (!v || !v->reference || !v->fc || !v->value || !v->reason) return CMS_ERR;
    int err;

    /* 1. reference */
    err = cms_object_reference_decode_stream(s, v->reference);
    if (err) return err;

    /* 2. fc */
    err = cms_functional_constraint_decode_stream(s, v->fc);
    if (err) return err;

    /* 3. value */
    err = cms_data_decode_stream(s, v->value);
    if (err) return err;

    /* 4. reason */
    err = cms_reason_code_decode_stream(s, v->reason);
    if (err) return err;

    return CMS_OK;
}
