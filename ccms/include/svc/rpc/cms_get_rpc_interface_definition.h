#ifndef CMS_GET_RPC_INTERFACE_DEFINITION_H
#define CMS_GET_RPC_INTERFACE_DEFINITION_H

#include "svc/cms_svc.h"
#include "svc/rpc/cms_rpc_method_entry.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     interface       [0] IMPLICIT VisibleString,
 *     referenceAfter  [1] IMPLICIT VisibleString OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t        *req_id;
    cms_uint8_array_t   *interface_name;  /* VisibleString */
    cms_boolean_t       *ref_after_present;
    cms_uint8_array_t   *ref_after;       /* VisibleString */
} cms_get_rpc_interface_definition_request_t;

/*
 * ============================================================
 * GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     method          [0] IMPLICIT SEQUENCE OF RpcMethodEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *method;         /* SEQUENCE OF RpcMethodEntry */
    cms_boolean_t   *more_follows;
} cms_get_rpc_interface_definition_response_t;

/*
 * ============================================================
 * GetRpcInterfaceDefinition-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_rpc_interface_definition_error_t;

CMS_EXPORT int cms_get_rpc_interface_definition_request_encode(const cms_get_rpc_interface_definition_request_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_rpc_interface_definition_request_decode(cms_get_rpc_interface_definition_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_rpc_interface_definition_response_encode(const cms_get_rpc_interface_definition_response_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_rpc_interface_definition_response_decode(cms_get_rpc_interface_definition_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_rpc_interface_definition_error_encode(const cms_get_rpc_interface_definition_error_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_rpc_interface_definition_error_decode(cms_get_rpc_interface_definition_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
