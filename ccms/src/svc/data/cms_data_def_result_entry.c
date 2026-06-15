#include "svc/data/cms_data_def_result_entry.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "data/choice/cms_data_definition.h"

int cms_data_def_result_entry_encode_stream(per_stream_t *s, const cms_data_def_result_entry_t *v) {
    if (!v || !v->definition) return CMS_ERR;
    int err;

    /* 1. cdcType — VisibleString(129) OPTIONAL */
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

    /* 2. definition — DataDefinition */
    err = cms_data_definition_encode_stream(s, v->definition);
    if (err) return err;

    return CMS_OK;
}

int cms_data_def_result_entry_decode_stream(per_stream_t *s, cms_data_def_result_entry_t *v) {
    if (!v || !v->definition) return CMS_ERR;
    int err;

    /* 1. cdcType OPTIONAL */
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

    /* 2. definition */
    err = cms_data_definition_decode_stream(s, v->definition);
    if (err) return err;

    return CMS_OK;
}
