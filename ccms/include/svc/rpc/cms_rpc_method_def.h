#ifndef CMS_RPC_METHOD_DEF_H
#define CMS_RPC_METHOD_DEF_H

#include "cms_types.h"
#include "data/choice/cms_data_definition.h"
#include "data/scalar/cms_int32u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RpcMethodDef ::= SEQUENCE {
 *     version     [0] IMPLICIT INT32U,
 *     timeout     [1] IMPLICIT INT32U,
 *     request     [2] IMPLICIT DataDefinition,
 *     response    [3] IMPLICIT DataDefinition
 * }
 *
 * Used by GetRpcMethodDefinition response (inside CHOICE).
 * ============================================================
 */
typedef struct {
    cms_int32u_t *version;
    cms_int32u_t *timeout;
    cms_data_definition_t *request;
    cms_data_definition_t *response;
} cms_rpc_method_def_t;

int cms_rpc_method_def_encode_stream(per_stream_t *s, const cms_rpc_method_def_t *v);
int cms_rpc_method_def_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
