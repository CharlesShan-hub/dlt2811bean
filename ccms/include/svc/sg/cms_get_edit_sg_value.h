#ifndef CMS_GET_EDIT_SG_VALUE_H
#define CMS_GET_EDIT_SG_VALUE_H

#include "svc/cms_svc.h"
#include "svc/sg/cms_sg_ref_fc_entry.h"
#include "data/choice/cms_data.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetEditSGValue-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     data            [0] IMPLICIT SEQUENCE OF SGRefFcEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *data;          /* SEQUENCE OF SGRefFcEntry */
} cms_get_edit_sg_value_request_t;

/*
 * ============================================================
 * GetEditSGValue-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     value           [0] IMPLICIT SEQUENCE OF Data,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *value;         /* SEQUENCE OF Data */
    cms_boolean_t   *more_follows;  /* DEFAULT TRUE */
} cms_get_edit_sg_value_response_t;

/*
 * ============================================================
 * GetEditSGValue-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_service_error_t   *service_error;
} cms_get_edit_sg_value_error_t;

CMS_EXPORT int cms_get_edit_sg_value_request_encode(
    const cms_get_edit_sg_value_request_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_get_edit_sg_value_request_decode(
    cms_get_edit_sg_value_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_edit_sg_value_response_encode(
    const cms_get_edit_sg_value_response_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_get_edit_sg_value_response_decode(
    cms_get_edit_sg_value_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_get_edit_sg_value_error_encode(
    const cms_get_edit_sg_value_error_t *pdu,
    uint8_t **out_buf, size_t *out_len
);

CMS_EXPORT int cms_get_edit_sg_value_error_decode(
    cms_get_edit_sg_value_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
