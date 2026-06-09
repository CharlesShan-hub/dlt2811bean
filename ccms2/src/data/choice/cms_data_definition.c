#include "data/choice/cms_data_definition.h"
#include "per/cms_integer.h"

/* ── helper to encode/decode a struct_elem ── */
static int encode_struct_elem(per_stream_t *s, const cms_data_definition_struct_elem_t *e) {
    if (!e) return CMS_ERR;
    int err;

    /* name — ObjectName */
    if (!e->name) return CMS_ERR;
    err = cms_object_name_encode_stream(s, e->name);
    if (err) return err;

    /* fc — FunctionalConstraint OPTIONAL */
    {
        int present = (e->fc_present && e->fc_present->value) && e->fc;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_functional_constraint_encode_stream(s, e->fc);
            if (err) return err;
        }
    }

    /* type — DataDefinition */
    if (!e->type) return CMS_ERR;
    err = cms_data_definition_encode_stream(s, e->type);
    if (err) return err;

    return CMS_OK;
}

static int decode_struct_elem(per_stream_t *s, cms_data_definition_struct_elem_t *e) {
    if (!e) return CMS_ERR;
    int err;

    /* name */
    if (!e->name) return CMS_ERR;
    err = cms_object_name_decode_stream(s, e->name);
    if (err) return err;

    /* fc OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (bit.value && e->fc) {
            err = cms_functional_constraint_decode_stream(s, e->fc);
            if (err) return err;
        }
        if (e->fc_present) e->fc_present->value = bit.value;
    }

    /* type */
    if (!e->type) return CMS_ERR;
    err = cms_data_definition_decode_stream(s, e->type);
    if (err) return err;

    return CMS_OK;
}

/* ── main encode / decode ── */

int cms_data_definition_encode_stream(per_stream_t *s, const void *ptr) {
    const cms_data_definition_t *d = (const cms_data_definition_t*)ptr;
    if (!d || !d->choice) return CMS_ERR;
    int sel = d->choice->value;
    if (sel < 0 || sel > 23) return CMS_ERR;

    /* 1. Encode CHOICE index — small non-negative */
    per_error_t perr = per_encode_small_non_negative(s, (uint32_t)sel);
    if (perr) return CMS_ERR;

    /* 2. Encode the selected alternative (if it has payload) */
    switch (sel) {

    case CMS_DATA_DEFINITION_CHOICE_ERROR:
        if (!d->alt_error) return CMS_ERR;
        return cms_service_error_encode_stream(s, d->alt_error);

    case CMS_DATA_DEFINITION_CHOICE_ARRAY:
        if (!d->alt_array) return CMS_ERR;
        return cms_data_definition_array_encode_stream(s, d->alt_array);

    case CMS_DATA_DEFINITION_CHOICE_STRUCTURE: {
        cms_array_t *arr = (cms_array_t*)d->alt_structure;
        int32_t count = arr ? arr->count : 0;
        perr = per_encode_length(s, (uint32_t)count);
        if (perr) return CMS_ERR;
        for (int32_t i = 0; i < count; i++) {
            if (!arr || !arr->elements || !arr->elements[i]) return CMS_ERR;
            int err = encode_struct_elem(s, (const cms_data_definition_struct_elem_t*)arr->elements[i]);
            if (err) return err;
        }
        return CMS_OK;
    }

    /* [3..13] and [18..23] — NULL alternatives, no payload */
    case CMS_DATA_DEFINITION_CHOICE_BOOLEAN:
    case CMS_DATA_DEFINITION_CHOICE_INT8:
    case CMS_DATA_DEFINITION_CHOICE_INT16:
    case CMS_DATA_DEFINITION_CHOICE_INT32:
    case CMS_DATA_DEFINITION_CHOICE_INT64:
    case CMS_DATA_DEFINITION_CHOICE_INT8U:
    case CMS_DATA_DEFINITION_CHOICE_INT16U:
    case CMS_DATA_DEFINITION_CHOICE_INT32U:
    case CMS_DATA_DEFINITION_CHOICE_INT64U:
    case CMS_DATA_DEFINITION_CHOICE_FLOAT32:
    case CMS_DATA_DEFINITION_CHOICE_FLOAT64:
    case CMS_DATA_DEFINITION_CHOICE_UTC_TIME:
    case CMS_DATA_DEFINITION_CHOICE_BINARY_TIME:
    case CMS_DATA_DEFINITION_CHOICE_QUALITY:
    case CMS_DATA_DEFINITION_CHOICE_DBPOS:
    case CMS_DATA_DEFINITION_CHOICE_TCMD:
    case CMS_DATA_DEFINITION_CHOICE_CHECK:
        return CMS_OK;

    /* [14..17] — INTEGER max length */
    case CMS_DATA_DEFINITION_CHOICE_BIT_STRING:
        if (!d->alt_bit_string_len) return CMS_ERR;
        return cms_int32_encode_stream(s, d->alt_bit_string_len);

    case CMS_DATA_DEFINITION_CHOICE_OCTET_STRING:
        if (!d->alt_octet_string_len) return CMS_ERR;
        return cms_int32_encode_stream(s, d->alt_octet_string_len);

    case CMS_DATA_DEFINITION_CHOICE_VISIBLE_STRING:
        if (!d->alt_visible_string_len) return CMS_ERR;
        return cms_int32_encode_stream(s, d->alt_visible_string_len);

    case CMS_DATA_DEFINITION_CHOICE_UNICODE_STRING:
        if (!d->alt_unicode_string_len) return CMS_ERR;
        return cms_int32_encode_stream(s, d->alt_unicode_string_len);

    default:
        return CMS_ERR;
    }
}

int cms_data_definition_decode_stream(per_stream_t *s, void *ptr) {
    cms_data_definition_t *d = (cms_data_definition_t*)ptr;
    if (!d || !d->choice) return CMS_ERR;

    /* 1. Decode CHOICE index */
    uint32_t sel32;
    per_error_t perr = per_decode_small_non_negative(s, &sel32);
    if (perr) return CMS_ERR;
    if (sel32 > 23) return CMS_ERR;
    d->choice->value = (int)sel32;

    /* 2. Decode payload */
    switch (d->choice->value) {

    case CMS_DATA_DEFINITION_CHOICE_ERROR:
        if (!d->alt_error) return CMS_ERR;
        return cms_service_error_decode_stream(s, d->alt_error);

    case CMS_DATA_DEFINITION_CHOICE_ARRAY:
        if (!d->alt_array) return CMS_ERR;
        return cms_data_definition_array_decode_stream(s, d->alt_array);

    case CMS_DATA_DEFINITION_CHOICE_STRUCTURE: {
        uint32_t count;
        perr = per_decode_length(s, &count);
        if (perr) return CMS_ERR;
        cms_array_t *arr = (cms_array_t*)d->alt_structure;
        if (arr) arr->count = (int32_t)count;
        for (uint32_t i = 0; i < count; i++) {
            if (!arr || !arr->elements || !arr->elements[i]) return CMS_ERR;
            int err = decode_struct_elem(s, (cms_data_definition_struct_elem_t*)arr->elements[i]);
            if (err) return err;
        }
        return CMS_OK;
    }

    /* [3..13] and [18..23] — NULL alternatives, no payload */
    case CMS_DATA_DEFINITION_CHOICE_BOOLEAN:
    case CMS_DATA_DEFINITION_CHOICE_INT8:
    case CMS_DATA_DEFINITION_CHOICE_INT16:
    case CMS_DATA_DEFINITION_CHOICE_INT32:
    case CMS_DATA_DEFINITION_CHOICE_INT64:
    case CMS_DATA_DEFINITION_CHOICE_INT8U:
    case CMS_DATA_DEFINITION_CHOICE_INT16U:
    case CMS_DATA_DEFINITION_CHOICE_INT32U:
    case CMS_DATA_DEFINITION_CHOICE_INT64U:
    case CMS_DATA_DEFINITION_CHOICE_FLOAT32:
    case CMS_DATA_DEFINITION_CHOICE_FLOAT64:
    case CMS_DATA_DEFINITION_CHOICE_UTC_TIME:
    case CMS_DATA_DEFINITION_CHOICE_BINARY_TIME:
    case CMS_DATA_DEFINITION_CHOICE_QUALITY:
    case CMS_DATA_DEFINITION_CHOICE_DBPOS:
    case CMS_DATA_DEFINITION_CHOICE_TCMD:
    case CMS_DATA_DEFINITION_CHOICE_CHECK:
        return CMS_OK;

    /* [14..17] — INTEGER max length */
    case CMS_DATA_DEFINITION_CHOICE_BIT_STRING:
        if (!d->alt_bit_string_len) return CMS_ERR;
        return cms_int32_decode_stream(s, d->alt_bit_string_len);

    case CMS_DATA_DEFINITION_CHOICE_OCTET_STRING:
        if (!d->alt_octet_string_len) return CMS_ERR;
        return cms_int32_decode_stream(s, d->alt_octet_string_len);

    case CMS_DATA_DEFINITION_CHOICE_VISIBLE_STRING:
        if (!d->alt_visible_string_len) return CMS_ERR;
        return cms_int32_decode_stream(s, d->alt_visible_string_len);

    case CMS_DATA_DEFINITION_CHOICE_UNICODE_STRING:
        if (!d->alt_unicode_string_len) return CMS_ERR;
        return cms_int32_decode_stream(s, d->alt_unicode_string_len);

    default:
        return CMS_ERR;
    }
}

int cms_data_definition_encode(const void *ptr, uint8_t *out_buf, int *out_len) {
    per_stream_t s;
    per_stream_init_write(&s, out_buf, (size_t)*out_len);
    int rc = cms_data_definition_encode_stream(&s, ptr);
    if (rc) return rc;
    *out_len = (int)per_stream_bytes_written(&s);
    return CMS_OK;
}

int cms_data_definition_decode(void *ptr, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_data_definition_decode_stream(&s, ptr);
}
