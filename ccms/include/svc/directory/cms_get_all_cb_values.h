#ifndef CMS_GET_ALL_CB_VALUES_H
#define CMS_GET_ALL_CB_VALUES_H

#include "svc/cms_svc.h"
#include "svc/other/cms_reference_choice.h"
#include "svc/directory/cms_acsi_class.h"
#include "svc/directory/cms_cb_value_entry.h"
#include "data/common/cms_object_reference.h"
#include "data/common/cms_service_error.h"
#include "data/scalar/cms_boolean.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * GetAllCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     reference       [0] IMPLICIT ReferenceChoice,
 *     acsiClass       [1] IMPLICIT ACSIClass,
 *     referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_reference_choice_t *reference;
    cms_acsi_class_t *acsi_class;
    cms_boolean_t *ref_after_present;
    cms_object_reference_t *ref_after;
} cms_get_all_cb_values_request_t;

/*
 * ============================================================
 * GetAllCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     cbValue         [0] IMPLICIT SEQUENCE OF CBValueEntry,
 *     moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *cb_value;       /* SEQUENCE OF CBValueEntry */
    cms_boolean_t *more_follows; /* DEFAULT TRUE */
} cms_get_all_cb_values_response_t;

/*
 * ============================================================
 * GetAllCBValues-ErrorPDU ::= ServiceError
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_service_error_t *service_error;
} cms_get_all_cb_values_error_t;

CMS_EXPORT int cms_get_all_cb_values_request_encode(const cms_get_all_cb_values_request_t *pdu, uint8_t **out_buf,
                                                    size_t *out_len);

CMS_EXPORT int cms_get_all_cb_values_request_decode(cms_get_all_cb_values_request_t *pdu, const uint8_t *in_buf,
                                                    int in_len);

CMS_EXPORT int cms_get_all_cb_values_response_encode(const cms_get_all_cb_values_response_t *pdu, uint8_t **out_buf,
                                                     size_t *out_len);

CMS_EXPORT int cms_get_all_cb_values_response_decode(cms_get_all_cb_values_response_t *pdu, const uint8_t *in_buf,
                                                     int in_len);

CMS_EXPORT int cms_get_all_cb_values_error_encode(const cms_get_all_cb_values_error_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_get_all_cb_values_error_decode(cms_get_all_cb_values_error_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

#ifdef __cplusplus
}
#endif

#endif
