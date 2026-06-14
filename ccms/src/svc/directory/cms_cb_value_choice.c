#include "svc/directory/cms_cb_value_choice.h"
#include "per/cms_integer.h"

int cms_cb_value_choice_encode_stream(per_stream_t *s, const cms_cb_value_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    int sel = v->choice->value;
    per_error_t perr = per_encode_small_non_negative(s, (uint32_t)sel);
    if (perr) return CMS_ERR;
    switch (sel) {
    case CMS_CB_VALUE_CHOICE_BRCB:
        if (!v->alt_brcb) return CMS_ERR;
        return cms_brcb_encode_stream(s, v->alt_brcb);
    case CMS_CB_VALUE_CHOICE_URCB:
        if (!v->alt_urcb) return CMS_ERR;
        return cms_urcb_encode_stream(s, v->alt_urcb);
    case CMS_CB_VALUE_CHOICE_LCB:
        if (!v->alt_lcb) return CMS_ERR;
        return cms_lcb_encode_stream(s, v->alt_lcb);
    case CMS_CB_VALUE_CHOICE_SGECB:
        if (!v->alt_sgecb) return CMS_ERR;
        return cms_sgcb_encode_stream(s, v->alt_sgecb);
    case CMS_CB_VALUE_CHOICE_GOCB:
        if (!v->alt_gocb) return CMS_ERR;
        return cms_go_cb_encode_stream(s, v->alt_gocb);
    case CMS_CB_VALUE_CHOICE_MSVCB:
        if (!v->alt_msvcb) return CMS_ERR;
        return cms_msvcb_encode_stream(s, v->alt_msvcb);
    default:
        return CMS_ERR;
    }
}

int cms_cb_value_choice_decode_stream(per_stream_t *s, cms_cb_value_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    uint32_t sel;
    per_error_t perr = per_decode_small_non_negative(s, &sel);
    if (perr) return CMS_ERR;
    v->choice->value = (int)sel;
    switch (sel) {
    case CMS_CB_VALUE_CHOICE_BRCB:
        if (!v->alt_brcb) return CMS_ERR;
        return cms_brcb_decode_stream(s, v->alt_brcb);
    case CMS_CB_VALUE_CHOICE_URCB:
        if (!v->alt_urcb) return CMS_ERR;
        return cms_urcb_decode_stream(s, v->alt_urcb);
    case CMS_CB_VALUE_CHOICE_LCB:
        if (!v->alt_lcb) return CMS_ERR;
        return cms_lcb_decode_stream(s, v->alt_lcb);
    case CMS_CB_VALUE_CHOICE_SGECB:
        if (!v->alt_sgecb) return CMS_ERR;
        return cms_sgcb_decode_stream(s, v->alt_sgecb);
    case CMS_CB_VALUE_CHOICE_GOCB:
        if (!v->alt_gocb) return CMS_ERR;
        return cms_go_cb_decode_stream(s, v->alt_gocb);
    case CMS_CB_VALUE_CHOICE_MSVCB:
        if (!v->alt_msvcb) return CMS_ERR;
        return cms_msvcb_decode_stream(s, v->alt_msvcb);
    default:
        return CMS_ERR;
    }
}
