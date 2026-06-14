#ifndef CMS_RPC_METHOD_DEF_CHOICE_H
#define CMS_RPC_METHOD_DEF_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/common/cms_service_error.h"
#include "svc/rpc/cms_rpc_method_def.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RpcMethodDefChoice ::= CHOICE {
 *     error       [0] IMPLICIT ServiceError,
 *     method      [1] IMPLICIT RpcMethodDef
 * }
 *
 * Used by GetRpcMethodDefinition response.
 * ============================================================
 */

#define CMS_RPC_METHOD_DEF_CHOICE_ERROR  0
#define CMS_RPC_METHOD_DEF_CHOICE_METHOD 1

typedef struct {
    cms_enumerated_t     *choice;
    cms_service_error_t  *alt_error;
    cms_rpc_method_def_t *alt_method;
} cms_rpc_method_def_choice_t;

int cms_rpc_method_def_choice_encode_stream(per_stream_t *s, const cms_rpc_method_def_choice_t *v);
int cms_rpc_method_def_choice_decode_stream(per_stream_t *s, cms_rpc_method_def_choice_t *v);

#ifdef __cplusplus
}
#endif

#endif
