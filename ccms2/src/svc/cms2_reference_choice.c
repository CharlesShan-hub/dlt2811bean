#include "svc/directory/cms2_reference_choice.h"
#include <string.h>

int cms2_reference_choice_encode(per_stream_t *s, const cms2_reference_choice_t *c) {
    if (!c->choice) return CMS2_ERR;
    int32_t ref_choice = *(const int32_t*)c->choice;

    /* Encode CHOICE index as small non-negative integer */
    per_error_t err = per_encode_small_non_negative(s, (uint32_t)ref_choice);
    if (err) return CMS2_ERR;

    /* Encode the selected alternative */
    if (ref_choice == CMS2_REF_CHOICE_LD_NAME) {
        if (!c->ld_name) return CMS2_ERR;
        const uint8_t *vptr = *(const uint8_t *const*)c->ld_name;
        if (vptr) {
            err = per_encode_visible_string(s, vptr, 64);  /* ObjectName max 64 */
            if (err) return CMS2_ERR;
        }
    } else {
        if (!c->ln_reference) return CMS2_ERR;
        const uint8_t *vptr = *(const uint8_t *const*)c->ln_reference;
        if (vptr) {
            err = per_encode_visible_string(s, vptr, 129);  /* ObjectReference max 129 */
            if (err) return CMS2_ERR;
        }
    }
    return CMS2_OK;
}

int cms2_reference_choice_decode(per_stream_t *s, cms2_reference_choice_t *c) {
    /* Decode CHOICE index */
    uint32_t ref_choice;
    per_error_t err = per_decode_small_non_negative(s, &ref_choice);
    if (err) return CMS2_ERR;

    if (c->choice) *(int32_t*)c->choice = (int32_t)ref_choice;

    if (ref_choice == CMS2_REF_CHOICE_LD_NAME) {
        if (!c->ld_name) return CMS2_ERR;
        uint8_t *vptr = *(uint8_t **)c->ld_name;
        if (!vptr) return CMS2_ERR;
        err = per_decode_visible_string(s, vptr, 64);
        if (err) return CMS2_ERR;
        *(int32_t*)((uint8_t*)c->ld_name + 8) = (int32_t)strlen((const char*)vptr);
    } else {
        if (!c->ln_reference) return CMS2_ERR;
        uint8_t *vptr = *(uint8_t **)c->ln_reference;
        if (!vptr) return CMS2_ERR;
        err = per_decode_visible_string(s, vptr, 129);
        if (err) return CMS2_ERR;
        *(int32_t*)((uint8_t*)c->ln_reference + 8) = (int32_t)strlen((const char*)vptr);
    }
    return CMS2_OK;
}
