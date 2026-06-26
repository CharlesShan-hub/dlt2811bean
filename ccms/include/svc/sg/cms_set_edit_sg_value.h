#ifndef CMS_SET_EDIT_SG_VALUE_H
#define CMS_SET_EDIT_SG_VALUE_H

#include "svc/cms_svc.h"
#include "svc/sg/cms_sg_ref_value_entry.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetEditSGValue-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF SGRefValueEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *data;          /* SEQUENCE OF SGRefValueEntry */
} cms_set_edit_sg_value_request_t;

/*
 * ============================================================
 * SetEditSGValue-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_set_edit_sg_value_response_t;

/*
 * ============================================================
 * SetEditSGValue-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF ServiceError
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_array_t           *result;        /* SEQUENCE OF ServiceError */
} cms_set_edit_sg_value_error_t;

CMS_EXPORT int cms_set_edit_sg_value_request_encode(
    const cms_set_edit_sg_value_request_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_edit_sg_value_request_decode(
    cms_set_edit_sg_value_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_edit_sg_value_response_encode(
    const cms_set_edit_sg_value_response_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_edit_sg_value_response_decode(
    cms_set_edit_sg_value_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_edit_sg_value_error_encode(
    const cms_set_edit_sg_value_error_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_set_edit_sg_value_error_decode(
    cms_set_edit_sg_value_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
