#include "svc/goose/cms_set_go_cb_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_visible_string.h"
#include "per/cms_sequence.h"

int cms_set_go_cb_entry_encode_stream(per_stream_t *s, const cms_set_go_cb_entry_t *v) {
    if (!v || !v->reference) return CMS_ERR;
    int err;

    /* 0. reference — ObjectReference */
    err = cms_object_reference_encode_stream(s, v->reference);
    if (err) return err;

    /* 1. OPTIONAL bitmap (3 fields: goEna, goID, datSet) */
    bool opt_present[3] = {
        (v->go_ena_present && v->go_ena_present->value) && v->go_ena,
        (v->go_id_present && v->go_id_present->value) && v->go_id,
        (v->dat_set_present && v->dat_set_present->value) && v->dat_set
    };
    err = (int)per_encode_optional_bitmap(s, opt_present, 3);
    if (err) return err;

    /* 2. goEna — BOOLEAN OPTIONAL (bitmap[0]) */
    if (opt_present[0]) {
        err = cms_boolean_encode_stream(s, v->go_ena);
        if (err) return err;
    }

    /* 3. goID — VisibleString(129) OPTIONAL (bitmap[1]) */
    if (opt_present[1]) {
        err = cms_visible_string_encode_stream(s, v->go_id, 129);
        if (err) return err;
    }

    /* 4. datSet — ObjectReference OPTIONAL (bitmap[2]) */
    if (opt_present[2]) {
        err = cms_object_reference_encode_stream(s, v->dat_set);
        if (err) return err;
    }

    return CMS_OK;
}

int cms_set_go_cb_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_set_go_cb_entry_t *v = (cms_set_go_cb_entry_t*)ptr;
    int err;

    /* 0. reference */
    if (v && !v->reference) return CMS_ERR;
    err = cms_object_reference_decode_stream(s, v ? v->reference : NULL);
    if (err) return err;

    /* 1. OPTIONAL bitmap (3 fields) */
    bool opt_present[3];
    err = (int)per_decode_optional_bitmap(s, opt_present, 3);
    if (err) return err;
    if (v) {
        if (v->go_ena_present)  v->go_ena_present->value  = opt_present[0];
        if (v->go_id_present)   v->go_id_present->value   = opt_present[1];
        if (v->dat_set_present) v->dat_set_present->value = opt_present[2];
    }

    /* 2. goEna OPTIONAL */  if (opt_present[0]) { if (v && !v->go_ena) return CMS_ERR; err = cms_boolean_decode_stream(s, v ? v->go_ena : NULL); if (err) return err; }
    /* 3. goID OPTIONAL */   if (opt_present[1]) { if (v && !v->go_id) return CMS_ERR; err = cms_visible_string_decode_stream(s, v ? v->go_id : NULL, 129); if (err) return err; }
    /* 4. datSet OPTIONAL */ if (opt_present[2]) { if (v && !v->dat_set) return CMS_ERR; err = cms_object_reference_decode_stream(s, v ? v->dat_set : NULL); if (err) return err; }

    return CMS_OK;
}
