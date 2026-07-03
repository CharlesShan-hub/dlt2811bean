#ifndef CMS_RPC_METHOD_ENTRY_H
#define CMS_RPC_METHOD_ENTRY_H

#include "cms_types.h"
#include "data/choice/cms_data_definition.h"
#include "data/scalar/cms_int32u.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RpcMethodEntry ::= SEQUENCE {
 *     name        [0] IMPLICIT VisibleString,
 *     version     [1] IMPLICIT INT32U,
 *     timeout     [2] IMPLICIT INT32U,
 *     request     [3] IMPLICIT DataDefinition,
 *     response    [4] IMPLICIT DataDefinition
 * }
 *
 * Used by GetRpcInterfaceDefinition response.
 * ============================================================
 */
typedef struct {
    cms_uint8_array_t    *name;
    cms_int32u_t         *version;
    cms_int32u_t         *timeout;
    cms_data_definition_t *request;
    cms_data_definition_t *response;
} cms_rpc_method_entry_t;

int cms_rpc_method_entry_encode_stream(per_stream_t *s, const cms_rpc_method_entry_t *v);
int cms_rpc_method_entry_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
