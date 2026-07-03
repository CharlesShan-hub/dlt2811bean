#ifndef CMS_RPC_CALL_REQ_CHOICE_H
#define CMS_RPC_CALL_REQ_CHOICE_H

#include "cms_types.h"
#include "per/cms_stream.h"
#include "data/enum/cms_enumerated.h"
#include "data/choice/cms_data.h"
#include "data/string/cms_uint8_array.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * RpcCallReqChoice ::= CHOICE {
 *     reqData     [0] IMPLICIT Data,
 *     callID      [1] IMPLICIT OCTET STRING
 * }
 *
 * Used by RpcCall request.
 * ============================================================
 */

#define CMS_RPC_CALL_REQ_CHOICE_REQ_DATA  0
#define CMS_RPC_CALL_REQ_CHOICE_CALL_ID   1

typedef struct {
    cms_enumerated_t   *choice;
    cms_data_t         *alt_req_data;
    cms_uint8_array_t  *alt_call_id;
} cms_rpc_call_req_choice_t;

int cms_rpc_call_req_choice_encode_stream(per_stream_t *s, const cms_rpc_call_req_choice_t *v);
int cms_rpc_call_req_choice_decode_stream(per_stream_t *s, void *ptr);

#ifdef __cplusplus
}
#endif

#endif
