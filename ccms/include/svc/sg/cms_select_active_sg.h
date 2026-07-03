#ifndef CMS_SELECT_ACTIVE_SG_H
#define CMS_SELECT_ACTIVE_SG_H

#include "svc/cms_svc.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int8u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SelectActiveSG-RequestPDU ::= SEQUENCE {
 *     reqId               Int16U,
 *     sgcbReference       [0] IMPLICIT ObjectReference,
 *     settingGroupNumber  [1] IMPLICIT INT8U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_object_reference_t *sgcb_reference;
    cms_int8u_t *setting_group_number;
} cms_select_active_sg_request_t;

/*
 * ============================================================
 * SelectActiveSG-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_select_active_sg_response_t;

/*
 * ============================================================
 * SelectActiveSG-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_select_active_sg_error_t;

CMS_EXPORT int cms_select_active_sg_request_encode(const cms_select_active_sg_request_t *pdu, uint8_t **out_buf,
                                                   size_t *out_len);

CMS_EXPORT int cms_select_active_sg_request_decode(cms_select_active_sg_request_t *pdu, const uint8_t *in_buf,
                                                   int in_len);

CMS_EXPORT int cms_select_active_sg_response_encode(const cms_select_active_sg_response_t *pdu, uint8_t **out_buf,
                                                    size_t *out_len);

CMS_EXPORT int cms_select_active_sg_response_decode(cms_select_active_sg_response_t *pdu, const uint8_t *in_buf,
                                                    int in_len);

CMS_EXPORT int cms_select_active_sg_error_encode(const cms_select_active_sg_error_t *pdu, uint8_t **out_buf,
                                                 size_t *out_len);

CMS_EXPORT int cms_select_active_sg_error_decode(cms_select_active_sg_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
