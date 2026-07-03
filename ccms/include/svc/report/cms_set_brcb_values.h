#ifndef CMS_SET_BRCB_VALUES_H
#define CMS_SET_BRCB_VALUES_H

#include "svc/cms_svc.h"
#include "svc/report/cms_set_brcb_entry.h"
#include "svc/report/cms_set_brcb_result.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetBRCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     brcb            [0] IMPLICIT SEQUENCE OF SetBRCBEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *brcb; /* SEQUENCE OF SetBRCBEntry */
} cms_set_brcb_values_request_t;

/*
 * ============================================================
 * SetBRCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
} cms_set_brcb_values_response_t;

/*
 * ============================================================
 * SetBRCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetBRCBResult
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t *req_id;
    cms_array_t *result; /* SEQUENCE OF SetBRCBResult */
} cms_set_brcb_values_error_t;

CMS_EXPORT int cms_set_brcb_values_request_encode(const cms_set_brcb_values_request_t *pdu, uint8_t **out_buf,
                                                  size_t *out_len);

CMS_EXPORT int cms_set_brcb_values_request_decode(cms_set_brcb_values_request_t *pdu, const uint8_t *in_buf,
                                                  int in_len);

CMS_EXPORT int cms_set_brcb_values_response_encode(const cms_set_brcb_values_response_t *pdu, uint8_t **out_buf,
                                                   size_t *out_len);

CMS_EXPORT int cms_set_brcb_values_response_decode(cms_set_brcb_values_response_t *pdu, const uint8_t *in_buf,
                                                   int in_len);

CMS_EXPORT int cms_set_brcb_values_error_encode(const cms_set_brcb_values_error_t *pdu, uint8_t **out_buf,
                                                size_t *out_len);

CMS_EXPORT int cms_set_brcb_values_error_decode(cms_set_brcb_values_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
