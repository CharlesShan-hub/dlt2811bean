#ifndef CMS_SELECT_H
#define CMS_SELECT_H

#include "svc/cms_svc.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Select-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *reference;
} cms_select_request_t;

/*
 * Select-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *reference;
} cms_select_response_t;

/*
 * Select-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ObjectReference
 * }
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *reference;
} cms_select_error_t;

CMS_EXPORT int cms_select_request_encode(const cms_select_request_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_select_request_decode(cms_select_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_select_response_encode(const cms_select_response_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_select_response_decode(cms_select_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_select_error_encode(const cms_select_error_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_select_error_decode(cms_select_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
