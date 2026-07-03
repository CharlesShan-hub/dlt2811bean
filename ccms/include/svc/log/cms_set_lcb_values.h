#ifndef CMS_SET_LCB_VALUES_H
#define CMS_SET_LCB_VALUES_H

#include "svc/cms_svc.h"
#include "svc/log/cms_set_lcb_entry.h"
#include "svc/log/cms_set_lcb_result.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetLCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     lcb             [0] IMPLICIT SEQUENCE OF SetLCBEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *lcb; /* SEQUENCE OF SetLCBEntry */
} cms_set_lcb_values_request_t;

/*
 * ============================================================
 * SetLCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_set_lcb_values_response_t;

/*
 * ============================================================
 * SetLCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetLCBResult
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *result; /* SEQUENCE OF SetLCBResult */
} cms_set_lcb_values_error_t;

CMS_EXPORT int cms_set_lcb_values_request_encode(const cms_set_lcb_values_request_t *pdu, uint8_t **out_buf,
                                                 size_t *out_len);

CMS_EXPORT int cms_set_lcb_values_request_decode(cms_set_lcb_values_request_t *pdu, const uint8_t *in_buf, int in_len);

CMS_EXPORT int cms_set_lcb_values_response_encode(const cms_set_lcb_values_response_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_set_lcb_values_response_decode(cms_set_lcb_values_response_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

CMS_EXPORT int cms_set_lcb_values_error_encode(const cms_set_lcb_values_error_t *pdu, uint8_t **out_buf,
                                               size_t *out_len);

CMS_EXPORT int cms_set_lcb_values_error_decode(cms_set_lcb_values_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
