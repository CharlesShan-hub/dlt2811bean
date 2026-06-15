#include "svc/goose/cms_set_go_cb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"

int cms_set_go_cb_entry_encode_stream(per_stream_t *s, const cms_set_go_cb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 2. goEna — BOOLEAN OPTIONAL */
    {
        int present = (v->go_ena_present && v->go_ena_present->value) && v->go_ena;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_boolean_encode_stream(s, v->go_ena);
            if (err) return err;
        }
    }

    /* 3. goID — VisibleString(129) OPTIONAL */
    {
        int present = (v->go_id_present && v->go_id_present->value) && v->go_id;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_visible_string_encode_stream(s, v->go_id, 129);
            if (err) return err;
        }
    }

    /* 4. datSet — ObjectReference OPTIONAL */
    {
        int present = (v->dat_set_present && v->dat_set_present->value) && v->dat_set;
        cms_boolean_t bit = { .value = present };
        err = cms_boolean_encode_stream(s, &bit);
        if (err) return err;
        if (present) {
            err = cms_object_reference_encode_stream(s, v->dat_set);
            if (err) return err;
        }
    }

    return CMS_OK;
}

int cms_set_go_cb_entry_decode_stream(per_stream_t *s, cms_set_go_cb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 1. reference */
    err = cms_object_reference_decode_stream(s, v->reference);
    if (err) return err;

    /* 2. goEna OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->go_ena_present) v->go_ena_present->value = bit.value;
        if (bit.value) {
            if (!v->go_ena) return CMS_ERR;
            err = cms_boolean_decode_stream(s, v->go_ena);
            if (err) return err;
        }
    }

    /* 3. goID OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->go_id_present) v->go_id_present->value = bit.value;
        if (bit.value) {
            if (!v->go_id) return CMS_ERR;
            err = cms_visible_string_decode_stream(s, v->go_id, 129);
            if (err) return err;
        }
    }

    /* 4. datSet OPTIONAL */
    {
        cms_boolean_t bit = {0};
        err = cms_boolean_decode_stream(s, &bit);
        if (err) return err;
        if (v->dat_set_present) v->dat_set_present->value = bit.value;
        if (bit.value) {
            if (!v->dat_set) return CMS_ERR;
            err = cms_object_reference_decode_stream(s, v->dat_set);
            if (err) return err;
        }
    }

    return CMS_OK;
}
