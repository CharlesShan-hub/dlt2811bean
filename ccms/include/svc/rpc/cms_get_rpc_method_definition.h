#ifndef CMS_GET_RPC_METHOD_DEFINITION_H
#define CMS_GET_RPC_METHOD_DEFINITION_H

#include "svc/cms_svc.h"
#include "svc/rpc/cms_rpc_method_def_choice.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetRpcMethodDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t        *req_id;
    cms_array_t         *reference;      /* SEQUENCE OF VisibleString */
} cms_get_rpc_method_definition_request_t;

/*
 * ============================================================
 * GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF RpcMethodDefChoice,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *reference;      /* SEQUENCE OF RpcMethodDefChoice */
    cms_boolean_t   *more_follows;
} cms_get_rpc_method_definition_response_t;

/*
 * ============================================================
 * GetRpcMethodDefinition-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_rpc_method_definition_error_t;

CMS_EXPORT int cms_get_rpc_method_definition_request_encode(const cms_get_rpc_method_definition_request_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_get_rpc_method_definition_request_decode(cms_get_rpc_method_definition_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_rpc_method_definition_response_encode(const cms_get_rpc_method_definition_response_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_get_rpc_method_definition_response_decode(cms_get_rpc_method_definition_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_rpc_method_definition_error_encode(const cms_get_rpc_method_definition_error_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_get_rpc_method_definition_error_decode(cms_get_rpc_method_definition_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
