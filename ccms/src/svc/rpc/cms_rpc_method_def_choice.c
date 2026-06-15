#include "svc/rpc/cms_rpc_method_def_choice.h"
#include "per/cms_integer.h"

int cms_rpc_method_def_choice_encode_stream(per_stream_t *s, const cms_rpc_method_def_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    int sel = v->choice->value;
    per_error_t perr = per_encode_small_non_negative(s, (uint32_t)sel);
    if (perr) return CMS_ERR;

    switch (sel) {
    case CMS_RPC_METHOD_DEF_CHOICE_ERROR:
        if (!v->alt_error) return CMS_ERR;
        return cms_service_error_encode_stream(s, v->alt_error);
    case CMS_RPC_METHOD_DEF_CHOICE_METHOD:
        if (!v->alt_method) return CMS_ERR;
        return cms_rpc_method_def_encode_stream(s, v->alt_method);
    default:
        return CMS_ERR;
    }
}

int cms_rpc_method_def_choice_decode_stream(per_stream_t *s, cms_rpc_method_def_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    uint32_t sel;
    per_error_t perr = per_decode_small_non_negative(s, &sel);
    if (perr) return CMS_ERR;
    v->choice->value = (int)sel;

    switch (sel) {
    case CMS_RPC_METHOD_DEF_CHOICE_ERROR:
        if (!v->alt_error) return CMS_ERR;
        return cms_service_error_decode_stream(s, v->alt_error);
    case CMS_RPC_METHOD_DEF_CHOICE_METHOD:
        if (!v->alt_method) return CMS_ERR;
        return cms_rpc_method_def_decode_stream(s, v->alt_method);
    default:
        return CMS_ERR;
    }
}
