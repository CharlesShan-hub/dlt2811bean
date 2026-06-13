#ifndef CMS_SET_MSVCB_VALUES_H
#define CMS_SET_MSVCB_VALUES_H

#include "svc/cms_svc.h"
#include "svc/msv/cms_set_msvcb_entry.h"
#include "svc/msv/cms_set_msvcb_result.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * SetMSVCBValues-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     msvcb           [0] IMPLICIT SEQUENCE OF SetMSVCBEntry
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
    cms_array_t     *msvcb;         /* SEQUENCE OF SetMSVCBEntry */
} cms_set_msvcb_values_request_t;

/*
 * ============================================================
 * SetMSVCBValues-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_set_msvcb_values_response_t;

/*
 * ============================================================
 * SetMSVCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     result          [0] IMPLICIT SEQUENCE OF SetMSVCBResult
 * }
 * ============================================================
 */
typedef struct {
    cms_req_id_t          *req_id;
    cms_array_t           *result;     /* SEQUENCE OF SetMSVCBResult */
} cms_set_msvcb_values_error_t;

CMS_EXPORT int cms_set_msvcb_values_request_encode(
    const cms_set_msvcb_values_request_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_set_msvcb_values_request_decode(
    cms_set_msvcb_values_request_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_msvcb_values_response_encode(
    const cms_set_msvcb_values_response_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_set_msvcb_values_response_decode(
    cms_set_msvcb_values_response_t *pdu,
    const uint8_t *in_buf, int in_len
);

CMS_EXPORT int cms_set_msvcb_values_error_encode(
    const cms_set_msvcb_values_error_t *pdu,
    uint8_t *out_buf, int *out_len
);

CMS_EXPORT int cms_set_msvcb_values_error_decode(
    cms_set_msvcb_values_error_t *pdu,
    const uint8_t *in_buf, int in_len
);

#ifdef __cplusplus
}
#endif

#endif
