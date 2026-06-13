#ifndef CMS_TEST_H
#define CMS_TEST_H

#include "svc/cms_svc.h"
#include "data/common/cms_service_error.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * ============================================================
 * Test (Service Code 0xA1)
 *
 * This service has no service-specific fields.
 * Both request and response carry only reqId (Int16U).
 * ============================================================
 */

/*
 * Test-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 * (FL = 0, no additional fields)
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_test_request_t;

/*
 * Test-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 */
typedef struct {
    cms_req_id_t    *req_id;
} cms_test_response_t;

/*
 * Test-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U
 * }
 */
typedef struct {
    cms_req_id_t          *req_id;
} cms_test_error_t;

CMS_EXPORT int cms_test_request_encode(const cms_test_request_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_test_request_decode(cms_test_request_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_test_response_encode(const cms_test_response_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_test_response_decode(cms_test_response_t *pdu, const uint8_t *in_buf, int in_len);
CMS_EXPORT int cms_test_error_encode(const cms_test_error_t *pdu, uint8_t *out_buf, int *out_len);
CMS_EXPORT int cms_test_error_decode(cms_test_error_t *pdu, const uint8_t *in_buf, int in_len);

#ifdef __cplusplus
}
#endif

#endif
