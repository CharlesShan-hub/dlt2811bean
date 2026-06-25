#include "svc/report/cms_report_data_entry.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_object_reference.h"
#include "data/fc/cms_functional_constraint.h"
#include "data/scalar/cms_boolean.h"
#include "data/scalar/cms_int16u.h"
#include "data/block/cms_reason_code.h"
#include "per/cms_sequence.h"

int cms_report_data_entry_encode_stream(per_stream_t *s, const cms_report_data_entry_t *v) {
    if (!v || !v->id || !v->value) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (3 fields: ref, fc, reason) */
    bool opt_present[3] = {
        (v->ref_present && v->ref_present->value) && v->reference,
        (v->fc_present && v->fc_present->value) && v->fc,
        (v->reason_present && v->reason_present->value) && v->reason
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 3);
    if (err) return err;

    /* 1. reference — ObjectReference OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_object_reference_encode_stream(s, v->reference);
        if (err) return err;
    }

    /* 2. fc — FunctionalConstraint OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_functional_constraint_encode_stream(s, v->fc);
        if (err) return err;
    }

    /* 3. id — INT16U */
    err = cms_int16u_encode_stream(s, v->id);
    if (err) return err;

    /* 4. value — Data */
    err = cms_data_encode_stream(s, v->value);
    if (err) return err;

    /* 5. reason — ReasonCode OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_reason_code_encode_stream(s, v->reason);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_report_data_entry_decode_stream(per_stream_t *s, cms_report_data_entry_t *v) {
    if (!v || !v->id || !v->value) return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (3 fields: ref, fc, reason) */
    bool opt_present[3];
    err = (int)per_decode_optional_bitmap(s, opt_present, 3);
    if (err) return err;
    if (v->ref_present) v->ref_present->value = opt_present[0];
    if (v->fc_present) v->fc_present->value = opt_present[1];
    if (v->reason_present) v->reason_present->value = opt_present[2];

    /* 1. reference OPTIONAL */
    if (opt_present[0] && v->reference) {
        err = cms_object_reference_decode_stream(s, v->reference);
        if (err) return err;
    }

    /* 2. fc OPTIONAL */
    if (opt_present[1] && v->fc) {
        err = cms_functional_constraint_decode_stream(s, v->fc);
        if (err) return err;
    }

    /* 3. id */
    err = cms_int16u_decode_stream(s, v->id);
    if (err) return err;

    /* 4. value */
    err = cms_data_decode_stream(s, v->value);
    if (err) return err;

    /* 5. reason OPTIONAL */
    if (opt_present[2] && v->reason) {
        err = cms_reason_code_decode_stream(s, v->reason);
        if (err) return err;
    }

    return CMS_OK;
}
