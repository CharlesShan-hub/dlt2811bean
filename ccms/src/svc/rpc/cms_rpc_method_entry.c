#include "svc/rpc/cms_rpc_method_entry.h"
#include "data/choice/cms_data_definition.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_visible_string.h"

int cms_rpc_method_entry_encode_stream(per_stream_t *s, const cms_rpc_method_entry_t *v) {
    if (!v || !v->name || !v->version || !v->timeout || !v->request || !v->response) return CMS_ERR;
    int err;

    /* 1. name — VisibleString */
    err = cms_visible_string_encode_stream(s, v->name, UINT32_MAX);
    if (err) return err;

    /* 2. version — INT32U */
    err = cms_int32u_encode_stream(s, v->version);
    if (err) return err;

    /* 3. timeout — INT32U */
    err = cms_int32u_encode_stream(s, v->timeout);
    if (err) return err;

    /* 4. request — DataDefinition */
    err = cms_data_definition_encode_stream(s, v->request);
    if (err) return err;

    /* 5. response — DataDefinition */
    err = cms_data_definition_encode_stream(s, v->response);
    if (err) return err;

    return CMS_OK;
}

int cms_rpc_method_entry_decode_stream(per_stream_t *s, void *ptr) {
    cms_rpc_method_entry_t *v = (cms_rpc_method_entry_t*)ptr;
    int err;

    /* 1. name */
    if (v && !v->name) return CMS_ERR;
    err = cms_visible_string_decode_stream(s, v ? v->name : NULL, UINT32_MAX);
    if (err) return err;

    /* 2. version */
    if (v && !v->version) return CMS_ERR;
    err = cms_int32u_decode_stream(s, v ? v->version : NULL);
    if (err) return err;

    /* 3. timeout */
    if (v && !v->timeout) return CMS_ERR;
    err = cms_int32u_decode_stream(s, v ? v->timeout : NULL);
    if (err) return err;

    /* 4. request */
    if (v && !v->request) return CMS_ERR;
    err = cms_data_definition_decode_stream(s, v ? v->request : NULL);
    if (err) return err;

    /* 5. response */
    if (v && !v->response) return CMS_ERR;
    err = cms_data_definition_decode_stream(s, v ? v->response : NULL);
    if (err) return err;

    return CMS_OK;
}
