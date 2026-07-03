#include "svc/rpc/cms_rpc_method_def.h"
#include "data/choice/cms_data_definition.h"
#include "data/scalar/cms_int32u.h"

int cms_rpc_method_def_encode_stream(per_stream_t *s, const cms_rpc_method_def_t *v) {
    if (!v || !v->version || !v->timeout || !v->request || !v->response) return CMS_ERR;
    int err;

    /* 1. version — INT32U */
    err = cms_int32u_encode_stream(s, v->version);
    if (err) return err;

    /* 2. timeout — INT32U */
    err = cms_int32u_encode_stream(s, v->timeout);
    if (err) return err;

    /* 3. request — DataDefinition */
    err = cms_data_definition_encode_stream(s, v->request);
    if (err) return err;

    /* 4. response — DataDefinition */
    err = cms_data_definition_encode_stream(s, v->response);
    if (err) return err;

    return CMS_OK;
}

int cms_rpc_method_def_decode_stream(per_stream_t *s, void *ptr) {
    cms_rpc_method_def_t *v = (cms_rpc_method_def_t*)ptr;
    int err;

    /* 1. version */
    if (v && !v->version) return CMS_ERR;
    err = cms_int32u_decode_stream(s, v ? v->version : NULL);
    if (err) return err;

    /* 2. timeout */
    if (v && !v->timeout) return CMS_ERR;
    err = cms_int32u_decode_stream(s, v ? v->timeout : NULL);
    if (err) return err;

    /* 3. request */
    if (v && !v->request) return CMS_ERR;
    err = cms_data_definition_decode_stream(s, v ? v->request : NULL);
    if (err) return err;

    /* 4. response */
    if (v && !v->response) return CMS_ERR;
    err = cms_data_definition_decode_stream(s, v ? v->response : NULL);
    if (err) return err;

    return CMS_OK;
}
