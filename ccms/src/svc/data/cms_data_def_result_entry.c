#include "svc/data/cms_data_def_result_entry.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "data/choice/cms_data_definition.h"
#include "per/cms_sequence.h"

int cms_data_def_result_entry_encode_stream(per_stream_t *s, const cms_data_def_result_entry_t *v) {
    if (!v || !v->definition)
        return CMS_ERR;
    int err;

    /* 0. OPTIONAL bitmap (1 field: cdcType) */
    bool opt[1] = {(v->cdc_type_present && v->cdc_type_present->value) && v->cdc_type};
    err = (int) per_encode_optional_bitmap(s, opt, 1);
    if (err)
        return err;

    /* 1. cdcType — VisibleString(129) OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        err = cms_visible_string_encode_stream(s, v->cdc_type, 129);
        if (err)
            return err;
    }

    /* 2. definition — DataDefinition */
    err = cms_data_definition_encode_stream(s, v->definition);
    if (err)
        return err;

    return CMS_OK;
}

int cms_data_def_result_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_data_def_result_entry_t *v = (cms_data_def_result_entry_t *) ptr;
    int err;

    /* 0. OPTIONAL bitmap (1 field: cdcType) */
    bool opt[1] = {false};
    err = (int) per_decode_optional_bitmap(s, opt, 1);
    if (err)
        return err;
    if (v) {
        if (v->cdc_type_present)
            v->cdc_type_present->value = opt[0] ? 1 : 0;
    }

    /* 1. cdcType OPTIONAL (bitmap[0]) */
    if (opt[0]) {
        if (v && !v->cdc_type)
            return CMS_ERR;
        err = cms_visible_string_decode_stream(s, v ? v->cdc_type : NULL, 129);
        if (err)
            return err;
    }

    /* 2. definition */
    if (v && !v->definition)
        return CMS_ERR;
    err = cms_data_definition_decode_stream(s, v ? v->definition : NULL);
    if (err)
        return err;

    return CMS_OK;
}
