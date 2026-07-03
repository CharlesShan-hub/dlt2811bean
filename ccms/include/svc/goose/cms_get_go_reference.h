#ifndef CMS_GET_GO_REFERENCE_H
#define CMS_GET_GO_REFERENCE_H

#include "svc/cms_svc.h"
#include "svc/goose/cms_go_ref_fc_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_int16u.h"
#include "data/scalar/cms_int32u.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetGoReference-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     memberOfs       [1] IMPLICIT SEQUENCE OF INT16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_object_reference_t *gocb_reference;
    cms_array_t *member_ofs; /* SEQUENCE OF INT16U */
} cms_get_go_reference_request_t;

/*
 * ============================================================
 * GetGoReference-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     gocbReference   [0] IMPLICIT ObjectReference,
 *     confRev         [1] IMPLICIT INT32U,
 *     datSet          [2] IMPLICIT ObjectReference,
 *     memberData      [3] IMPLICIT SEQUENCE OF GoRefFcEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_object_reference_t *gocb_reference;
    cms_int32u_t *conf_rev;
    cms_object_reference_t *dat_set;
    cms_array_t *member_data; /* SEQUENCE OF GoRefFcEntry */
} cms_get_go_reference_response_t;

/*
 * ============================================================
 * GetGoReference-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_get_go_reference_error_t;

CMS_EXPORT int cms_get_go_reference_request_encode(const cms_get_go_reference_request_t *pdu, uint8_t **out_buf,
                                                   size_t *out_len);

CMS_EXPORT int cms_get_go_reference_request_decode(cms_get_go_reference_request_t *pdu, const uint8_t *in_buf,
                                                   int in_len);

CMS_EXPORT int cms_get_go_reference_response_encode(const cms_get_go_reference_response_t *pdu, uint8_t **out_buf,
                                                    size_t *out_len);

CMS_EXPORT int cms_get_go_reference_response_decode(cms_get_go_reference_response_t *pdu, const uint8_t *in_buf,
                                                    int in_len);

CMS_EXPORT int cms_get_go_reference_error_encode(const cms_get_go_reference_error_t *pdu, uint8_t **out_buf,
                                                 size_t *out_len);

CMS_EXPORT int cms_get_go_reference_error_decode(cms_get_go_reference_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
