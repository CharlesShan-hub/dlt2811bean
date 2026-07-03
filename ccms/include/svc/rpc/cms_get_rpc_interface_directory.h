#ifndef CMS_GET_RPC_INTERFACE_DIRECTORY_H
#define CMS_GET_RPC_INTERFACE_DIRECTORY_H

#include "svc/cms_svc.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     referenceAfter  [0] IMPLICIT VisibleString OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_boolean_t *ref_after_present;
    cms_uint8_array_t *ref_after; /* VisibleString */
} cms_get_rpc_interface_directory_request_t;

/*
 * ============================================================
 * GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT SEQUENCE OF VisibleString,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *reference; /* SEQUENCE OF VisibleString */
    cms_boolean_t *more_follows;
} cms_get_rpc_interface_directory_response_t;

/*
 * ============================================================
 * GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_get_rpc_interface_directory_error_t;

CMS_EXPORT int cms_get_rpc_interface_directory_request_encode(const cms_get_rpc_interface_directory_request_t *pdu,
                                                              uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_rpc_interface_directory_request_decode(cms_get_rpc_interface_directory_request_t *pdu,
                                                              const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_rpc_interface_directory_response_encode(const cms_get_rpc_interface_directory_response_t *pdu,
                                                               uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_rpc_interface_directory_response_decode(cms_get_rpc_interface_directory_response_t *pdu,
                                                               const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_get_rpc_interface_directory_error_encode(const cms_get_rpc_interface_directory_error_t *pdu,
                                                            uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_get_rpc_interface_directory_error_decode(cms_get_rpc_interface_directory_error_t *pdu,
                                                            const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
