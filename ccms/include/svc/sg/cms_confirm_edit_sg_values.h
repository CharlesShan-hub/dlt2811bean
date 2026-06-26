#ifndef CMS_CONFIRM_EDIT_SG_VALUES_H
#define CMS_CONFIRM_EDIT_SG_VALUES_H

#include "svc/cms_svc.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * ConfirmEditSGValues-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     sgcbReference       [0] IMPLICIT ObjectReference
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t           *req_id;
    cms_object_reference_t *sgcb_reference;
} cms_confirm_edit_sg_values_request_t;

/*
 * ============================================================
 * ConfirmEditSGValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_confirm_edit_sg_values_response_t;

/*
 * ============================================================
 * ConfirmEditSGValues-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_confirm_edit_sg_values_error_t;

CMS_EXPORT int cms_confirm_edit_sg_values_request_encode(
    const cms_confirm_edit_sg_values_request_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_confirm_edit_sg_values_request_decode(
    cms_confirm_edit_sg_values_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_confirm_edit_sg_values_response_encode(
    const cms_confirm_edit_sg_values_response_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_confirm_edit_sg_values_response_decode(
    cms_confirm_edit_sg_values_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_confirm_edit_sg_values_error_encode(
    const cms_confirm_edit_sg_values_error_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_confirm_edit_sg_values_error_decode(
    cms_confirm_edit_sg_values_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
