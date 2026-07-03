#ifndef CMS_RPC_CALL_H
#define CMS_RPC_CALL_H

#include "svc/cms_svc.h"
#include "svc/rpc/cms_rpc_call_req_choice.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_service_error.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RpcCall-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     method          [0] IMPLICIT VisibleString,
 *     req             [1] IMPLICIT RpcCallReqChoice
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_uint8_array_t *method; /* VisibleString */
    cms_rpc_call_req_choice_t *req;
} cms_rpc_call_request_t;

/*
 * ============================================================
 * RpcCall-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     rspData         [0] IMPLICIT Data,
 *     nextCallID      [1] IMPLICIT OCTET STRING OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_data_t *rsp_data;
    cms_boolean_t *next_call_id_present;
    cms_uint8_array_t *next_call_id; /* OCTET STRING */
} cms_rpc_call_response_t;

/*
 * ============================================================
 * RpcCall-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_rpc_call_error_t;

CMS_EXPORT int cms_rpc_call_request_encode(const cms_rpc_call_request_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_rpc_call_request_decode(cms_rpc_call_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_rpc_call_response_encode(const cms_rpc_call_response_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_rpc_call_response_decode(cms_rpc_call_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_rpc_call_error_encode(const cms_rpc_call_error_t *pdu, uint8_t **out_buf, size_t *out_len);
CMS_EXPORT int cms_rpc_call_error_decode(cms_rpc_call_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
