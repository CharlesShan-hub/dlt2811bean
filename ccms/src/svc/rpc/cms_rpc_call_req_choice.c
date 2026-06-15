#include "svc/rpc/cms_rpc_call_req_choice.h"
#include "per/cms_integer.h"
#include "data/string/cms_octet_string.h"

int cms_rpc_call_req_choice_encode_stream(per_stream_t *s, const cms_rpc_call_req_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    int sel = v->choice->value;
    per_error_t perr = per_encode_small_non_negative(s, (uint32_t)sel);
    if (perr) return CMS_ERR;

    switch (sel) {
    case CMS_RPC_CALL_REQ_CHOICE_REQ_DATA:
        if (!v->alt_req_data) return CMS_ERR;
        return cms_data_encode_stream(s, v->alt_req_data);
    case CMS_RPC_CALL_REQ_CHOICE_CALL_ID:
        if (!v->alt_call_id) return CMS_ERR;
        return cms_octet_string_encode_stream(s, v->alt_call_id, UINT32_MAX);
    default:
        return CMS_ERR;
    }
}

int cms_rpc_call_req_choice_decode_stream(per_stream_t *s, cms_rpc_call_req_choice_t *v) {
    if (!v || !v->choice) return CMS_ERR;
    uint32_t sel;
    per_error_t perr = per_decode_small_non_negative(s, &sel);
    if (perr) return CMS_ERR;
    v->choice->value = (int)sel;

    switch (sel) {
    case CMS_RPC_CALL_REQ_CHOICE_REQ_DATA:
        if (!v->alt_req_data) return CMS_ERR;
        return cms_data_decode_stream(s, v->alt_req_data);
    case CMS_RPC_CALL_REQ_CHOICE_CALL_ID:
        if (!v->alt_call_id) return CMS_ERR;
        return cms_octet_string_decode_stream(s, v->alt_call_id, UINT32_MAX);
    default:
        return CMS_ERR;
    }
}
