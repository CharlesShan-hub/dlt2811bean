#include "svc/directory/cms_data_definition_entry.h"
#include "data/common/cms_sub_reference.h"
#include "data/choice/cms_data_definition.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"

int cms_data_definition_entry_encode_stream(per_stream_t *s, const cms_data_definition_entry_t *v) {
    if (!v || !v->reference || !v->definition) return CMS_ERR;
    int err;
    err = cms_sub_reference_encode_stream(s, v->reference);
    if (err) return err;
    /* cdcType OPTIONAL */
    {
        int present = (v->cdc_type_present && v->cdc_type_present->value) && v->cdc_type;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_visible_string_encode_stream(s, v->cdc_type, 129);
            if (err) return err;
        }
    }
    err = cms_data_definition_encode_stream(s, v->definition);
    if (err) return err;
    return CMS_OK;
}

int cms_data_definition_entry_decode_stream(per_stream_t *s, cms_data_definition_entry_t *v) {
    if (!v || !v->reference || !v->definition) return CMS_ERR;
    int err;
    err = cms_sub_reference_decode_stream(s, v->reference);
    if (err) return err;
    /* cdcType OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->cdc_type_present) v->cdc_type_present->value = bit.value;
        if (bit.value) {
            if (!v->cdc_type) return CMS_ERR;
            err = cms_visible_string_decode_stream(s, v->cdc_type, 129);
            if (err) return err;
        }
    }
    err = cms_data_definition_decode_stream(s, v->definition);
    if (err) return err;
    return CMS_OK;
}
