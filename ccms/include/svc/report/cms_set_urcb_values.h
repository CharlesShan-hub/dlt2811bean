#ifndef CMS_SET_URCB_VALUES_H
#define CMS_SET_URCB_VALUES_H

#include "svc/cms_svc.h"
#include "svc/report/cms_set_urcb_entry.h"
#include "svc/report/cms_set_urcb_result.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetURCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     urcb            [0] IMPLICIT SEQUENCE OF SetURCBEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *urcb; /* SEQUENCE OF SetURCBEntry */
} cms_set_urcb_values_request_t;

/*
 * ============================================================
 * SetURCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_set_urcb_values_response_t;

/*
 * ============================================================
 * SetURCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetURCBResult
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *result; /* SEQUENCE OF SetURCBResult */
} cms_set_urcb_values_error_t;

CMS_EXPORT int cms_set_urcb_values_request_encode(const cms_set_urcb_values_request_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_set_urcb_values_request_decode(cms_set_urcb_values_request_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

CMS_EXPORT int cms_set_urcb_values_response_encode(const cms_set_urcb_values_response_t *pdu, uint8_t **out_buf,
                                                   size_t *out_len);

CMS_EXPORT int cms_set_urcb_values_response_decode(cms_set_urcb_values_response_t *pdu, const uint8_t *in_buf,
                                                   int in_len);

CMS_EXPORT int cms_set_urcb_values_error_encode(const cms_set_urcb_values_error_t *pdu, uint8_t **out_buf,
                                                size_t *out_len);

CMS_EXPORT int cms_set_urcb_values_error_decode(cms_set_urcb_values_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
