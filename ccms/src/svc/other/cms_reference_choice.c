#include "svc/other/cms_reference_choice.h"
#include "per/cms_integer.h"
#include "data/common/cms_object_name.h"
#include "data/common/cms_object_reference.h"

int cms_reference_choice_encode_stream(per_stream_t *s, const cms_reference_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    int sel = v->choice->value;

    per_error_t perr = per_encode_small_non_negative(s, (uint32_t)sel);
    if (perr) return CMS_ERR;

    switch (sel) {
    case CMS_REFERENCE_CHOICE_LD_NAME:
        if (!v->alt_ld_name) return CMS_ERR;
        return cms_object_name_encode_stream(s, v->alt_ld_name);
    case CMS_REFERENCE_CHOICE_LN_REFERENCE:
        if (!v->alt_ln_reference) return CMS_ERR;
        return cms_object_reference_encode_stream(s, v->alt_ln_reference);
    default:
        return CMS_ERR;
    }
}

int cms_reference_choice_decode_stream(per_stream_t *s, void *ptr) {
    cms_reference_choice_t *v = (cms_reference_choice_t*)ptr;

    uint32_t sel;
    per_error_t perr = per_decode_small_non_negative(s, &sel);
    if (perr) return CMS_ERR;
    if (sel > 1) return CMS_ERR;
    if (v) v->choice->value = (int)sel;

    switch (sel) {
    case CMS_REFERENCE_CHOICE_LD_NAME:
        if (v && !v->alt_ld_name) return CMS_ERR;
        return cms_object_name_decode_stream(s, v ? v->alt_ld_name : NULL);
    case CMS_REFERENCE_CHOICE_LN_REFERENCE:
        if (v && !v->alt_ln_reference) return CMS_ERR;
        return cms_object_reference_decode_stream(s, v ? v->alt_ln_reference : NULL);
    default:
        return CMS_ERR;
    }
}

int cms_reference_choice_encode(const cms_reference_choice_t *v, uint8_t **out_buf, size_t *out_len) {
    per_stream_t s;
    per_error_t err = per_stream_init_write(&s, 64);
    if (err) return (int)err;
    int rc = cms_reference_choice_encode_stream(&s, v);
    if (rc) { per_stream_free(&s); return rc; }
    *out_buf = per_stream_detach(&s, out_len);
    return CMS_OK;
}

int cms_reference_choice_decode(cms_reference_choice_t *v, const uint8_t *in_buf, int in_len) {
    per_stream_t s;
    per_stream_init_read(&s, in_buf, (size_t)in_len);
    return cms_reference_choice_decode_stream(&s, v);
}
